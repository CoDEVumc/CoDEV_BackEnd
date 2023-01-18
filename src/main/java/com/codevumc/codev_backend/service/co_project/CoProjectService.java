package com.codevumc.codev_backend.service.co_project;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;

public interface CoProjectService {
    void insertProject(CoProject coProject, JSONArray co_languages, JSONArray co_parts);
    CoDevResponse getCoProject(long co_projectId);
    void updateMainImg(String co_mainImg, long co_projectId);
    CoDevResponse getCoProjects(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_processTag);
}
