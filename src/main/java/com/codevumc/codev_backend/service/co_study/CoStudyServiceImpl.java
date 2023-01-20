package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.mapper.CoStudyMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Service
public class CoStudyServiceImpl extends ResponseService implements CoStudyService {
    private final CoStudyMapper coStudyMapper;
    private final CoPhotosMapper coPhotosMapper;

    @Override
    public void insertStudy(CoStudy coStudy, JSONArray co_languages) {
        this.coStudyMapper.insertCoStudy(coStudy);
        for (Object co_language : co_languages) {
            long co_languageId = (long) co_language;
            this.coStudyMapper.insertCoLanguageOfStudy(coStudy.getCo_studyId(), co_languageId);
        }
        Map<String, Object> coPartDto = new HashMap<>();
        coPartDto.put("co_studyId", coStudy.getCo_studyId());
        coPartDto.put("co_part", coStudy.getCo_part());
        coPartDto.put("co_limit", coStudy.getCo_limit());
        this.coStudyMapper.insertCoPartOfStudy(coPartDto);
    }

    @Override
    public void updateMainImg(String co_mainImg, long co_studyId) {
        coStudyMapper.updateCoMainImg(co_mainImg, co_studyId);
    }
}
