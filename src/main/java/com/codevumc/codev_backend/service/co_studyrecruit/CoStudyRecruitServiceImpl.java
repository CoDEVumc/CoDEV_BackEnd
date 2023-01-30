package com.codevumc.codev_backend.service.co_studyrecruit;

import com.codevumc.codev_backend.domain.CoRecruitOfStudy;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.apache.ibatis.binding.BindingException;
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
    public CoDevResponse getCoStudyApplicants(String co_email, long co_studyId) {
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("co_email", co_email);
            condition.put("co_studyId", co_studyId);
            Optional<CoStudy> coStudy = coStudyMapper.getCoStudy(co_studyId);
            if(coStudy.isPresent()) {
                if(coStudy.get().getCo_email().equals(co_email)) {
                    List<CoRecruitOfStudy> applicants = this.coStudyMapper.getCoStudyApplicants(condition);
                    return setResponse(200, "Complete", applicants);
                }else
                    return setResponse(403, "Forbidden", "권한이 없습니다.");
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
                    if (applicants.size() > coStudy.get().getCo_total()){
                        return setResponse(446, "message", "지원 인원을 초과하였습니다.");
                    } else {
                        this.coStudyMapper.completeCoStudyRecruitment(condition);
                        for (CoRecruitOfStudy applicant : applicants) {
                            this.coStudyMapper.updateCoStudyMemberApprove(applicant.getCo_email(), co_studyId);
                        }
                        return setResponse(200, "message", "모집 마감되었습니다.");
                    }
                }else
                    return setResponse(403, "Forbidden", "권한이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
