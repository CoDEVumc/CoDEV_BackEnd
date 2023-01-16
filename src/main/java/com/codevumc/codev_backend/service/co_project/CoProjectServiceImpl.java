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

//    public CoProject getCoProject(long co_projectId) {
//        Optional<CoProject> coProject = this.coProjectMapper.findByCoProjectId(co_projectId);
//        coProject.get().setCoParts(coProjectMapper.selectCoPartOfProject(co_projectId));
//        if(coProject.isPresent())
//            return coProject.get();
//        return null;
//    }

    @Override
    public CoDevResponse getCoProject(long co_projectId) {
        try {
            Optional<CoProject> coProject = coProjectMapper.getCoProject(co_projectId);
            if(coProject.isPresent()) {
                coProject.get().setCoPartList(coProjectMapper.getCoPartList(co_projectId));
                coProject.get().setCoLanguageList(coProjectMapper.getCoLanguageList(co_projectId));
                coProject.get().setCoHeartCount(coProjectMapper.getCoHeartCount(co_projectId));
            }
                return setResponse(200, "Complete", coProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
