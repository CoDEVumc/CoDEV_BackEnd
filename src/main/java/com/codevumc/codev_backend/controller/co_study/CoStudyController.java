package com.codevumc.codev_backend.controller.co_study;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_study.CoStudyServiceImpl;
import com.codevumc.codev_backend.service.co_studyheart.CoStudyHeartServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/codev/study")
public class CoStudyController extends JwtController {
    private final CoFileServiceImpl coFileService;
    private final CoStudyServiceImpl coStudyService;
    private final CoStudyHeartServiceImpl coStudyHeartService;

    public CoStudyController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoFileServiceImpl coFileService, CoStudyServiceImpl coStudyService, CoStudyHeartServiceImpl coStudyHeartService) {
        super(jwtTokenProvider, jwtService);
        this.coFileService = coFileService;
        this.coStudyService = coStudyService;
        this.coStudyHeartService = coStudyHeartService;
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
                .co_deadLine((study.get("co_deadLine").toString())).build();
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String co_languageListByString = gson.toJson(study.get("co_languages"));
        JSONArray co_languageList = (JSONArray) parser.parse(co_languageListByString);
        this.coStudyService.insertStudy(coStudy, co_languageList);
        if (files != null) {
            List<CoPhotos> coPhotos = Arrays.asList(files)
                    .stream()
                    .map(file -> coFileService.storeFile(file, coStudy.getCo_studyId(), "STUDY"))
                    .collect(Collectors.toList());
            coStudy.setCo_photos(coPhotos);
            coStudyService.updateMainImg(coFileService.getCo_MainImg("STUDY", coStudy.getCo_studyId()), coStudy.getCo_studyId());
        }
        return null;
    }

}
