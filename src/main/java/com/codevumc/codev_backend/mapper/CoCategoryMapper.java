package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoLocation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoCategoryMapper {
    List<CoLocation> getLocation();
}
