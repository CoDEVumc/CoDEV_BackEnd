package com.codevumc.codev_backend.service.co_project;

import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
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
    private final CoPhotosMapper coPhotosMapper;

    private String setting(String keyword) {
        return keyword == null ? null : "%" + keyword + "%";
    }

    @Override
    public CoDevResponse insertProject(CoProject coProject, JSONArray co_languages, JSONArray co_parts) {
        try {
            Map<String, Object> coPartsDto = new HashMap<>();
            this.coProjectMapper.insertCoProject(coProject);

            return insertCoLanguageAndCoPart(co_languages, co_parts, coProject.getCo_projectId(), coPartsDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }

    }

    @Override
    public CoDevResponse updateProject(CoProject coProject, JSONArray co_languages, JSONArray co_parts) {
        try {
            Optional<CoProject> coProjectOptional = coProjectMapper.getCoProject(coProject.getCo_projectId());
            if(coProjectOptional.isPresent()) {
                if(!coProject.getCo_email().equals(coProjectOptional.get().getCo_email()))
                    return setResponse(403, "Forbidden", "수정 권한이 없습니다.");
            }
            Map<String, Object> coPartsDto = new HashMap<>();
            this.coProjectMapper.updateCoProject(coProject);

            this.coProjectMapper.deleteCoLanguageOfProject(coProject.getCo_projectId());
            this.coProjectMapper.deleteCoPartOfProject(coProject.getCo_projectId());

            return insertCoLanguageAndCoPart(co_languages, co_parts, coProject.getCo_projectId(), coPartsDto);

        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }

    }

    @Override
    public void updateMainImg(String co_mainImg, long co_projectId) {
        coProjectMapper.updateCoMainImg(co_mainImg, co_projectId);
    }

    @Override
    public CoDevResponse getCoProjects(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_processTag, int limit, int offset, int page) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("co_email", co_email);
        condition.put("co_locationTag", co_locationTag);
        condition.put("co_partTag", setting(co_partTag));
        condition.put("co_keyword", setting(co_keyword));
        condition.put("co_processTag", co_processTag);
        condition.put("limit", limit);
        condition.put("offset", offset);

        try {
            List<CoProject> coProjects = this.coProjectMapper.getCoProjects(condition);
            setResponse(200, "success", coProjects);
            return addResponse("co_page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getCoProject(long co_projectId) {
        try {
            Optional<CoProject> coProject = coProjectMapper.getCoProject(co_projectId);
            if(coProject.isPresent()) {
                coProject.get().setCo_partList(coProjectMapper.getCoPartList(co_projectId));
                coProject.get().setCo_languageList(coProjectMapper.getCoLanguageList(co_projectId));
                coProject.get().setCo_heartCount(coProjectMapper.getCoHeartCount(co_projectId));
                coProject.get().setCo_photos(coPhotosMapper.findByCoTargetId(String.valueOf(co_projectId), "PROJECT"));
            }
                return setResponse(200, "Complete", coProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse deleteCoProject(String co_email, long co_projectId) {
        Map<String, Object> coProjectDto = new HashMap<>();
        coProjectDto.put("co_email", co_email);
        coProjectDto.put("co_projectId", co_projectId);
        try {
            Optional<CoProject> coProject = coProjectMapper.getCoProject(co_projectId);
            if(coProject.isPresent()) {
                return coProjectMapper.deleteCoProject(coProjectDto) ? setResponse(200, "Complete", "삭제되었습니다.") : setResponse(403, "Forbidden", "수정 권한이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private CoDevResponse insertCoLanguageAndCoPart(JSONArray co_languages, JSONArray co_parts, long co_projectId, Map<String, Object> coPartsDto) throws Exception{
        for (Object co_language : co_languages) {
            long co_languageId = (long) co_language;
            this.coProjectMapper.insertCoLanguageOfProject(co_projectId, co_languageId);
        }
        JSONObject jsonObj;
        for (Object co_part : co_parts) {
            jsonObj = (JSONObject) co_part;
            coPartsDto.put("co_projectId", co_projectId);
            coPartsDto.put("co_part", jsonObj.get("co_part").toString());
            coPartsDto.put("co_limit", jsonObj.get("co_limit"));
            this.coProjectMapper.insertCoPartOfProject(coPartsDto);
        }
        return setResponse(200, "message", "프로젝트 모집글이 수정되었습니다.");
    }

}
