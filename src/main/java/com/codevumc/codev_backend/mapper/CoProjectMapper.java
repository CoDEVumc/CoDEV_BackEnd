package com.codevumc.codev_backend.mapper;


import com.codevumc.codev_backend.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    void deleteCoHeartOfProject(@Param("co_email") String co_email,@Param("co_projectId") Long co_projectId);
    Optional<CoProject> getCoProject(@Param("co_projectId") long co_projectId);
    List<CoPartOfProject> getCoPartList(long co_projectId);
    List<CoLanguage> getCoLanguageList(long co_projectId);
    long getCoHeartCount(long co_projectId);
    boolean deleteCoProject(Map<String, Object> coProjectDto);
    Long getCoHeartOfProjectEmail(@Param("co_projectId") Long co_projectId, @Param("co_email") String co_email);
    void insertCoRecruitOfProject(CoRecruitOfProject coRecruitOfProject);
    boolean cancelCoRecruitOfProject(Map<String, Object> recruitDto);
    boolean getCoProjectProcess( long co_projectId, String co_process);
    void updateCoProjectdeadLine(CoProject coProject);
    void closeCoProjectDeadLine(Map<String, Object> condition);
    void approveCoProjectMember(String co_email, Long co_projectId);
    List<CoApplicantInfo> getCoApplicantsInfo(Map<String, Object> coProjectDto);
    int getTempsavedApplicantsCount(long co_projectId);
    List<CoApplicantCount> getCoApplicantsCount(long co_projectId);
    Optional<CoProject> getCoProjectByViewer(Map<String, Object> coProjectDto);
    Optional<CoPortfolioOfApplicant> getCoPortfolioOfApplicant(Map<String, Object> coPortfolioDro);
}
