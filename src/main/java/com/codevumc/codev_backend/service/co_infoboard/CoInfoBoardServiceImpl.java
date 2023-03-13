package com.codevumc.codev_backend.service.co_infoboard;


import com.codevumc.codev_backend.domain.CoCommentOfInfoBoard;
import com.codevumc.codev_backend.domain.CoCommentOfQnaBoard;
import com.codevumc.codev_backend.domain.CoInfoBoard;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoInfoBoardMapper;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class CoInfoBoardServiceImpl extends ResponseService implements CoInfoBoardService{
    private final CoInfoBoardMapper coInfoBoardMapper;
    private final CoPhotosMapper coPhotosMapper;


    @Override
    public CoDevResponse insertCoInfoBoard(CoInfoBoard coInfoBoard) {
        try {
            this.coInfoBoardMapper.insertCoInfoBoard(coInfoBoard);

            return setResponse(200, "message", "정보게시판 글이 작성/수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }

    }

    @Override
    public CoDevResponse insertCoCommentOfInfoBoard(CoCommentOfInfoBoard coCommentOfInfoBoard) {
        try{
            this.coInfoBoardMapper.insertCoCommentOfQnaBoard(coCommentOfInfoBoard);
            return setResponse(200,"message", "정보게시판 댓글이 작성되었습니다.");
        }catch(Exception e){
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse changeMark(String co_email, long co_infoId) {
        try{
            if(coInfoBoardMapper.getCoMarkOfInfoBoardEmail(co_email,co_infoId) == null){
                this.coInfoBoardMapper.insertCoMarkOfInfoBoard(co_email,co_infoId);
                return setResponse(200,"message","북마크 등록이 완료되었습니다.");
            }
            else{
                this.coInfoBoardMapper.deleteCoMarkOfInfoBoard(co_email,co_infoId);
                return setResponse(200,"message","북마크 등록이 취소되었습니다.");
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }


    @Override
    public void updateMainImg(String co_mainImg, long co_infoId) {
        coInfoBoardMapper.updateCoMainImg(co_mainImg, co_infoId);
    }


}
