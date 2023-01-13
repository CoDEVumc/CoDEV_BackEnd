package com.codevumc.codev_backend.service.co_project;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoProjectMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class CoProjectServiceImpl extends ResponseService implements CoProjectService {
    private final CoProjectMapper coProjectMapper;

    public void insertProject(CoProject coProject, long co_languageId, JSONArray co_parts) {
        this.coProjectMapper.insertCoProject(coProject);
        this.coProjectMapper.insertCoLanguageOfCoProject(coProject.getCo_projectId(), co_languageId);
        JSONObject jsonObj;
        for (Object co_part : co_parts) {
            jsonObj = (JSONObject) co_part;
            this.coProjectMapper.insertCoPartOfCoProject(coProject.getCo_projectId(), (long) jsonObj.get("co_partId"), (long) jsonObj.get("co_limit"));
        }
    }



//    public CoProject getCoProject(long co_projectId) {
//        Optional<CoProject> coProject = this.coProjectMapper.findByCoProjectId(co_projectId);
//        if(coProject.isPresent())
//            return coProject.get();
//        return null;
//    }

    public CoDevResponse getCoProject(CoProject coProject) {
        try {
            return setResponse(200, "Complete", coProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
