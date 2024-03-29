package com.codevumc.codev_backend.controller.co_project;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoProject.DevType;
import com.codevumc.codev_backend.domain.CoRecruitOfProject;
import com.codevumc.codev_backend.domain.CoTempSaveApplicants;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileService;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_project.CoProjectService;
import com.codevumc.codev_backend.service.co_project.CoProjectServiceImpl;
import com.codevumc.codev_backend.service.co_projectheart.CoProjectHeartService;
import com.codevumc.codev_backend.service.co_projectheart.CoProjectHeartServiceImpl;
import com.codevumc.codev_backend.service.co_projectrecruit.CoProjectRecruitService;
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
    private final CoFileService coFileService;
    private final CoProjectService coProjectService;
    private final CoProjectHeartService coProjectHeartService;
    private final CoProjectRecruitService coProjectRecruitService;

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
                .co_process(DevType.from("ING"))
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
        return coProjectService.getCoProjects(getCoUserEmail(request), co_locationTag, co_partTag, co_keyword, co_sortingTag.toUpperCase(), co_processTag, SHOW_COUNT, offset, pageNum);
    }

    //찜하기
    @PatchMapping("/heart/{coProjectId}")
    public CoDevResponse heartOfProjectDelete(HttpServletRequest request, @PathVariable("coProjectId") long coProjectId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectHeartService.changeHeart(co_email,coProjectId);
    }

    //수정하기
    @PutMapping(value = "/update/{coProjectId}", consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse updateCoProject(HttpServletRequest request, @PathVariable("coProjectId") long coProjectId, @RequestPart Map<String, Object> project, @RequestPart(required = false) MultipartFile[] files) throws Exception {
        CoProject coProject = CoProject.builder()
                .co_projectId(coProjectId)
                .co_email(getCoUserEmail(request))
                .co_title(project.get("co_title").toString())
                .co_location(project.get("co_location").toString())
                .co_content(project.get("co_content").toString())
                .co_process(DevType.from(project.get("co_process").toString()))
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
                .co_writer(portfolio.get("co_writer").toString())
                .co_partId(portfolio.get("co_partId").toString())
                .co_recruitStatus(Boolean.parseBoolean(portfolio.get("co_recruitStatus").toString()))
                .co_process(DevType.valueOf(portfolio.get("co_process").toString()))
                .co_motivation(portfolio.get("co_motivation").toString()).build();
        if(portfolio.get("co_portfolioId")!=null){
            coRecruitOfProject.setCo_portfolioId(Long.parseLong(portfolio.get("co_portfolioId").toString()));
        }
        return coProjectRecruitService.insertCoRecruitOfProject(coRecruitOfProject);

    }

    @DeleteMapping("/recruitment/{coProjectId}")
    public CoDevResponse cancelCoRecruitOfProject(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId, @RequestBody Map<String, Object> portfolio) throws Exception {
        CoRecruitOfProject coRecruitOfProject = CoRecruitOfProject.builder()
                .co_projectId(co_projectId)
                .co_email(getCoUserEmail(request))
                .co_writer(portfolio.get("co_writer").toString())
                .co_process(DevType.valueOf(portfolio.get("co_process").toString()))
                .co_recruitStatus(Boolean.parseBoolean(portfolio.get("co_recruitStatus").toString())).build();
        return coProjectRecruitService.cancelCoRecruitOfProject(coRecruitOfProject);
    }

    @PatchMapping("/recruitment/extension/{coProjectId}")
    public CoDevResponse updateCoProjectDeadLine(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId, @RequestBody Map<String, Object> project) throws Exception {
        CoProject coProject = CoProject.builder()
                .co_projectId(co_projectId)
                .co_email(getCoUserEmail(request))
                .co_deadLine(project.get("co_deadLine").toString()).build();
        return coProjectService.updateCoProjectDeadLine(coProject);
    }

    @PatchMapping("/recruitment/dead-line/{coProjectId}")
    public CoDevResponse closeCoProjectDeadLine(HttpServletRequest request, @PathVariable("coProjectId") Long co_projectId, @RequestBody Map<String, Object> user) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectRecruitService.closeCoProjectDeadLine(co_email, co_projectId, getJSONArray(user.get("co_emails")));
    }

    @GetMapping("/recruitment/{coProjectId}")
    public CoDevResponse getCoApplicantsOfProject(HttpServletRequest request, @PathVariable("coProjectId") long co_projectId, @RequestParam("coPart") String co_part) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectRecruitService.getCoApplicantsOfProject(co_email, co_projectId, co_part);
    }

    @GetMapping("/recruitment/portfolio/{coProjectId}/{coPortfolioId}")
    public CoDevResponse getCoPortfolioOfApplicant(HttpServletRequest request,
                                                   @PathVariable("coProjectId") long co_projectId,
                                                   @PathVariable("coPortfolioId") long co_portfolioId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coProjectRecruitService.getCoPortfolioOfApplicant(co_email, co_projectId, co_portfolioId);
    }

    @PatchMapping("/recruitment/pick/{coProjectId}")
    public CoDevResponse saveCoApplicantsTemporarily(HttpServletRequest request,
                                                     @PathVariable("coProjectId") long co_projectId,
                                                     @RequestBody CoTempSaveApplicants coTempSaveApplicants) throws Exception {
        String co_eamil = getCoUserEmail(request);
        return coProjectRecruitService.saveCoApplicantsTemporarily(co_eamil, co_projectId, coTempSaveApplicants);
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

