package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoStudy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoMyPageMapper {
    List<Long> getCoHeartsOfStudy(String co_email);
    List<CoStudy> getCoStudies(String co_email, long co_studyId);
    List<Long> getCoHeartsOfProject(String co_email);
    List<CoProject> getCoProjects(Long coProjectId);
}
