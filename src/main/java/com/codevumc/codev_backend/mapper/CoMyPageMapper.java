package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CoMyPageMapper {
    List<CoStudy> getCoHeartsOfStudy(String co_email);
    List<CoProject> getCoHeartsOfProject(String co_email);
    List<CoPortfolio> getPortfolioByCo_email(String co_email);
    Map<String, String> getUserProfile(String co_email);
    List<CoStudy> getParticipateOfStudies(String co_email);
    List<CoProject> getParticipateOfProjects(String co_email);
    List<CoProject> getRecruitOfProjects(String co_email);
    List<CoStudy> getRecruitOfStudies(String co_email);
    List<CoBoards> getCoMarkOfBoard(String co_email);
}
