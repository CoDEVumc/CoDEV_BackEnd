package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoUserMapper {

    List<CoUser> findAll();
    Optional<CoUser> findByEmail(String co_email);
    void insertCoUser(CoUser coUser);
    void updateProfileImg(@Param("profileImg") String profileImg, @Param("co_email") String co_email);
    void updateProfile(@Param("co_email") String co_email, @Param("co_name") String co_name, @Param("co_nickName") String co_nickName);
    void updateLoginType(@Param("co_loginType") String co_loginType, @Param("co_email") String co_email);
    boolean isExistedEmail(String co_email);
}
