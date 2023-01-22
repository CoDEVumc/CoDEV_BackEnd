package com.codevumc.codev_backend.controller.co_project;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_project.CoProjectServiceImpl;
import com.codevumc.codev_backend.service.co_projectheart.CoProjectHeartServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/codev/project")
public class CoProjectController extends JwtController {

    public static final int SHOW_COUNT = 10;
    private final CoFileServiceImpl coFileService;
    private final CoProjectServiceImpl coProjectService;
    private final CoProjectHeartServiceImpl coProjectHeartService;

    public CoProjectController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoFileServiceImpl coFileService, CoProjectServiceImpl coProjectService, CoProjectHeartServiceImpl coProjectHeartService) {
        super(jwtTokenProvider, jwtService);
        this.coFileService = coFileService;
        this.coProjectService = coProjectService;
        this.coProjectHeartService = coProjectHeartService;
    }

    @GetMapping("/{coProjectId}")
    public CoDevResponse getProject(HttpServletRequest request, @PathVariable("coProjectId") long co_projectId){
        return coProjectService.getCoProject(co_projectId);
    }

    @PostMapping(consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse insertProject(HttpServletRequest request, @RequestPart Map<String, Object> project, @RequestPart(required = false) MultipartFile[] files) throws Exception {
        CoProject coProject = CoProject.builder()
                .co_email(getCoUserEmail(request))
                .co_title(project.get("co_title").toString())
                .co_location(project.get("co_location").toString())
                .co_content(project.get("co_content").toString())
                .co_process(CoProject.DevType.from("ING"))
                .co_deadLine(project.get("co_deadLine").toString()).build();
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String co_parts = gson.toJson(project.get("co_parts"));
        JSONArray co_partsList = (JSONArray) parser.parse(co_parts);
        String co_languages = gson.toJson(project.get("co_languages"));
        JSONArray co_languagesList = (JSONArray) parser.parse(co_languages);
        this.coProjectService.insertProject(coProject, co_languagesList, co_partsList);
        if (files != null) {
            List<CoPhotos> coPhotos = Arrays.asList(files)
                    .stream()
                    .map(file -> coFileService.storeFile(file, coProject.getCo_projectId(), "PROJECT"))
                    .collect(Collectors.toList());
            coProject.setCo_photos(coPhotos);

            coProjectService.updateMainImg(coFileService.getCo_MainImg("PROJECT", coProject.getCo_projectId()), coProject.getCo_projectId());
        }
        return null;
    }

    @GetMapping(value = "/projects/{page}")
    public CoDevResponse getAllProjects(HttpServletRequest request, @PathVariable(name = "page") int pageNum, @RequestParam("coLocationTag") String coLocationTag, @RequestParam("coPartTag") String coPartTag, @RequestParam("coKeyword") String coKeyword, @RequestParam("coProcessTag") String coProcessTag) throws Exception {
        int limit = getLimitCnt(pageNum);
        int offset = limit - SHOW_COUNT;
        return coProjectService.getCoProjects(getCoUserEmail(request), coLocationTag, coPartTag, coKeyword, coProcessTag, limit, offset, pageNum);
    }

    //찜하기
    @PatchMapping("/heart/{coProjectId}")
    public CoDevResponse heartOfProjectInsert(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectHeartService.insertHeart(co_email,co_projectId);
    }

    //찜하기 취소
    @PatchMapping("/nonheart/{coProjectId}")
    public CoDevResponse heartOfProjectDelete(HttpServletRequest request,  @PathVariable("coProjectId") Long co_projectId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectHeartService.deleteHeart(co_email,co_projectId);
    }

    @DeleteMapping("/out/{coProjectId}")
    public CoDevResponse deleteCoProject(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectService.deleteCoProject(co_email, co_projectId);
    }

    private int getLimitCnt(int pageNum) {
        int limit = SHOW_COUNT;
        for(int i = 0; i <= pageNum; i++) {
            if(i != 0)
                limit += SHOW_COUNT;
        }
        return limit;
    }
}

