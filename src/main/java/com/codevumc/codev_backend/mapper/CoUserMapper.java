package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoUserMapper {

    List<CoUser> findAll();
    Optional<CoUser> findByEmail(String co_email);
    void insertCoUser(CoUser coUser);
}
