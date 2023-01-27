package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;

public interface CoStudyService {
    CoDevResponse insertStudy(CoStudy coStudy, JSONArray co_languages);
    CoDevResponse updateStudy(CoStudy coStudy, JSONArray co_languages);
    void updateMainImg(String co_mainImg, long co_studyId);
    CoDevResponse getCoStudies(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_sortingTag, String co_processTag, int limit, int offset, int page);
    CoDevResponse getCoStudy(String co_viewer, long co_studyId);
    CoDevResponse deleteStudy(String coUserEmail, long coStudyId);
    CoDevResponse updateCoStudyDeadLine(CoStudy coStudy);
}
