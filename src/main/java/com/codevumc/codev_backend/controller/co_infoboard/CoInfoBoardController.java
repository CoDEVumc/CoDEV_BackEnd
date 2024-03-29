package com.codevumc.codev_backend.controller.co_infoboard;


import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.*;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileService;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_infoboard.CoInfoBoardService;
import com.codevumc.codev_backend.service.co_infoboard.CoInfoBoardServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
    public static final String BOARD_TYPE = "INFOBOARD";
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

    //정보게시판 댓글 작성
    @PostMapping("comment/{coInfoId}")
    public CoDevResponse insertCoCommentOfInfoBoard(HttpServletRequest request, @PathVariable("coInfoId") Long coInfoId, @RequestBody Map<String,Object> co_content) throws Exception{
        CoCommentOfInfoBoard coCommentOfInfoBoard = CoCommentOfInfoBoard.builder()
                .co_email(getCoUserEmail(request))
                .co_infoId(coInfoId)
                .content(co_content.get("co_content").toString()).build();
        return coInfoBoardService.insertCoCommentOfInfoBoard(coCommentOfInfoBoard);
    }

    @PostMapping("/recomments/{coCoib}")
    public CoDevResponse insertCoReCommentOfInfoBoard(HttpServletRequest request, @PathVariable("coCoib") Long co_coib, @RequestBody Map<String, Object> co_content) throws Exception {
        CoReCommentOfInfoBoard coReCommentOfInfoBoard = CoReCommentOfInfoBoard.builder()
                .co_email(getCoUserEmail(request))
                .co_coib(co_coib)
                .content(co_content.get("co_content").toString())
                .build();
        return coInfoBoardService.insertCoReCommentOfInfoBoard(coReCommentOfInfoBoard);
    }
    //정보게시판 북마크
    @PatchMapping("/mark/{coInfoId}")
    public CoDevResponse markOfInfoBoard(HttpServletRequest request, @PathVariable("coInfoId") long co_infoId) throws Exception{
        return coInfoBoardService.changeMark(getCoUserEmail(request),co_infoId);
    }


    @GetMapping("/infoBoards/{page}")
    public CoDevResponse getAllInfoBoards(HttpServletRequest request, @PathVariable("page") int pageNum, @RequestParam("coMyBoard") boolean coMyBoard, @RequestParam("sortingTag") String sortingTag) throws Exception {
        int limit = getLimitCnt(pageNum);
        int offset = limit - SHOW_COUNT;
        return coInfoBoardService.getAllInfoBoards(getCoUserEmail(request), SHOW_COUNT, offset, pageNum, coMyBoard, sortingTag);
    }

    private int getLimitCnt(int pageNum) {
        int limit = SHOW_COUNT;
        for(int i = 0; i <= pageNum; i++) {
            if(i != 0)
                limit += SHOW_COUNT;
        }

        return limit;
    }
    @PutMapping(value="/update/{coInfoId}",consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse updateCoInfoBoard(HttpServletRequest request,@PathVariable("coInfoId") long coInfoId, @RequestPart Map<String, Object> infoBoard, @RequestPart(required = false) MultipartFile[] files) throws Exception {

        CoInfoBoard coInfoBoard = CoInfoBoard.builder()
                .co_infoId(coInfoId)
                .co_email(getCoUserEmail(request))
                .co_title(infoBoard.get("co_title").toString())
                .content(infoBoard.get("content").toString()).build();

        CoDevResponse result = coInfoBoardService.updateCoInfoBoard(coInfoBoard);
        coFileService.deleteFile(String.valueOf(coInfoBoard.getCo_infoId()),BOARD_TYPE);
        if (files != null) {
            coInfoBoard.setCo_photos(uploadPhotos(files, String.valueOf(coInfoBoard.getCo_infoId())));
        }
        return result;
    }
    @GetMapping("/{coInfoId}")
    public CoDevResponse getCoInfoBoard(HttpServletRequest request, @PathVariable("coInfoId") Long co_infoId) throws Exception {
        String co_viewer = getCoUserEmail(request);
        return coInfoBoardService.getCoInfoBoard(co_viewer,co_infoId);
    }

    @DeleteMapping("/{coInfoId}")
    public CoDevResponse deleteInfoBoard(HttpServletRequest request, @PathVariable("coInfoId") Long co_infoId) throws Exception {
        return coInfoBoardService.deleteInfoBoard(getCoUserEmail(request), co_infoId);
    }

    //정보게시판 댓글 삭제
    @DeleteMapping("/out/comment/{coCoib}")
    public CoDevResponse deleteCoInfoComment(HttpServletRequest request, @PathVariable("coCoib") Long co_coib) throws Exception {
        String co_email = getCoUserEmail(request);
        return coInfoBoardService.deleteCoInfoComment(co_email,co_coib);
    }

    //정보게시판 대댓글 삭제
    @DeleteMapping("/out/recomment/{coRcoib}")
    public CoDevResponse deleteCoInfoReComment(HttpServletRequest request, @PathVariable("coRcoib") Long co_rcoib) throws Exception {
        String co_email = getCoUserEmail(request);
        return coInfoBoardService.deleteCoInfoReComment(co_email,co_rcoib);
    }

    //정보게시판 좋아요
    @PatchMapping("/like/{coInfoId}")
    public CoDevResponse likeCoInfoBoard(HttpServletRequest request, @PathVariable("coInfoId") Long co_infoId, @RequestBody Map<String, Object> likeDto) throws Exception {
        CoLikeOfInfoBoard coLikeOfInfoBoard = CoLikeOfInfoBoard.builder()
                .co_infoId(co_infoId)
                .co_email(getCoUserEmail(request))
                .co_like(Boolean.parseBoolean(likeDto.get("co_like").toString())).build();
        return coInfoBoardService.likeCoInfoBoard(coLikeOfInfoBoard);
    }

}
