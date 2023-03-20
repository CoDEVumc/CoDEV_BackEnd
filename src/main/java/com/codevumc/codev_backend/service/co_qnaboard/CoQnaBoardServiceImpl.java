package com.codevumc.codev_backend.service.co_qnaboard;

import com.codevumc.codev_backend.domain.*;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.mapper.CoQnaBoardMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@AllArgsConstructor
@Service
public class CoQnaBoardServiceImpl extends ResponseService implements CoQnaBoardService{

    private final CoQnaBoardMapper coQnaBoardMapper;
    private final CoPhotosMapper coPhotosMapper;



    @Override
    public CoDevResponse insertCoQnaBoard(CoQnaBoard coQnaBoard) {
        try {
            this.coQnaBoardMapper.insertCoQnaBoard(coQnaBoard);

            return setResponse(200, "message", "질문게시판 글이 작성/수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }

    }

    @Override
    public void updateMainImg(String co_mainImg, long co_qnaId) {
        coQnaBoardMapper.updateCoMainImg(co_mainImg, co_qnaId);
    }

    @Override
    public CoDevResponse insertCoCommentOfQnaBoard(CoCommentOfQnaBoard coCommentOfQnaBoard) {
        try{
            this.coQnaBoardMapper.insertCoCommentOfQnaBoard(coCommentOfQnaBoard);
            return setResponse(200,"message", "질문게시판 댓글이 작성되었습니다.");
        }catch(Exception e){
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse changeMark(String co_email, long co_qnaId) {
        try{
            if(coQnaBoardMapper.getCoMarkOfQnaBoardEmail(co_email,co_qnaId) == null){
                this.coQnaBoardMapper.insertCoMarkOfQnaBoard(co_email,co_qnaId);
                return setResponse(200,"message","북마크 등록이 완료되었습니다.");
            }
            else{
                this.coQnaBoardMapper.deleteCoMarkOfQnaBoard(co_email,co_qnaId);
                return setResponse(200,"message","북마크 등록이 취소되었습니다.");
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse insertCoReCommentOfQnaBoard(CoReCommentOfQnaBoard coReCommentOfQnaBoard) {
        try {
            this.coQnaBoardMapper.insertCoReCommentOfQnaBoard(coReCommentOfQnaBoard);
            return setResponse(200, "message", "질문게시판 대댓글이 작성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse getCoQnaBoard(String co_viewer, long co_qnaId) {
        try {
            Map<String, Object> coQnaBoardDto = new HashMap<>();
            List<CoPhotos> coPhotosList = coPhotosMapper.findByCoTargetId(String.valueOf(co_qnaId), "QNABOARD");
            coQnaBoardDto.put("co_viewer", co_viewer);
            coQnaBoardDto.put("co_qnaId", co_qnaId);
            Optional<CoQnaBoard> coQnaBoard = coQnaBoardMapper.getCoQnaBoardByViewer(coQnaBoardDto);
            if(coQnaBoard.isPresent()) {
                coQnaBoard.get().setCo_viewer(co_viewer);
                coQnaBoard.get().setCo_photos(coPhotosList);
                coQnaBoard.get().setCo_comment(coQnaBoardMapper.getComment(co_qnaId));
            }
                return setResponse(200, "Complete", coQnaBoard);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse deleteQnaBoard(String co_email, Long co_qnaId) {
        Map<String, Object> coQnaBoardDto = new HashMap<>();
        coQnaBoardDto.put("co_email", co_email);
        coQnaBoardDto.put("co_qnaId", co_qnaId);
        try {
            return coQnaBoardMapper.deleteQnaBoard(coQnaBoardDto) ? setResponse(200, "Complete", "삭제되었습니다.") : setResponse(403, "Forbidden", "수정 권한이 없습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
