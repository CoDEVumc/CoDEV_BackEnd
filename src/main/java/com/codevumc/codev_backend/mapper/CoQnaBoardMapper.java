package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.*;
import com.codevumc.codev_backend.domain.CoCommentOfQnaBoard;
import com.codevumc.codev_backend.domain.CoMarkOfQnaBoard;
import com.codevumc.codev_backend.domain.CoQnaBoard;
import com.codevumc.codev_backend.domain.CoReCommentOfQnaBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CoQnaBoardMapper {

    void updateCoMainImg(@Param("co_mainImg") String co_mainImg, @Param("co_qnaId") long co_qnaId);
    void insertCoQnaBoard(CoQnaBoard coQnaBoard);
    void insertCoCommentOfQnaBoard(CoCommentOfQnaBoard coCommentOfQnaBoard);
    Long getCoMarkOfQnaBoardEmail(String co_email, long co_qnaId);
    void insertCoMarkOfQnaBoard(String co_email, long co_qnaId);
    void deleteCoMarkOfQnaBoard(String co_email, long co_qnaId);
    void insertCoReCommentOfQnaBoard(CoReCommentOfQnaBoard coReCommentOfQnaBoard);
    Optional<CoQnaBoard> getQnaBoard(long co_qnaId);
    void updateCoQnaBoard(CoQnaBoard coQnaBoard);
    Optional<CoQnaBoard> getCoQnaBoardByViewer(Map<String, Object> coQnaBoardDto);
    List<CoCommentOfQnaBoard> getComment(long co_qnaId);
    boolean deleteQnaBoard(Map<String, Object> coQnaBoardDto);
    Optional<CoCommentOfQnaBoard> getCoQnaComment(long co_coqb);
    Optional<CoReCommentOfQnaBoard> getCoQnaReComment(long co_rcoqb);
    boolean deleteCoQnaComment(Map<String, Object> coCommentDto);
    boolean deleteCoQnaReComment(Map<String, Object> coReCommentDto);
    List<CoQnaBoard> getCoQnaBoards(Map<String, Object> condition);
    void insertLikeCoQnaBoard(CoLikeOfQnaBoard coLikeOfQnaBoard);
    void deleteLikeCoQnaBoard(CoLikeOfQnaBoard coLikeOfQnaBoard);
    Optional<CoMarkOfQnaBoard> getCoMarkOfQnaBoards(String co_email);
    List<CoMarkOfQnaBoard> getCoMarkOfQnaBoard(String co_email);
}
