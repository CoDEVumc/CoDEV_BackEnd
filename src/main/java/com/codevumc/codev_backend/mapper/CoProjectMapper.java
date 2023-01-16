package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoHeartOfProject;
import com.codevumc.codev_backend.domain.CoProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface CoProjectMapper {
    void insertCoProject(CoProject coProject);
    void insertCoPartOfProject(@Param("co_projectId") long co_projectId, @Param("co_partId") long co_partId, @Param("co_limit") long co_limit);
    void insertCoLanguageOfProject(@Param("co_projectId") long co_projectId, @Param("co_languageId") long co_languageId);
    void insertCoHeartOfProject(String co_email, Long co_projectId);
    Optional<CoHeartOfProject> getCoHeartOfProject(Long co_projectId);
    void updateCoHeartOfProject(String co_email, Long co_projectId);

}
