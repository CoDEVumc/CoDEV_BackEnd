package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoCommentOfQnaBoard;
import com.codevumc.codev_backend.domain.CoQnaBoard;
import com.codevumc.codev_backend.domain.CoReCommentOfQnaBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface CoQnaBoardMapper {

    void updateCoMainImg(@Param("co_mainImg") String co_mainImg, @Param("co_qnaId") long co_qnaId);
    void insertCoQnaBoard(CoQnaBoard coQnaBoard);
    void insertCoCommentOfQnaBoard(CoCommentOfQnaBoard coCommentOfQnaBoard);
    void insertCoReCommentOfQnaBoard(CoReCommentOfQnaBoard coReCommentOfQnaBoard);
    Optional<CoQnaBoard> getQnaBoard(long co_qnaId);
    void updateCoQnaBoard(CoQnaBoard coQnaBoard);
}
