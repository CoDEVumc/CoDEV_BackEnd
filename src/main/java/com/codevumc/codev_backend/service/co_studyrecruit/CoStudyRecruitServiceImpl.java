package com.codevumc.codev_backend.service.co_studyrecruit;

import com.codevumc.codev_backend.domain.*;
import com.codevumc.codev_backend.domain.CoStudy.DevType;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CoStudyRecruitServiceImpl extends ResponseService implements CoStudyRecruitService {
    private final CoStudyMapper coStudyMapper;

    @Override
    public CoDevResponse submitCoStudy(CoRecruitOfStudy coRecruitOfStudy) {
        try {
            if (isAdmin(coRecruitOfStudy))
                return setResponse(403, "Forbidden", "작성자는 지원할 수 없습니다.");
            if (!DevType.FIN.equals(coRecruitOfStudy.getCo_process()) && !coRecruitOfStudy.isCo_recruitStatus()) {
                this.coStudyMapper.insertCoRecruitOfStudy(coRecruitOfStudy);
                return setResponse(200, "message", "지원 완료되었습니다.");
            } else
                return setResponse(445, "message", "이미 지원했거나 마감된 스터디입니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse cancelRecruitStudy(CoRecruitOfStudy coRecruitOfStudy) {
        try {
            if (isAdmin(coRecruitOfStudy))
                return setResponse(403, "Forbidden", "작성자는 취소할 수 없습니다.");
            if (!DevType.FIN.equals(coRecruitOfStudy.getCo_process()) && coRecruitOfStudy.isCo_recruitStatus()) {
                this.coStudyMapper.deleteRecruitOfStudy(coRecruitOfStudy);
                return setResponse(200, "message", "지원 취소되었습니다.");
            } else
                return setResponse(445, "message", "이미 취소했거나 마감된 스터디입니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse completeCoStudyRecruitment(String co_email, long co_studyId, JSONArray co_applicantList) {
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("co_email", co_email);
            condition.put("co_studyId", co_studyId);
            Optional<CoStudy> coStudy = coStudyMapper.getCoStudy(co_studyId);
            if(coStudy.isPresent()) {
                if(coStudy.get().getCo_email().equals(co_email)) {
                    this.coStudyMapper.completeCoStudyRecruitment(condition);
                    for(Object email : co_applicantList) {
                        String co_applicantEmail = (String) email;
                        this.coStudyMapper.updateCoStudyMemberApprove(co_applicantEmail, co_studyId);
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
                        .co_applicantCount(this.coStudyMapper.getCoApplicantsCount(co_studyId))
                        .co_applicantsInfo(this.coStudyMapper.getCoApplicantsInfo(coStudyDto))
                        .build();
                return setResponse(200, "message", coApplicantsInfoOfStudy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getCoPortfolioOfApplicant(String co_email, long co_studyId, long co_portfolioId) {
        try{
            Optional<CoStudy> coStudyOptional = coStudyMapper.getCoStudy(co_studyId);
            if(coStudyOptional.isPresent()){
                if(!coStudyOptional.get().getCo_email().equals(co_email))
                    return setResponse(403, "Forbidden", "조회 권한이 없습니다.");
                Map<String, Object> coPortfolioDto = new HashMap<>();
                coPortfolioDto.put("co_studyId", co_studyId);
                coPortfolioDto.put("co_portfolioId", co_portfolioId);
                Optional<CoPortfolioOfApplicant> coPortfolioOptional = this.coStudyMapper.getCoPortfolioOfApplicant(coPortfolioDto);
                return coPortfolioOptional.isPresent() ? setResponse(200, "message", coPortfolioOptional.get()) : setResponse(400, "Bad Request", "존재하지 않는 포트폴리오입니다.");

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse saveCoApplicantsTemporarily(String co_email, long co_studyId, CoTempSaveApplicants coTempSaveApplicants) {
        try {
            Optional<CoStudy> coStudyOptional = this.coStudyMapper.getCoStudy(co_studyId);
            if (!coStudyOptional.get().getCo_email().equals(co_email)) {
                return setResponse(403, "Forbidden", "조회 권한이 없습니다.");
            }
            Map<String, Object> coApplicantsInfoDto = new HashMap<>();
            coApplicantsInfoDto.put("co_emails", coTempSaveApplicants.getCo_emails());
            coApplicantsInfoDto.put("co_studyId", co_studyId);
            if (!coTempSaveApplicants.checkAllTempSave(this.coStudyMapper.getCoTemporaryStorage(coApplicantsInfoDto))) {
                return setResponse(400, "message", "임시저장 여부가 다른 지원자가 존재합니다.");
            }
            if (this.coStudyMapper.updateCoTemporaryStorage(coApplicantsInfoDto)) {
                return setResponse(200, "message", "일괄 처리 되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAdmin(CoRecruitOfStudy coRecruitOfStudy) throws Exception {
        if (coRecruitOfStudy.getCo_email().equals(coRecruitOfStudy.getCo_writer())){
            return true;
        }
        return false;
    }

}
