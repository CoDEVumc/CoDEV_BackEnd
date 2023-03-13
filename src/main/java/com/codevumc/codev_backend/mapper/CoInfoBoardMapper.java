package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoCommentOfInfoBoard;
import com.codevumc.codev_backend.domain.CoInfoBoard;
import com.codevumc.codev_backend.domain.CoQnaBoard;
import com.codevumc.codev_backend.service.co_infoboard.CoInfoBoardService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CoInfoBoardMapper {

    void updateCoMainImg(@Param("co_mainImg") String co_mainImg, @Param("co_infoId") long co_infoId);
    void insertCoInfoBoard(CoInfoBoard coInfoBoard);
    void insertCoCommentOfQnaBoard(CoCommentOfInfoBoard coCommentOfInfoBoard);

    Long getCoMarkOfInfoBoardEmail(@Param("co_email") String co_email, @Param("co_infoId") long co_infoId);
    void insertCoMarkOfInfoBoard(String co_email, long co_infoId);
    void deleteCoMarkOfInfoBoard(String co_email, long co_infoId);
}
