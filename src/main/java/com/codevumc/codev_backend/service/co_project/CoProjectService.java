package com.codevumc.codev_backend.service.co_project;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoRecruitOfProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;

public interface CoProjectService {
    CoDevResponse insertProject(CoProject coProject, JSONArray co_languages, JSONArray co_parts);
    CoDevResponse updateProject(CoProject coProject, JSONArray co_lanugages, JSONArray co_parts);
    CoDevResponse getCoProject(String co_viewer, long co_projectId);
    void updateMainImg(String co_mainImg, long co_projectId);
    CoDevResponse getCoProjects(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_sortingTag, String co_processTag, int limit, int offset, int page);
    CoDevResponse deleteCoProject(String co_email, long co_projectId);
    CoDevResponse updateCoProjectDeadLine(CoProject coProject);
}
