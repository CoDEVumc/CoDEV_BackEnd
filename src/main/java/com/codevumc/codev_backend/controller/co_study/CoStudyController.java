package com.codevumc.codev_backend.controller.co_study;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoRecruitOfStudy;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.domain.CoStudy.DevType;
import com.codevumc.codev_backend.domain.CoTempSaveApplicants;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_study.CoStudyServiceImpl;
import com.codevumc.codev_backend.service.co_studyheart.CoStudyHeartServiceImpl;
import com.codevumc.codev_backend.service.co_studyrecruit.CoStudyRecruitServiceImpl;
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
@RequestMapping("/codev/study")
public class CoStudyController extends JwtController {
    public static final int SHOW_COUNT = 10;

    private final CoFileServiceImpl coFileService;
    private final CoStudyServiceImpl coStudyService;
    private final CoStudyHeartServiceImpl coStudyHeartService;
    private final CoStudyRecruitServiceImpl coStudyRecruitService;

    public CoStudyController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoFileServiceImpl coFileService, CoStudyServiceImpl coStudyService, CoStudyHeartServiceImpl coStudyHeartService, CoStudyRecruitServiceImpl coStudyRecruitService) {
        super(jwtTokenProvider, jwtService);
        this.coFileService = coFileService;
        this.coStudyService = coStudyService;
        this.coStudyHeartService = coStudyHeartService;
        this.coStudyRecruitService = coStudyRecruitService;
    }

    @GetMapping(value = "/studies/{page}")
    public CoDevResponse getCoStudies(HttpServletRequest request, @PathVariable(name = "page") int pageNum, @RequestParam("coLocationTag") String coLocationTag, @RequestParam("coPartTag") String coPartTag, @RequestParam("coKeyword") String coKeyword, @RequestParam("coSortingTag") String co_sortingTag, @RequestParam("coProcessTag") String coProcessTag) throws Exception {
        int limit = getLimitCnt(pageNum);
        int offset = limit - SHOW_COUNT;
        return coStudyService.getCoStudies(getCoUserEmail(request), coLocationTag, coPartTag, coKeyword, co_sortingTag.toUpperCase(), coProcessTag, SHOW_COUNT, offset, pageNum);
    }

    @PatchMapping("/heart/{coStudyId}")
    public CoDevResponse heartOfStudyUpdate(HttpServletRequest request, @PathVariable("coStudyId") Long co_studyId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coStudyHeartService.changeHeart(co_email, co_studyId);
    }

    //스터디 글쓰기
    @PostMapping(consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse insertStudy(HttpServletRequest request, @RequestPart Map<String, Object> study, @RequestPart(required = false) MultipartFile[] files) throws Exception {
        CoStudy coStudy = CoStudy.builder()
                .co_email(getCoUserEmail(request))
                .co_title(study.get("co_title").toString())
                .co_location(study.get("co_location").toString())
                .co_content(study.get("co_content").toString())
                .co_process(DevType.from("ING"))
                .co_part(study.get("co_part").toString())
                .co_total((Integer) study.get("co_total"))
                .co_deadLine((study.get("co_deadLine").toString())).build();

        CoDevResponse result = coStudyService.insertStudy(coStudy, getJSONArray(study.get("co_languages")));
        if (files != null) {
            coStudy.setCo_photos(uploadPhotos(files, String.valueOf(coStudy.getCo_studyId())));
        }
        return result;
    }


    @GetMapping("/{coStudyId}")
    public CoDevResponse getCoStudy(HttpServletRequest request, @PathVariable("coStudyId") long coStudyId) throws Exception {
        String co_viewer = getCoUserEmail(request);
        return coStudyService.getCoStudy(co_viewer, coStudyId);
    }

    @PutMapping(value = "/update/{coStudyId}", consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse updateCoStudy(HttpServletRequest request, @PathVariable("coStudyId") long coStudyId, @RequestPart Map<String, Object> study, @RequestPart(required = false) MultipartFile[] files) throws Exception {
        CoStudy coStudy = CoStudy.builder()
                .co_studyId(coStudyId)
                .co_email(getCoUserEmail(request))
                .co_title(study.get("co_title").toString())
                .co_location(study.get("co_location").toString())
                .co_content(study.get("co_content").toString())
                .co_process(DevType.from(study.get("co_process").toString()))
                .co_part(study.get("co_part").toString())
                .co_total((Integer) study.get("co_total"))
                .co_deadLine((study.get("co_deadLine").toString())).build();

        CoDevResponse result = coStudyService.updateStudy(coStudy, getJSONArray(study.get("co_languages")));
        coFileService.deleteFile(String.valueOf(coStudy.getCo_studyId()), "STUDY");
        if(files != null) {
            coStudy.setCo_photos(uploadPhotos(files, String.valueOf(coStudy.getCo_studyId())));
        }
        return result;
    }


    @DeleteMapping("/{coStudyId}")
    public CoDevResponse deleteStudy(HttpServletRequest request, @PathVariable("coStudyId") long coStudyId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coStudyService.deleteStudy(co_email, coStudyId);
    }

    @PostMapping("/submission/{coStudyId}")
    public CoDevResponse submitCoStudy(HttpServletRequest request,
                                       @PathVariable("coStudyId") long coStudyId,
                                       @RequestBody Map<String, Object> recruitStudy) throws Exception {
        CoRecruitOfStudy coRecruitOfStudy = CoRecruitOfStudy.builder()
                .co_studyId(coStudyId)
                .co_email(getCoUserEmail(request))
                .co_writer(recruitStudy.get("co_writer").toString())
                .co_recruitStatus(Boolean.parseBoolean(recruitStudy.get("co_recruitStatus").toString()))
                .co_process(DevType.valueOf(recruitStudy.get("co_process").toString()))
                .co_portfolioId(Long.parseLong(recruitStudy.get("co_portfolioId").toString()))
                .co_motivation(recruitStudy.get("co_motivation").toString())
                .build();
        return coStudyRecruitService.submitCoStudy(coRecruitOfStudy);
    }

    @DeleteMapping("/recruitment/{coStudyId}")
    public CoDevResponse cancelRecruitStudy(HttpServletRequest request, @PathVariable("coStudyId") long co_studyId, @RequestBody Map<String, Object> portfolio) throws Exception {
        CoRecruitOfStudy coRecruitOfStudy = CoRecruitOfStudy.builder()
                .co_studyId(co_studyId)
                .co_email(getCoUserEmail(request))
                .co_writer(portfolio.get("co_writer").toString())
                .co_process(DevType.valueOf(portfolio.get("co_process").toString()))
                .co_recruitStatus(Boolean.parseBoolean(portfolio.get("co_recruitStatus").toString()))
                .build();
        return coStudyRecruitService.cancelRecruitStudy(coRecruitOfStudy);
    }

    @GetMapping("/recruitment/portfolio/{coStudyId}/{coPortfolioId}")
    public CoDevResponse getCoPortfolioOfApplicant(HttpServletRequest request, @PathVariable("coStudyId") long co_studyId, @PathVariable("coPortfolioId") long co_portfolioId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coStudyRecruitService.getCoPortfolioOfApplicant(co_email,co_studyId,co_portfolioId);
    }

    private int getLimitCnt(int pageNum) {
        int limit = SHOW_COUNT;
        for (int i = 0; i <= pageNum; i++) {
            if (i != 0)
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
                .map(file -> coFileService.storeFile(file, co_targetId, "STUDY"))
                .collect(Collectors.toList());

        coStudyService.updateMainImg(coFileService.getCo_MainImg("STUDY", co_targetId), Long.parseLong(co_targetId));
        return coPhotos;
    }

    @PatchMapping("/recruitment/extension/{coStudyId}")
    public CoDevResponse updateCoStudyDeadLine(HttpServletRequest request, @PathVariable("coStudyId") long coStudyId, @RequestBody Map<String, Object> study) throws Exception {
        CoStudy coStudy = CoStudy.builder()
                .co_studyId(coStudyId)
                .co_email(getCoUserEmail(request))
                .co_deadLine(study.get("co_deadLine").toString())
                .build();
        return coStudyService.updateCoStudyDeadLine(coStudy);
    }

    @PatchMapping("/recruitment/completion/{coStudyId}")
    public CoDevResponse completeCoStudyRecruitment(HttpServletRequest request,
                                                    @PathVariable("coStudyId") long coStudyId,
                                                    @RequestBody Map<String, Object> user) throws Exception {
        return coStudyRecruitService.completeCoStudyRecruitment(getCoUserEmail(request), coStudyId, getJSONArray(user.get("co_emails")));
    }

    @GetMapping("/recruitment/{coStudyId}")
    public CoDevResponse getCoApplicantsOfStudy(HttpServletRequest request,
                                                @PathVariable("coStudyId") long co_studyId,
                                                @RequestParam("coPart") String co_part) throws Exception {
        String co_email = getCoUserEmail(request);
        return coStudyRecruitService.getCoApplicantsOfStudy(co_email, co_studyId, co_part);
    }

    @PatchMapping("/recruitment/pick/{coStudyId}")
    public CoDevResponse saveCoApplicantsTemporarily(HttpServletRequest request,
                                                     @PathVariable("coStudyId") long co_studyId,
                                                     @RequestBody CoTempSaveApplicants coTempSaveApplicants) throws Exception {
        String co_email = getCoUserEmail(request);
        return coStudyRecruitService.saveCoApplicantsTemporarily(co_email, co_studyId, coTempSaveApplicants);
    }

}
