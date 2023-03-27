package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CoInfoBoardMapper {

    void updateCoMainImg(@Param("co_mainImg") String co_mainImg, @Param("co_infoId") long co_infoId);
    void insertCoInfoBoard(CoInfoBoard coInfoBoard);
    void insertCoCommentOfInfoBoard(CoCommentOfInfoBoard coCommentOfInfoBoard);
    void insertCoReCommentOfInfoBoard(CoReCommentOfInfoBoard coReCommentOfInfoBoard);
    Long getCoMarkOfInfoBoardEmail(@Param("co_email") String co_email, @Param("co_infoId") long co_infoId);
    void insertCoMarkOfInfoBoard(String co_email, long co_infoId);
    void deleteCoMarkOfInfoBoard(String co_email, long co_infoId);
    List<CoInfoBoard> getCoInfoBoards(Map<String, Object> condition);
    Optional<CoInfoBoard> getCoInfoBoardByViewer(Map<String, Object> coInfoBoardDto);
    List<CoCommentOfInfoBoard> getComment(long co_infoId);
    Optional<CoInfoBoard> getInfoBoard(long co_infoId);
    void updateCoInfoBoard(CoInfoBoard coInfoBoard);
    boolean deleteInfoBoard(Map<String, Object> coInfoBoardDto);
    Optional<CoCommentOfInfoBoard> getCoInfoComment(long co_coib);
    Optional<CoReCommentOfInfoBoard> getCoInfoReComment(long co_rcoib);
    boolean deleteCoInfoComment(Map<String, Object> coCommentDto);
    boolean deleteCoInfoReComment(Map<String, Object> coReCommentDto);
    List<CoMarkOfInfoBoard> getCoMarkOfInfoBoard(String co_email);
    Optional<CoMarkOfInfoBoard> getCoMarkOfInfoBoards(String co_email);
}