package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPhotoOfProject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoPhotoOfProjectMapper {
    void insertCoPhotoOfProject(CoPhotoOfProject coPhotoOfProject);

    void deleteCoPhotoOfProject(long co_projectId);

    List<CoPhotoOfProject> findByCoProjectId(long co_projectId);

    Optional<CoPhotoOfProject> findByCo_uuId(String co_uuId);
}
