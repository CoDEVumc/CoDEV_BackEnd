package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPhotos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoPhotosMapper {
    void insertCoPhoto(com.codevumc.codev_backend.domain.CoPhotos coPhotos);

    void deleteCoPhotoOfProject(String co_targetId);

    List<CoPhotos> findByCoTargetId(@Param("co_targetId") String co_targetId, @Param("co_type") String co_type);

    List<CoPhotos> findByFileUrl(@Param("co_fileUrl") String co_fileUrl);

    Optional<CoPhotos> findByCo_uuId(String co_uuId);

    Optional<CoPhotos> findByCo_mainImg(@Param("co_type") String co_type, @Param("co_targetId") String co_targetId);

}
