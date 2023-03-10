package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CoStudyMapper {
    void insertCoStudy(CoStudy coStudy);
    void updateCoStudy(CoStudy coStudy);
    void insertCoLanguageOfStudy(long co_studyId, long co_languageId);
    void deleteCoLanguageOfStudy(long co_studyId);
    void deleteCoPartOfStudy(long co_studyId);
    void updateCoMainImg(@Param("co_mainImg") String co_mainImg, @Param("co_studyId") long co_studyId);
    void insertCoPartOfStudy(Map<String, Object> coPartDto);
    Optional<CoStudy> getCoStudy(long co_studyId);
    List<CoLanguage> getCoLanguageList(long co_studyId);
    void insertCoHeartOfStudy(String co_email, Long co_studyId);
    void deleteCoHeartOfStudy(@Param("co_email") String co_email, @Param("co_studyId") Long co_studyId);
    List<CoStudy> getCoStudies(Map<String, Object> condition);
    boolean deleteCoStudy(Map<String, Object> studyDto);
    void insertCoRecruitOfStudy(CoRecruitOfStudy coRecruitOfStudy);
    boolean deleteRecruitOfStudy(CoRecruitOfStudy coRecruitOfStudy);
    Long getCoHeartOfStudyEmail(@Param("co_studyId") Long co_studyId, @Param("co_email") String co_email);
    boolean getCoStudyProcess(long co_studyId, String co_process);
    void updateCoStudyDeadLine(CoStudy coStudy);
    void updateCoStudyMemberApprove(String co_email, long co_studyId);
    void completeCoStudyRecruitment(Map<String, Object> condition);
    Optional<CoStudy> getCoStudyViewer(String co_viewer, long co_studyId);
    Optional<CoPortfolioOfApplicant> getCoPortfolioOfApplicant(Map<String, Object> coPortfolioDto);
    List<CoApplicantInfo> getCoApplicantsInfo(Map<String, Object> coStudyDto);
    List<CoApplicantCount> getCoApplicantsCount(long co_studyId);
    int getTempsavedApplicantsCount(long co_studyId);
    Optional<CoStudy> getCoStudyViewer(Map<String, Object> coStudyDto);
    List<Boolean> getCoTemporaryStorage(Map<String, Object> coApplicantsInfoDto);
    boolean updateCoTemporaryStorage(Map<String, Object> coApplicantsInfoDto);
}
