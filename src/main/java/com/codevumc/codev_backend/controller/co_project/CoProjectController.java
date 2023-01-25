package com.codevumc.codev_backend.controller.co_project;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoRecruitOfProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_project.CoProjectServiceImpl;
import com.codevumc.codev_backend.service.co_projectheart.CoProjectHeartServiceImpl;
import com.codevumc.codev_backend.service.co_projectrecruit.CoProjectRecruitServiceImpl;
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
    private final CoProjectRecruitServiceImpl coProjectRecruitService;

    public CoProjectController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoFileServiceImpl coFileService, CoProjectServiceImpl coProjectService, CoProjectHeartServiceImpl coProjectHeartService, CoProjectRecruitServiceImpl coProjectRecruitService) {
        super(jwtTokenProvider, jwtService);
        this.coFileService = coFileService;
        this.coProjectService = coProjectService;
        this.coProjectHeartService = coProjectHeartService;
        this.coProjectRecruitService = coProjectRecruitService;
    }

    //상세보기
    @GetMapping("/{coProjectId}")
    public CoDevResponse getProject(HttpServletRequest request, @PathVariable("coProjectId") long co_projectId) throws Exception {
        String co_viewer = getCoUserEmail(request);
        return coProjectService.getCoProject(co_viewer,co_projectId);
    }

    //프로젝트 글쓰기
    @PostMapping(consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse insertProject(HttpServletRequest request, @RequestPart Map<String, Object> project, @RequestPart(required = false) MultipartFile[] files) throws Exception {
        CoProject coProject = CoProject.builder()
                .co_email(getCoUserEmail(request))
                .co_title(project.get("co_title").toString())
                .co_location(project.get("co_location").toString())
                .co_content(project.get("co_content").toString())
                .co_process(CoProject.DevType.from("ING"))
                .co_deadLine(project.get("co_deadLine").toString()).build();

        CoDevResponse result = coProjectService.insertProject(coProject, getJSONArray(project.get("co_languages")), getJSONArray(project.get("co_parts")));
        if (files != null) {
            coProject.setCo_photos(uploadPhotos(files, String.valueOf(coProject.getCo_projectId())));
        }
        return result;
    }

    //프로젝트 리스트
    @GetMapping(value = "/projects/{page}")
    public CoDevResponse getAllProjects(HttpServletRequest request, @PathVariable(name = "page") int pageNum, @RequestParam("coLocationTag") String co_locationTag, @RequestParam("coPartTag") String co_partTag, @RequestParam("coKeyword") String co_keyword, @RequestParam("coSortingTag") String co_sortingTag, @RequestParam("coProcessTag") String co_processTag) throws Exception {
        int limit = getLimitCnt(pageNum);
        int offset = limit - SHOW_COUNT;
        return coProjectService.getCoProjects(getCoUserEmail(request), co_locationTag, co_partTag, co_keyword, co_sortingTag.toUpperCase(), co_processTag, limit, offset, pageNum);
    }

    //찜하기
    @PatchMapping("/heart/{coProjectId}")
    public CoDevResponse heartOfProjectDelete(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectHeartService.changeHeart(co_email,co_projectId);
    }

    //수정하기
    @PutMapping(value = "/update/{coProjectId}", consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse updateCoProject(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId, @RequestPart Map<String, Object> project, @RequestPart(required = false) MultipartFile[] files) throws Exception {
        CoProject coProject = CoProject.builder()
                .co_projectId(co_projectId)
                .co_email(getCoUserEmail(request))
                .co_title(project.get("co_title").toString())
                .co_location(project.get("co_location").toString())
                .co_content(project.get("co_content").toString())
                .co_process(CoProject.DevType.from(project.get("co_process").toString()))
                .co_deadLine(project.get("co_deadLine").toString()).build();

        CoDevResponse result = coProjectService.updateProject(coProject, getJSONArray(project.get("co_languages")), getJSONArray(project.get("co_parts")));
        coFileService.deleteFile(String.valueOf(coProject.getCo_projectId()), "PROJECT");
        if (files != null) {
            coProject.setCo_photos(uploadPhotos(files, String.valueOf(coProject.getCo_projectId())));
        }
        return result;
    }

    //글 삭제
    @DeleteMapping("/out/{coProjectId}")
    public CoDevResponse deleteCoProject(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectService.deleteCoProject(co_email, co_projectId);
    }

    @PostMapping("/submission/{coProjectId}")
    public CoDevResponse insertCoRecruitOfProject(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId, @RequestBody Map<String, Object> portfolio) throws Exception{
        CoRecruitOfProject coRecruitOfProject = CoRecruitOfProject.builder()
                .co_projectId(co_projectId)
                .co_email(getCoUserEmail(request))
                .co_portfolioId(Long.parseLong(portfolio.get("co_portfolioId").toString()))
                .co_partId(portfolio.get("co_partId").toString())
                .co_motivation(portfolio.get("co_motivation").toString()).build();
        return coProjectRecruitService.insertCoRecruitOfProject(coRecruitOfProject);

    }

    @DeleteMapping("/recruitment/{coProjectId}")
    public CoDevResponse cancelCoRecruitOfProject(HttpServletRequest request, @PathVariable("coProjectId") long co_projectId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectRecruitService.cancelCoRecruitOfProject(co_email,co_projectId);
    }

    @PatchMapping("/recruitment/extension/{coProjectId}")
    public CoDevResponse updateCoProjectDeadLine(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId, @RequestBody Map<String, Object> project) throws Exception {
        CoProject coProject = CoProject.builder()
                .co_projectId(co_projectId)
                .co_email(getCoUserEmail(request))
                .co_deadLine(project.get("co_deadLine").toString()).build();
        return coProjectService.updateCoProjectDeadLine(coProject);
    }

    private int getLimitCnt(int pageNum) {
        int limit = SHOW_COUNT;
        for(int i = 0; i <= pageNum; i++) {
            if(i != 0)
                limit += SHOW_COUNT;
        }
        return limit;
    }

    private JSONArray getJSONArray(Object object) throws Exception{
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(object);
        return (JSONArray) parser.parse(jsonArray);
    }

    private List<CoPhotos> uploadPhotos(MultipartFile[] files, String co_targetId) {
        List<CoPhotos> coPhotos = Arrays.asList(files)
                .stream()
                .map(file -> coFileService.storeFile(file, co_targetId, "PROJECT"))
                .collect(Collectors.toList());

        coProjectService.updateMainImg(coFileService.getCo_MainImg("PROJECT", co_targetId), Long.parseLong(co_targetId));
        return coPhotos;
    }
}

