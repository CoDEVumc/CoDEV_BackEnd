package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CoProjectMapper {
    void insertCoProject(CoProject coProject);
    void insertCoPartOfProject(Map<String, Object> coPartsDto);
    void insertCoLanguageOfProject(@Param("co_projectId") long co_projectId, @Param("co_languageId") long co_languageId);
    Optional<CoProject> getCoProject(@Param("co_projectId") long co_projectId);

    List<CoPart> getCoPartList(long co_projectId);

    List<CoLanguage> getCoLanguageList(long co_projectId);

    long getCoHeartCount(long co_projectId);
}
