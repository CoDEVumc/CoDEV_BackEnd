package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoProject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoProjectMapper {
    void insertCoProject(CoProject coProject);

    void insertCoLanguageOfCoProject(long co_projectId, long co_languageId);

    void insertCoPartOfCoProject(long co_projectId, long co_partId, long co_limit);
    //Optional<CoProject> findByCoProjectId(long co_projectId);


}
