package com.codevumc.codev_backend.controller.co_project;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_project.CoProjectServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.ibatis.annotations.Param;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/co_project")
public class CoProjectController extends JwtController {

    private final CoFileServiceImpl coFileService;
    private final CoProjectServiceImpl coProjectService;

    public CoProjectController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoFileServiceImpl coFileService, CoProjectServiceImpl coProjectService) {
        super(jwtTokenProvider, jwtService);
        this.coFileService = coFileService;
        this.coProjectService = coProjectService;
    }


    @PostMapping(value = "/p1/write", consumes = { MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse write(HttpServletRequest request, @RequestPart Map<String, String> project, @RequestPart(required = false) MultipartFile[] files) throws Exception {

        CoProject coProject = CoProject.builder()
                .co_email(getCoUserEmail(request))
                .co_title(project.get("co_title"))
                .co_location(project.get("co_location"))
                .co_content(project.get("co_content"))
                .co_deadLine(project.get("co_deadLine")).build();
        JSONParser parser = new JSONParser();
        Object coPartsObj = parser.parse(String.valueOf(project.get("co_parts")));
        JSONArray co_parts = (JSONArray) coPartsObj;
        Object coLanguagesObj = parser.parse(String.valueOf(project.get("co_languages")));
        JSONArray co_Languages = (JSONArray) coLanguagesObj;
        this.coProjectService.insertProject(coProject, co_Languages, co_parts);
        if(files != null) {
            List<CoPhotos> coPhotos = Arrays.asList(files)
                    .stream()
                    .map(file -> coFileService.storeFile(file, coProject.getCo_projectId()))
                    .collect(Collectors.toList());
            coProject.setCoPhotos(coPhotos);
        }else {
            //TO-DO
            //이미지 첨부 안했을 시 랜덤으로 사진 넣기
        }
        return null;
    }

    @GetMapping(value = "/p1/codev/projects/{coLocationTag}/{coPartTag}/{coKeyword}/{coProcessTag}")
    public CoDevResponse getAllProjects(
            @RequestParam("coLocationTag") String coLocationTag,
            @RequestParam("coPartTag") String coPartTag,
            @RequestParam("coKeyword") String coKeyword,
            @RequestParam("coProcessTag") String coProcessTag) {
        return coProjectService.getCoProjects(coLocationTag, coPartTag, coKeyword, coProcessTag);
    }
}

