package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CoProjectMapper {
    void insertCoProject(CoProject coProject);
    void insertCoPartOfProject(@Param("co_projectId") long co_projectId, @Param("co_partId") long co_partId, @Param("co_limit") long co_limit);
    void insertCoLanguageOfProject(@Param("co_projectId") long co_projectId, @Param("co_languageId") long co_languageId);

}
