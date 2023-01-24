package com.codevumc.codev_backend.mapper;


import com.codevumc.codev_backend.domain.CoHeartOfProject;
import com.codevumc.codev_backend.domain.CoProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codevumc.codev_backend.domain.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CoProjectMapper {
    void insertCoProject(CoProject coProject);
    void updateCoProject(CoProject coProject);
    void insertCoPartOfProject(Map<String, Object> coPartsDto);
    void deleteCoPartOfProject(@Param("co_projectId") long co_projectId);
    void insertCoLanguageOfProject(@Param("co_projectId") long co_projectId, @Param("co_languageId") long co_languageId);
    void deleteCoLanguageOfProject(@Param("co_projectId") long co_projectId);
    void updateCoMainImg(@Param("co_mainImg") String co_mainImg, @Param("co_projectId") long co_projectId);
    List<CoProject> getCoProjects(Map<String, Object> condition);
    void insertCoHeartOfProject(String co_email, Long co_projectId);
    Optional<CoHeartOfProject> getCoHeartOfProject(Long co_projectId);
    void deleteCoHeartOfProject(String co_email, Long co_projectId);
    Optional<CoProject> getCoProject(@Param("co_projectId") long co_projectId);
    List<CoPart> getCoPartList(long co_projectId);
    List<CoLanguage> getCoLanguageList(long co_projectId);
    long getCoHeartCount(long co_projectId);
    boolean deleteCoProject(Map<String, Object> coProjectDto);
    boolean getCoRecruitStatus(String co_viewer, long co_projectId);
    String getCoHeartOfProjectEmail(Long co_projectId);
}
