package com.codevumc.codev_backend.service.co_project;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoProjectMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class CoProjectServiceImpl extends ResponseService implements CoProjectService {
    private final CoProjectMapper coProjectMapper;

    public void insertProject(CoProject coProject, JSONArray co_languages, JSONArray co_parts) throws ParseException {
        this.coProjectMapper.insertCoProject(coProject);
        //this.coProjectMapper.insertCoLanguageOfProject(coProject.getCo_projectId(), co_languageId);
        JSONObject jsonObj;
        for (Object co_language : co_languages) {
            long co_languageId = Integer.parseInt(co_language.toString());
            this.coProjectMapper.insertCoLanguageOfProject(coProject.getCo_projectId(), co_languageId);
        }
        for (Object co_part : co_parts) {
            jsonObj = (JSONObject) co_part;
            this.coProjectMapper.insertCoPartOfProject(coProject.getCo_projectId(), Long.parseLong(jsonObj.get("co_partId").toString()), Long.parseLong(jsonObj.get("co_limit").toString()));
        }
    }

//    public CoProject getCoProject(long co_projectId) {
//        Optional<CoProject> coProject = this.coProjectMapper.findByCoProjectId(co_projectId);
//        coProject.get().setCoParts(coProjectMapper.selectCoPartOfProject(co_projectId));
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
