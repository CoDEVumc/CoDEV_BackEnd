package com.codevumc.codev_backend.service.co_infoboard;


import com.codevumc.codev_backend.domain.*;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoInfoBoardMapper;
import com.codevumc.codev_backend.mapper.CoPhotosMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
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
            this.coInfoBoardMapper.insertCoCommentOfInfoBoard(coCommentOfInfoBoard);
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
    public CoDevResponse updateCoInfoBoard(CoInfoBoard coInfoBoard) {
        try {
            Optional<CoInfoBoard> coInfoBoardOptional = coInfoBoardMapper.getInfoBoard(coInfoBoard.getCo_infoId());
            if(coInfoBoardOptional.isPresent()){
                if(!coInfoBoard.getCo_email().equals(coInfoBoardOptional.get().getCo_email()))
                    return setResponse(403, "Forbidden", "수정 권한이 없습니다.");
            }
            this.coInfoBoardMapper.updateCoInfoBoard(coInfoBoard);

            return setResponse(200, "message", "정보게시판 글이 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    @Override
    public CoDevResponse getAllInfoBoards(String co_email, int limit, int offset, int pageNum, boolean coMyBoard, String sortingTag) {
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("co_email", co_email);
            condition.put("coMyBoard", coMyBoard ? co_email : null);
            condition.put("limit", limit);
            condition.put("offset", offset);
            condition.put("sortingTag", sortingTag);
            List<CoInfoBoard> coInfoBoards = this.coInfoBoardMapper.getCoInfoBoards(condition);
            setResponse(200, "success", coInfoBoards);
            return addResponse("co_page", pageNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateMainImg(String co_mainImg, long co_infoId) {
        coInfoBoardMapper.updateCoMainImg(co_mainImg, co_infoId);
    }

    private String setting(String keyword) {
        return keyword == null ? null : "%" + keyword + "%";
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
        try {
            Map<String, Object> coInfoBoardDto = new HashMap<>();
            coInfoBoardDto.put("co_email", co_email);
            coInfoBoardDto.put("co_infoId", co_infoId);
            Optional<CoInfoBoard> coInfoBoard = coInfoBoardMapper.getInfoBoard(co_infoId);
            if (coInfoBoard.isPresent()){
                return coInfoBoardMapper.deleteInfoBoard(coInfoBoardDto) ? setResponse(200, "Complete", "삭제되었습니다.") : setResponse(403, "Forbidden", "수정 권한이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse deleteCoInfoComment(String co_email, long co_coib) {
        try {
            Map<String, Object> coCommentDto = new HashMap<>();
            coCommentDto.put("co_email", co_email);
            coCommentDto.put("co_coib", co_coib);
            Optional<CoCommentOfInfoBoard> coCommentOfInfoBoard = coInfoBoardMapper.getCoInfoComment(co_coib);
            if(coCommentOfInfoBoard.isPresent()) {
                return coInfoBoardMapper.deleteCoInfoComment(coCommentDto) ? setResponse(200,"Complete", "댓글이 삭제되었습니다.") : setResponse(403,"Forbidden", "삭제 권한이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse deleteCoInfoReComment(String co_email, long co_rcoib) {
        try {
            Map<String, Object> coReCommentDto = new HashMap<>();
            coReCommentDto.put("co_email", co_email);
            coReCommentDto.put("co_rcoib", co_rcoib);
            Optional<CoReCommentOfInfoBoard> coReCommentOfInfoBoard = coInfoBoardMapper.getCoInfoReComment(co_rcoib);
            if(coReCommentOfInfoBoard.isPresent()) {
                return coInfoBoardMapper.deleteCoInfoReComment(coReCommentDto) ? setResponse(200,"Complete", "댓글이 삭제되었습니다.") : setResponse(403, "Forbidden", "삭제 권한이 없습니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public CoDevResponse likeCoInfoBoard(CoLikeOfInfoBoard coLikeOfInfoBoard) {
        try {
            if(coLikeOfInfoBoard.isCo_like()==false) {
                this.coInfoBoardMapper.insertLikeCoInfoBoard(coLikeOfInfoBoard);
                return setResponse(200, "message", "좋아요를 하셨습니다.");
            } else {
                this.coInfoBoardMapper.deleteLikeCoInfoBoard(coLikeOfInfoBoard);
                return setResponse(200, "message", "좋아요를 취소하였습니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }
}
