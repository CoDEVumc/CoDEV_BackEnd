package com.codevumc.codev_backend.controller.co_qnaboard;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoCommentOfQnaBoard;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoQnaBoard;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileService;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_qnaboard.CoQnaBoardService;
import com.codevumc.codev_backend.service.co_qnaboard.CoQnaBoardServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/codev/QnaBoard")
public class CoQnaBoardController extends JwtController {
    public static final int SHOW_COUNT = 10;
    public static final String BOARD_TYPE = "INFOBOARD";
    private final CoFileService coFileService;
    private final CoQnaBoardService coQnaBoardService;

    public CoQnaBoardController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoFileServiceImpl coFileService, CoQnaBoardServiceImpl coQnaBoardService) {
        super(jwtTokenProvider, jwtService);
        this.coFileService = coFileService;
        this.coQnaBoardService = coQnaBoardService;
    }


    //질문게시판 글쓰기
    @PostMapping(consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse insertQnaBoard(HttpServletRequest request, @RequestPart Map<String, Object> qnaBoard, @RequestPart(required = false) MultipartFile[] files) throws Exception {

        CoQnaBoard coQnaBoard = CoQnaBoard.builder()
                .co_email(getCoUserEmail(request))
                .co_title(qnaBoard.get("co_title").toString())
                .content(qnaBoard.get("content").toString()).build();


        CoDevResponse result = coQnaBoardService.insertCoQnaBoard(coQnaBoard);
        if (files != null) {
            coQnaBoard.setCo_photos(uploadPhotos(files, String.valueOf(coQnaBoard.getCo_qnaId())));
        }
        return result;
    }


    private List<CoPhotos> uploadPhotos(MultipartFile[] files, String co_targetId) {
        List<CoPhotos> coPhotos = Arrays.asList(files)
                .stream()
                .map(file -> coFileService.storeFile(file, co_targetId, BOARD_TYPE))
                .collect(Collectors.toList());

        coQnaBoardService.updateMainImg(coFileService.getCo_MainImg(BOARD_TYPE, co_targetId), Long.parseLong(co_targetId));
        return coPhotos;
    }

    //질문 게시판 댓글 달기
    @PostMapping("/comment/{coQnaId}")
    public CoDevResponse insertCoCommentOfQnaBoard(HttpServletRequest request, @PathVariable("coQnaId") Long coQnaId, @RequestBody Map<String,Object> co_content) throws Exception{

        CoCommentOfQnaBoard coCommentOfQnaBoard = CoCommentOfQnaBoard.builder()
                .co_email(getCoUserEmail(request))
                .co_qnaId(coQnaId)
                .content(co_content.get("co_content").toString()).build();
        System.out.println(coCommentOfQnaBoard.getContent());
        return coQnaBoardService.insertCoCommentOfQnaBoard(coCommentOfQnaBoard);
    }
}
