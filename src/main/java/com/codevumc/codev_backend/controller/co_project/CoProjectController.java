package com.codevumc.codev_backend.controller.co_project;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPhotoOfProject;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_project.CoProjectServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import lombok.AllArgsConstructor;
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
        CoProject coProject = coProjectService.getCoProject(this.coProjectService.insertProject(
                                                            CoProject.builder()
                                                                    .co_email(getCoUserEmail(request))
                                                                    .co_title(project.get("co_title"))
                                                                    .co_content(project.get("co_content"))
                                                                    .co_limit(Integer.parseInt(project.get("co_limit"))).build()));

        System.out.println("pId = " + coProject.getCo_projectId());
        if(files != null) {
            List<CoPhotoOfProject> coPhotoOfProjects = Arrays.asList(files)
                    .stream()
                    .map(file -> coFileService.storeFile(file, coProject.getCo_projectId()))
                    .collect(Collectors.toList());
            coProject.setCoPhotoOfProjects(coPhotoOfProjects);
        }else {
            //TO-DO
            //이미지 첨부 안했을 시 랜덤으로 사진 넣기
        }
        return coProjectService.getCoProject(coProject);
    }
}
