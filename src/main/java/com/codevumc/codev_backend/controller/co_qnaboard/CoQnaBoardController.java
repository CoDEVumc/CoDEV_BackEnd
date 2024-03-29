package com.codevumc.codev_backend.controller.co_qnaboard;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.*;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileService;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_qnaboard.CoQnaBoardService;
import com.codevumc.codev_backend.service.co_qnaboard.CoQnaBoardServiceImpl;
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
@RequestMapping("/codev/qnaBoard")
public class CoQnaBoardController extends JwtController {
    public static final int SHOW_COUNT = 10;
    public static final String BOARD_TYPE = "QNABOARD";
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
        return coQnaBoardService.insertCoCommentOfQnaBoard(coCommentOfQnaBoard);
    }

    @PostMapping("/recomments/{coCoqb}")
    public CoDevResponse insertCoReCommentOfQnaBoard(HttpServletRequest request, @PathVariable("coCoqb") Long coCoqb, @RequestBody Map<String, Object> co_content) throws Exception {
        CoReCommentOfQnaBoard coReCommentOfQnaBoard = CoReCommentOfQnaBoard.builder()
                .co_email(getCoUserEmail(request))
                .co_coqb(coCoqb)
                .content(co_content.get("co_content").toString())
                .build();
        return coQnaBoardService.insertCoReCommentOfQnaBoard(coReCommentOfQnaBoard);
    }

    //질문게시판 북마크
    @PatchMapping("/mark/{coQnaId}")
    public CoDevResponse markOfQnaBoard(HttpServletRequest request, @PathVariable("coQnaId") long co_qnaId) throws Exception{
        return coQnaBoardService.changeMark(getCoUserEmail(request),co_qnaId);
    }

    @GetMapping("/{coQnaId}")
    public CoDevResponse getCoQnaBoard(HttpServletRequest request, @PathVariable("coQnaId") Long co_qnaId) throws Exception {
        String co_viewer = getCoUserEmail(request);
        return coQnaBoardService.getCoQnaBoard(co_viewer,co_qnaId);
    }

    @DeleteMapping("/{coQnaId}")
    public CoDevResponse deleteQnaBoard(HttpServletRequest request, @PathVariable("coQnaId") Long co_qnaId) throws Exception {
        return coQnaBoardService.deleteQnaBoard(getCoUserEmail(request), co_qnaId);
    }

    //질문게시판 댓글 삭제
    @DeleteMapping("/out/comment/{coCoqb}")
    public CoDevResponse deleteCoQnaComment(HttpServletRequest request, @PathVariable("coCoqb") Long co_coqb) throws Exception {
        String co_email = getCoUserEmail(request);
        return coQnaBoardService.deleteCoQnaComment(co_email,co_coqb);
    }

    //질문게시판 대댓글 삭제
    @DeleteMapping("/out/recomment/{coRcoqb}")
    public CoDevResponse deleteCoQnaReComment(HttpServletRequest request, @PathVariable("coRcoqb") Long co_rcoqb) throws Exception {
        String co_email = getCoUserEmail(request);
        return coQnaBoardService.deleteCoQnaReComment(co_email,co_rcoqb);
    }

    @GetMapping("/qnaBoards/{page}")
    public CoDevResponse getAllQnaBoards(HttpServletRequest request, @PathVariable("page") int pageNum, @RequestParam("coMyBoard") boolean coMyBoard, @RequestParam("sortingTag") String sortingTag) throws Exception {
        int limit = getLimitCnt(pageNum);
        int offset = limit - SHOW_COUNT;
        return coQnaBoardService.getAllQnaBoards(getCoUserEmail(request), SHOW_COUNT, offset, pageNum, coMyBoard, sortingTag.toUpperCase());
    }

    private int getLimitCnt(int pageNum) {
        int limit = SHOW_COUNT;
        for(int i = 0; i <= pageNum; i++) {
            if(i != 0)
                limit += SHOW_COUNT;
        }

        return limit;
    }

    @PutMapping(value="/update/{coqnaId}",consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse updateQnaBoard(HttpServletRequest request,@PathVariable("coqnaId") long coqnaId ,@RequestPart Map<String, Object> qnaBoard, @RequestPart(required = false) MultipartFile[] files) throws Exception {
        CoQnaBoard coQnaBoard = CoQnaBoard.builder()
                .co_qnaId(coqnaId)
                .co_email(getCoUserEmail(request))
                .co_title(qnaBoard.get("co_title").toString())
                .content(qnaBoard.get("content").toString()).build();

        CoDevResponse result = coQnaBoardService.updateCoQnaBoard(coQnaBoard);
        coFileService.deleteFile(String.valueOf(coQnaBoard.getCo_qnaId()),BOARD_TYPE);
        if (files != null) {
            coQnaBoard.setCo_photos(uploadPhotos(files, String.valueOf(coQnaBoard.getCo_qnaId())));
        }
        return result;
    }

    //질문게시글 좋아요
    @PatchMapping("/like/{coQnaId}")
    public CoDevResponse likeCoQnaBoard(HttpServletRequest request, @PathVariable ("coQnaId") Long co_qnaId, @RequestBody Map<String, Object> likeDto) throws Exception {
        CoLikeOfQnaBoard coLikeOfQnaBoard = CoLikeOfQnaBoard.builder()
                .co_qnaId(co_qnaId)
                .co_email(getCoUserEmail(request))
                .co_like(Boolean.parseBoolean(likeDto.get("co_like").toString())).build();
        return coQnaBoardService.likeCoQnaBoard(coLikeOfQnaBoard);
    }

}
