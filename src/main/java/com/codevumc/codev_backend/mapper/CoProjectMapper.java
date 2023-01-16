package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface CoProjectMapper {
    void insertCoProject(CoProject coProject);
    void insertCoPartOfProject(Map<String, Object> coPartsDto);
    void insertCoLanguageOfProject(@Param("co_projectId") long co_projectId, @Param("co_languageId") long co_languageId);
    List<CoProject> getCoProjects(Map<String, Object> coProjectsDto);

}
