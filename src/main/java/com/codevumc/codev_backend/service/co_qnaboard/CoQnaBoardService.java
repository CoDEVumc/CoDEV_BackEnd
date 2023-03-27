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
    CoDevResponse changeMark(String co_email, long co_qnaId);
    CoDevResponse getCoQnaBoard(String co_viewer, long co_qnaId);
    CoDevResponse updateCoQnaBoard(CoQnaBoard coQnaBoard);
    CoDevResponse deleteQnaBoard(String co_email, Long co_qnaId);
    CoDevResponse getAllQnaBoards(String co_email, int showCount, int offset, int pageNum, boolean co_myBoard, String sortingTag);
    CoDevResponse deleteCoQnaComment(String co_email, long co_coqb);
    CoDevResponse deleteCoQnaReComment(String co_email, long co_rcoqb);
}