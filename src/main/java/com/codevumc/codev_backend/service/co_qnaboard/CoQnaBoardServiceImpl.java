package com.codevumc.codev_backend.service.co_qnaboard;

import com.codevumc.codev_backend.domain.CoCommentOfQnaBoard;
import com.codevumc.codev_backend.domain.CoInfoBoard;
import com.codevumc.codev_backend.domain.CoQnaBoard;
import com.codevumc.codev_backend.domain.CoReCommentOfQnaBoard;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.mapper.CoQnaBoardMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


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
    public CoDevResponse insertCoReCommentOfQnaBoard(CoReCommentOfQnaBoard coReCommentOfQnaBoard) {
        try {
            this.coQnaBoardMapper.insertCoReCommentOfQnaBoard(coReCommentOfQnaBoard);
            return setResponse(200, "message", "질문게시판 대댓글이 작성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }
}
