package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoLanguage;
import com.codevumc.codev_backend.domain.CoPhotos;
import com.codevumc.codev_backend.domain.CoStudy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoStudyMapper {
    Optional<CoStudy> getCoStudy(long co_studyId);
    List<CoLanguage> getCoLanguageList(long co_studyId);
    long getCoHeartCount(long co_studyId);
}
