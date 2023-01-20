package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.domain.CoStudy;
import org.json.simple.JSONArray;

public interface CoStudyService {
    void insertStudy(CoStudy coStudy, JSONArray co_languages);
}
