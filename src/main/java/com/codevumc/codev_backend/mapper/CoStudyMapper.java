package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoStudy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CoStudyMapper {
    void insertCoStudy(CoStudy coStudy);
    void insertCoLanguageOfStudy(long co_studyId, long co_languageId);
    void updateCoMainImg(@Param("co_mainImg") String co_mainImg, @Param("co_studyId") long co_studyId);
}
