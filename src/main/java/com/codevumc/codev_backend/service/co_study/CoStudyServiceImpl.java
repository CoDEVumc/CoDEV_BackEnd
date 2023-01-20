package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CoStudyServiceImpl extends ResponseService implements CoStudyService {
    private final CoStudyMapper coStudyMapper;
    private final CoPhotosMapper coPhotosMapper;

    @Override
    public CoDevResponse getCoStudy(long co_studyId) {
        try {
            Optional<CoStudy> coStudy = coStudyMapper.getCoStudy(co_studyId);
            if (coStudy.isPresent()) {
                coStudy.get().setCo_languageList(coStudyMapper.getCoLanguageList(co_studyId));
                coStudy.get().setCo_heartCount(coStudyMapper.getCoHeartCount(co_studyId));
                coStudy.get().setCo_photos(coPhotosMapper.findByCoStudyId(co_studyId));
                return setResponse(200, "Complete", coStudy);
            } else {
                return setResponse(403, "Forbidden", "불러오기 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
