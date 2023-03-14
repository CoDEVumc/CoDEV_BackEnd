package com.codevumc.codev_backend.service.co_qnaboard;


import com.codevumc.codev_backend.domain.CoCommentOfQnaBoard;
import com.codevumc.codev_backend.domain.CoQnaBoard;
import com.codevumc.codev_backend.domain.CoReCommentOfQnaBoard;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoQnaBoardService {
    void updateMainImg(String co_mainImg, long co_qnaId);
    CoDevResponse insertCoQnaBoard(CoQnaBoard coQnaBoard);
    CoDevResponse insertCoCommentOfQnaBoard(CoCommentOfQnaBoard coCommentOfQnaBoard);
    CoDevResponse insertCoReCommentOfQnaBoard(CoReCommentOfQnaBoard coReCommentOfQnaBoard);
}
