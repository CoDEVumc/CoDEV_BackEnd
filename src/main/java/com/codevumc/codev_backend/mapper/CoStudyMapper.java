package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoHeartOfStudy;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface CoStudyMapper {
    Optional<CoHeartOfStudy> getCoHeartOfStudy(Long co_studyId);
    void insertCoHeartOfStudy(String co_email, Long co_studyId);
    void deleteCoHeartOfStudy(String co_email, Long co_studyId);
}
