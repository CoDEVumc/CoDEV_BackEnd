package com.codevumc.codev_backend.service.co_infoboard;


import com.codevumc.codev_backend.domain.*;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public CoDevResponse insertCoReCommentOfInfoBoard(CoReCommentOfInfoBoard coReCommentOfInfoBoard) {
        try {
            this.coInfoBoardMapper.insertCoReCommentOfInfoBoard(coReCommentOfInfoBoard);
            return setResponse(200, "message", "정보게시판 대댓글이 작성되었습니다.");
        } catch (Exception e) {
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

    @Override
    public CoDevResponse getCoInfoBoard(String co_viewer, long co_infoId) {
        try {
            Map<String, Object> coInfoBoardDto = new HashMap<>();
            List<CoPhotos> coPhotosList = coPhotosMapper.findByCoTargetId(String.valueOf(co_infoId), "INFOBOARD");
            coInfoBoardDto.put("co_viewer", co_viewer);
            coInfoBoardDto.put("co_infoId", co_infoId);
            Optional<CoInfoBoard> coInfoBoard = coInfoBoardMapper.getCoInfoBoardByViewer(coInfoBoardDto);
            if(coInfoBoard.isPresent()) {
                coInfoBoard.get().setCo_viewer(co_viewer);
                coInfoBoard.get().setCo_photos(coPhotosList);
                coInfoBoard.get().setCo_comment(coInfoBoardMapper.getComment(co_infoId));
            }
                return setResponse(200, "Complete", coInfoBoard);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse deleteInfoBoard(String co_email, Long co_infoId) {
        Map<String, Object> coInfoBoardDto = new HashMap<>();
        coInfoBoardDto.put("co_email", co_email);
        coInfoBoardDto.put("co_infoId", co_infoId);
        try {
            return coInfoBoardMapper.deleteInfoBoard(coInfoBoardDto) ? setResponse(200, "Complete", "삭제되었습니다.") : setResponse(403, "Forbidden", "수정 권한이 없습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
