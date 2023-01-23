package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CoStudyServiceImpl extends ResponseService implements CoStudyService {
    private final CoStudyMapper coStudyMapper;
    private final CoPhotosMapper coPhotosMapper;

    private String setting(String keyword) {
        return keyword == null ? null : "%" + keyword + "%";
    }

    @Override
    public CoDevResponse insertStudy(CoStudy coStudy, JSONArray co_languages) {
        try {
            this.coStudyMapper.insertCoStudy(coStudy);
            for (Object co_language : co_languages) {
                long co_languageId = (long) co_language;
                this.coStudyMapper.insertCoLanguageOfStudy(coStudy.getCo_studyId(), co_languageId);
            }
            Map<String, Object> coPartDto = new HashMap<>();
            coPartDto.put("co_studyId", coStudy.getCo_studyId());
            coPartDto.put("co_part", coStudy.getCo_part());
            coPartDto.put("co_total", coStudy.getCo_total());
            this.coStudyMapper.insertCoPartOfStudy(coPartDto);
            return setResponse(200, "message", "스터디 모집글이 작성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }

    }

    @Override
    public void updateMainImg(String co_mainImg, long co_studyId) {
        coStudyMapper.updateCoMainImg(co_mainImg, co_studyId);
    }

    @Override
    public CoDevResponse getCoStudy(long co_studyId) {
        try {
            Optional<CoStudy> coStudy = coStudyMapper.getCoStudy(co_studyId);
            if (coStudy.isPresent()) {
                coStudy.get().setCo_languageList(coStudyMapper.getCoLanguageList(co_studyId));
                coStudy.get().setCo_heartCount(coStudyMapper.getCoHeartCount(co_studyId));
                coStudy.get().setCo_photos(coPhotosMapper.findByCoTargetId(String.valueOf(co_studyId), "STUDY"));
                return setResponse(200, "Complete", coStudy);
            } else {
                return setResponse(403, "Forbidden", "불러오기 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getCoStudies(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_processTag, int limit, int offset, int page) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("co_email", co_email);
        condition.put("co_locationTag", co_locationTag);
        condition.put("co_partTag", setting(co_partTag));
        condition.put("co_keyword", setting(co_keyword));
        condition.put("co_processTag", co_processTag);
        condition.put("limit", limit);
        condition.put("offset", offset);
        List<CoStudy> coStudies = this.coStudyMapper.getCoStudies(condition);
        try {
            setResponse(200, "success", coStudies);
            return addResponse("co_page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
