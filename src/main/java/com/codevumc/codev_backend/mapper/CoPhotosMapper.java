package com.codevumc.codev_backend.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoPhotosMapper {
    void insertCoPhotoOfProject(com.codevumc.codev_backend.domain.CoPhotos coPhotos);

    void deleteCoPhotoOfProject(long co_projectId);

    List<com.codevumc.codev_backend.domain.CoPhotos> findByCoProjectId(long co_projectId);

    Optional<com.codevumc.codev_backend.domain.CoPhotos> findByCo_uuId(String co_uuId);
}
