package com.codevumc.codev_backend.controller.co_study;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_study.CoStudyServiceImpl;
import com.codevumc.codev_backend.service.co_studyheart.CoStudyHeartServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping(value = "/studies")
    public CoDevResponse getAllProjects(HttpServletRequest request, @RequestParam("coLocationTag") String coLocationTag, @RequestParam("coPartTag") String coPartTag, @RequestParam("coKeyword") String coKeyword, @RequestParam("coProcessTag") String coProcessTag) throws Exception {
        return coStudyService.getCoStudies(getCoUserEmail(request), coLocationTag, coPartTag, coKeyword, coProcessTag);
    }

    @PatchMapping("/heart/{coStudyId}")
    public CoDevResponse heartOfStudyUpdate(HttpServletRequest request, @PathVariable("coStudyId") Long co_studyId) throws Exception{
        String co_email = getCoUserEmail(request);
        return coStudyHeartService.insertHeart(co_email,co_studyId);
    }

    @PatchMapping("/nonheart/{coStudyId}")
    public CoDevResponse heartOfStudyDelete(HttpServletRequest request, @PathVariable("coStudyId") Long co_studyId) throws Exception{
        String co_email = getCoUserEmail(request);
        return coStudyHeartService.deleteHeart(co_email, co_studyId);
    }

}
