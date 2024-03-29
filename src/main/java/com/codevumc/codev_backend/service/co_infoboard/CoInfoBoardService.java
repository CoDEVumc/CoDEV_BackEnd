package com.codevumc.codev_backend.service.co_infoboard;

import com.codevumc.codev_backend.domain.CoCommentOfInfoBoard;
import com.codevumc.codev_backend.domain.CoInfoBoard;
import com.codevumc.codev_backend.domain.CoLikeOfInfoBoard;
import com.codevumc.codev_backend.domain.CoReCommentOfInfoBoard;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoInfoBoardService {

    void updateMainImg(String co_mainImg, long co_infoId);
    CoDevResponse insertCoInfoBoard(CoInfoBoard coInfoBoard);
    CoDevResponse insertCoCommentOfInfoBoard(CoCommentOfInfoBoard coCommentOfInfoBoard);
    CoDevResponse changeMark(String co_email, long co_infoId);
    CoDevResponse insertCoReCommentOfInfoBoard(CoReCommentOfInfoBoard coReCommentOfInfoBoard);
    CoDevResponse getAllInfoBoards(String co_email, int showCount, int offset, int pageNum, boolean coMyBoard, String sortingTag);
    CoDevResponse getCoInfoBoard(String co_viewer, long co_infoId);
    CoDevResponse updateCoInfoBoard(CoInfoBoard coInfoBoard);
    CoDevResponse deleteInfoBoard(String co_email, Long co_infoId);
    CoDevResponse deleteCoInfoComment(String co_email, long co_coib);
    CoDevResponse deleteCoInfoReComment(String co_email, long co_rcoib);
    CoDevResponse likeCoInfoBoard(CoLikeOfInfoBoard coLikeOfInfoBoard);
}