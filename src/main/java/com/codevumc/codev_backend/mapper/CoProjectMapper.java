package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface CoProjectMapper {
    void insertCoProject(CoProject coProject);

    Optional<CoProject> findByCoProjectId(long co_projectId);
}
