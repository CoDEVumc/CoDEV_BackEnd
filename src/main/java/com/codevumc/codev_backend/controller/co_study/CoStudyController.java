package com.codevumc.codev_backend.controller.co_study;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.domain.CoRecruitOfStudy;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_study.CoStudyServiceImpl;
import com.codevumc.codev_backend.service.co_studyheart.CoStudyHeartServiceImpl;
import com.codevumc.codev_backend.service.co_studyrecruit.CoStudyRecruitServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import org.springframework.web.bind.annotation.*;
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
        return coStudyService.getCoStudies(getCoUserEmail(request), coLocationTag, coPartTag, coKeyword, co_sortingTag.toUpperCase(), coProcessTag, limit, offset, pageNum);
    }

    @PatchMapping("/heart/{coStudyId}")
    public CoDevResponse heartOfStudyUpdate(HttpServletRequest request, @PathVariable("coStudyId") Long co_studyId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coStudyHeartService.changeHeart(co_email, co_studyId);
    }

    @PostMapping(consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse insertStudy(HttpServletRequest reqest, @RequestPart Map<String, Object> study, @RequestPart(required = false) MultipartFile[] files) throws Exception {
        CoStudy coStudy = CoStudy.builder()
                .co_email(getCoUserEmail(reqest))
                .co_title(study.get("co_title").toString())
                .co_location(study.get("co_location").toString())
                .co_content(study.get("co_content").toString())
                .co_process(CoStudy.DevType.from("ING"))
                .co_part(study.get("co_part").toString())
                .co_total((Integer) study.get("co_total"))
                .co_deadLine((study.get("co_deadLine").toString())).build();
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String co_languageListByString = gson.toJson(study.get("co_languages"));
        JSONArray co_languageList = (JSONArray) parser.parse(co_languageListByString);
        CoDevResponse result = coStudyService.insertStudy(coStudy, co_languageList);
        if (files != null) {
            List<CoPhotos> coPhotos = Arrays.asList(files)
                    .stream()
                    .map(file -> coFileService.storeFile(file, String.valueOf(coStudy.getCo_studyId()), "STUDY"))
                    .collect(Collectors.toList());
            coStudy.setCo_photos(coPhotos);
            coStudyService.updateMainImg(coFileService.getCo_MainImg("STUDY", String.valueOf(coStudy.getCo_studyId())), coStudy.getCo_studyId());
        }
        return result;
    }


    @GetMapping("/{coStudyId}")
    public CoDevResponse getCoStudy(HttpServletRequest request, @PathVariable("coStudyId") long coStudyId) throws Exception {
        String co_viewer = getCoUserEmail(request);
        return coStudyService.getCoStudy(co_viewer, coStudyId);
    }

    @DeleteMapping("/{coStudyId}")
    public CoDevResponse deleteStudy(HttpServletRequest request, @PathVariable("coStudyId") long coStudyId) throws Exception {
        String co_email = getCoUserEmail(request);
        return coStudyService.deleteStudy(co_email, coStudyId);
    }

    @ResponseBody
    @PostMapping("/submission/{coStudyId}")
    public CoDevResponse submitCoStudy(HttpServletRequest request,
                                       @PathVariable("coStudyId") long coStudyId,
                                       @RequestBody Map<String, Object> recruitStudy) throws Exception {
        CoRecruitOfStudy coRecruitOfStudy = CoRecruitOfStudy.builder()
                .co_email(getCoUserEmail(request))
                .co_studyId(coStudyId)
                .co_portfolioId(Long.parseLong(recruitStudy.get("co_portfolioId").toString()))
                .co_motivation(recruitStudy.get("co_motivation").toString())
                .build();
        return coStudyRecruitService.submitCoStudy(coRecruitOfStudy);
    }

    @DeleteMapping("/recruitment/{coStudyId}")
    public CoDevResponse cancelRecruitStudy(HttpServletRequest request, @PathVariable("coStudyId") long coStudyId) throws Exception {
        return coStudyRecruitService.cancelRecruitStudy(getCoUserEmail(request), coStudyId);
    }

    @GetMapping("/recruitment/{coStudyId}")
    public CoDevResponse getCoStudyApplicants(HttpServletRequest request, @PathVariable("coStudyId") long coStudyId) throws Exception {
        return coStudyRecruitService.getCoStudyApplicants(getCoUserEmail(request), coStudyId);
    }

    private int getLimitCnt(int pageNum) {
        int limit = SHOW_COUNT;
        for (int i = 0; i <= pageNum; i++) {
            if (i != 0)
                limit += SHOW_COUNT;
        }
        return limit;
    }
}
