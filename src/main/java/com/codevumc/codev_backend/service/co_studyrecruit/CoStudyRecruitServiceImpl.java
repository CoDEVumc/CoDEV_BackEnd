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

@Service
@AllArgsConstructor
public class CoStudyRecruitServiceImpl extends ResponseService implements CoStudyRecruitService {
    private final CoStudyMapper coStudyMapper;

    @Override
    public CoDevResponse submitCoStudy(CoRecruitOfStudy coRecruitOfStudy) {
        Map<String, Object> recruitDto = new HashMap<>();
        recruitDto.put("co_email", coRecruitOfStudy.getCo_email());
        recruitDto.put("co_studyId", coRecruitOfStudy.getCo_studyId());
        try {
            boolean coRecruitStatus = coStudyMapper.getCoRecruitStatus(coRecruitOfStudy.getCo_email(), coRecruitOfStudy.getCo_studyId());
            boolean isAlreadySubmit = coStudyMapper.isAlreadySubmit(coRecruitOfStudy.getCo_email(), coRecruitOfStudy.getCo_studyId());
            boolean isWriter = coStudyMapper.isWriter(recruitDto);
            if (!coRecruitStatus && !isWriter && !isAlreadySubmit) {
                this.coStudyMapper.insertCoRecruitOfStudy(coRecruitOfStudy);
                return setResponse(200, "Complete", "지원 완료되었습니다.");
            } else {
                return setResponse(446, "Fail", "지원 실패하였습니다.");
            }
        } catch (BindingException e) {
            try {
                return setResponse(403, "Forbidden", "잘못된 아이디 값 입니다.");
            } catch (Exception ex) {
                ex.printStackTrace();
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
            boolean isWriter = coStudyMapper.isWriter(recruitDto);
            if (coRecruitStatus && !isWriter) {
                this.coStudyMapper.deleteRecruitOfStudy(recruitDto);
                return setResponse(200, "Complete", "지원 취소되었습니다.");
            } else {
                return setResponse(403, "Forbidden", "수정 권한이 없습니다.");
            }
        } catch (BindingException e) {
            try {
                return setResponse(403, "Forbidden", "잘못된 아이디 값 입니다.");
            } catch (Exception ex) {
                ex.printStackTrace();
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
            boolean isWriter = coStudyMapper.isWriter(condition);
            List<CoRecruitOfStudy> applicants = this.coStudyMapper.getCoStudyApplicants(condition);
            if (isWriter) {
                if (applicants.isEmpty())
                    return setResponse(200, "Complete", "지원자가 없습니다.");
                else
                    return setResponse(200, "Complete", applicants);
            } else {
                return setResponse(403, "Forbidden", "권한이 없습니다.");
            }
        } catch (BindingException e) {
            try {
                return setResponse(403, "Forbidden", "잘못된 아이디 값 입니다.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
