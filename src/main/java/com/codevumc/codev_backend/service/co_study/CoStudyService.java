package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.domain.CoStudy;
import org.json.simple.JSONArray;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoStudyService {
    void insertStudy(CoStudy coStudy, JSONArray co_languages);
    void updateMainImg(String co_mainImg, long co_studyId);
    CoDevResponse getCoStudy(long co_studyId);
    CoDevResponse getCoStudies(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_processTag);
}
