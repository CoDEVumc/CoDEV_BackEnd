package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoProject;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface CoProjectMapper {
    long insertCoProject(CoProject coProject);

    Optional<CoProject> findByCoProjectId(long co_projectId);
}
