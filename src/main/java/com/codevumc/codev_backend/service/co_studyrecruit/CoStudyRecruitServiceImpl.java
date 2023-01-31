package com.codevumc.codev_backend.service.co_studyrecruit;

import com.codevumc.codev_backend.domain.CoApplicantsInfoOfStudy;
import com.codevumc.codev_backend.domain.CoRecruitOfStudy;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CoStudyRecruitServiceImpl extends ResponseService implements CoStudyRecruitService {
    private final CoStudyMapper coStudyMapper;

    @Override
    public CoDevResponse submitCoStudy(CoRecruitOfStudy coRecruitOfStudy) {
        try {
            boolean coRecruitStatus = coStudyMapper.getCoRecruitStatus(coRecruitOfStudy.getCo_email(), coRecruitOfStudy.getCo_studyId());
            Optional<CoStudy> coStudy = coStudyMapper.getCoStudy(coRecruitOfStudy.getCo_studyId());
            if(coStudy.isPresent()) {
                if(coStudy.get().getCo_email().equals(coRecruitOfStudy.getCo_email()))
                    return setResponse(403, "Forbidden", "작성자는 지원할 수 없습니다..");
                if (!coRecruitStatus) {
                    this.coStudyMapper.insertCoRecruitOfStudy(coRecruitOfStudy);
                    return setResponse(200, "message", "지원 완료되었습니다.");
                } else {
                    return setResponse(445,"message","이미 지원한 프로젝트입니다");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse cancelRecruitStudy(String co_email, long co_studyId) {
        Map<String, Object> recruitDto = new HashMap<>();
        recruitDto.put("co_email", co_email);
        recruitDto.put("co_studyId", co_studyId);

        try {
            boolean coRecruitStatus = coStudyMapper.getCoRecruitStatus(co_email, co_studyId);
            Optional<CoStudy> coStudy = coStudyMapper.getCoStudy(co_studyId);
            if(coStudy.isPresent()) {
                if(coStudy.get().getCo_email().equals(co_email))
                    return setResponse(403, "Forbidden", "작성자는 지원할 수 없습니다..");
                if (coRecruitStatus) {
                    this.coStudyMapper.deleteRecruitOfStudy(recruitDto);
                    return setResponse(200, "message", "지원 취소되었습니다.");
                } else {
                    return setResponse(445, "message", "이미 지원이 취소되었습니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse completeCoStudyRecruitment(String co_email, long co_studyId, CoStudy co_applicantList) {
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("co_email", co_email);
            condition.put("co_studyId", co_studyId);
            Optional<CoStudy> coStudy = coStudyMapper.getCoStudy(co_studyId);
            if(coStudy.isPresent()) {
                if(coStudy.get().getCo_email().equals(co_email)) {
                    List<CoRecruitOfStudy> applicants = co_applicantList.getCo_applicantList();
                    this.coStudyMapper.completeCoStudyRecruitment(condition);
                    for (CoRecruitOfStudy applicant : applicants) {
                        this.coStudyMapper.updateCoStudyMemberApprove(applicant.getCo_email(), co_studyId);
                    }
                    return setResponse(200, "message", "모집 마감되었습니다.");
                }else
                    return setResponse(403, "Forbidden", "권한이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getCoApplicantsOfStudy(String co_email, long co_studyId, String co_part) {
        try {
            Optional<CoStudy> coStudyOptional = coStudyMapper.getCoStudy(co_studyId);
            if (coStudyOptional.isPresent()) {
                if (!coStudyOptional.get().getCo_email().equals(co_email))
                    return setResponse(403, "Forbidden", "조회 권한이 없습니다.");
                if (!coStudyOptional.get().getCo_part().equals(co_part) && !co_part.equalsIgnoreCase("temp"))
                    return setResponse(400, "Bad Request", "모집중인 파트가 아닙니다.");
                Map<String, Object> coStudyDto = new HashMap<>();
                coStudyDto.put("co_studyId", co_studyId);
                coStudyDto.put("co_part", co_part.toUpperCase());

                CoApplicantsInfoOfStudy coApplicantsInfoOfStudy = CoApplicantsInfoOfStudy.builder()
                        .co_part(co_part.toUpperCase())
                        .co_tempSavedApplicantsCount(this.coStudyMapper.getTempsavedApplicantsCount(co_studyId))
                        .co_applicantCount(this.coStudyMapper.getCoApplicantCount(co_studyId))
                        .co_applicantsInfo(this.coStudyMapper.getCoApplicantsInfo(coStudyDto))
                        .build();
                return setResponse(200, "message", coApplicantsInfoOfStudy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
