package com.codevumc.codev_backend.service.co_project;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoProjectMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.JSONString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@AllArgsConstructor
@Service
public class CoProjectServiceImpl extends ResponseService implements CoProjectService {
    private final CoProjectMapper coProjectMapper;

    @Override
    public void insertProject(CoProject coProject, JSONArray co_languages, JSONArray co_parts) {
        Map<String, Object> coPartsDto = new HashMap<>();
        this.coProjectMapper.insertCoProject(coProject);
        JSONObject jsonObj;
        for (Object co_language : co_languages) {
            long co_languageId = (long) co_language;
            this.coProjectMapper.insertCoLanguageOfProject(coProject.getCo_projectId(), co_languageId);
        }
        for (Object co_part : co_parts) {
            jsonObj = (JSONObject) co_part;
            coPartsDto.put("co_projectId", coProject.getCo_projectId());
            coPartsDto.put("co_part", jsonObj.get("co_part").toString());
            coPartsDto.put("co_limit", Long.parseLong(jsonObj.get("co_limit").toString()));
            this.coProjectMapper.insertCoPartOfProject(coPartsDto);
        }
    }

    public CoDevResponse getCoProjects(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_processTag) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("co_email", co_email);
        condition.put("co_locationTag", co_locationTag);
        condition.put("co_partTag", setting(co_partTag));
        condition.put("co_keyword", setting(co_keyword));
        condition.put("co_processTag", co_processTag);
        List<CoProject> coProjects = this.coProjectMapper.getCoProjects(condition);
        try {
            return setResponse(200, "success", coProjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getCoProject(CoProject coProject) {
        try {
            return setResponse(200, "Complete", coProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String setting(String keyword) {
        return keyword == null ? null : "%" + keyword + "%";
    }
}
