package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoUserMapper {

    List<CoUser> findAll();
}
