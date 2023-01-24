package com.codevumc.codev_backend.service.co_studyrecruit;

import com.codevumc.codev_backend.domain.CoRecruitOfStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class CoStudyRecruitServiceImpl extends ResponseService implements CoStudyRecruitService {
    private final CoStudyMapper coStudyMapper;

    @Override
    public CoDevResponse submitCoStudy(CoRecruitOfStudy coRecruitOfStudy) {
        try {
            boolean coRecruitStatus = coStudyMapper.getCoRecruitStatus(coRecruitOfStudy.getCo_email(), coRecruitOfStudy.getCo_studyId());
            if (!coRecruitStatus) {
                this.coStudyMapper.insertCoRecruitOfStudy(coRecruitOfStudy);
                return setResponse(200, "Complete", "지원 완료되었습니다.");
            } else {
                return setResponse(446, "Fail", "지원 실패하였습니다.");
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
            if (coRecruitStatus) {
                this.coStudyMapper.deleteRecruitOfStudy(recruitDto);
                return setResponse(200, "Complete", "지원 취소되었습니다.");
            } else {
                return setResponse(403, "Forbidden", "수정 권한이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
