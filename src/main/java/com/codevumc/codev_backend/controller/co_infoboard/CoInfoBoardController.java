package com.codevumc.codev_backend.controller.co_infoboard;


import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoInfoBoard;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileService;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_infoboard.CoInfoBoardService;
import com.codevumc.codev_backend.service.co_infoboard.CoInfoBoardServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
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
@RequestMapping("/codev/infoBoard")
public class CoInfoBoardController extends JwtController {
    public static final int SHOW_COUNT = 10;
    public static final String BOARD_TYPE = "QNABOARD";
    private final CoFileService coFileService;
    private final CoInfoBoardService coInfoBoardService;

    public CoInfoBoardController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoFileServiceImpl coFileService, CoInfoBoardServiceImpl coInfoBoardService) {
        super(jwtTokenProvider, jwtService);
        this.coFileService = coFileService;
        this.coInfoBoardService = coInfoBoardService;
    }

    //정보게시판 글쓰기
    @PostMapping(consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse insertInfoBoard(HttpServletRequest request, @RequestPart Map<String, Object> InfoBoard, @RequestPart(required = false) MultipartFile[] files) throws Exception {

        CoInfoBoard coInfoBoard = CoInfoBoard.builder()
                .co_email(getCoUserEmail(request))
                .co_title(InfoBoard.get("co_title").toString())
                .content(InfoBoard.get("content").toString()).build();


        CoDevResponse result = coInfoBoardService.insertCoInfoBoard(coInfoBoard);
        if (files != null) {
            coInfoBoard.setCo_photos(uploadPhotos(files, String.valueOf(coInfoBoard.getCo_infoId())));
        }
        return result;
    }


    private List<CoPhotos> uploadPhotos(MultipartFile[] files, String co_targetId) {
        List<CoPhotos> coPhotos = Arrays.asList(files)
                .stream()
                .map(file -> coFileService.storeFile(file, co_targetId, BOARD_TYPE))
                .collect(Collectors.toList());

        coInfoBoardService.updateMainImg(coFileService.getCo_MainImg(BOARD_TYPE, co_targetId), Long.parseLong(co_targetId));
        return coPhotos;
    }
}
