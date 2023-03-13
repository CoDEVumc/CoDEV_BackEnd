package com.codevumc.codev_backend.service.co_infoboard;

import com.codevumc.codev_backend.domain.CoCommentOfInfoBoard;
import com.codevumc.codev_backend.domain.CoInfoBoard;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoInfoBoardService {

    void updateMainImg(String co_mainImg, long co_infoId);
    CoDevResponse insertCoInfoBoard(CoInfoBoard coInfoBoard);
    CoDevResponse insertCoCommentOfInfoBoard(CoCommentOfInfoBoard coCommentOfInfoBoard);
    CoDevResponse changeMark(String co_email, long co_infoId);
}
