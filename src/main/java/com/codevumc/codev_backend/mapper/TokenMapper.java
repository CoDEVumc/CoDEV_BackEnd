package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface TokenMapper {
    void insertRefreshToken(RefreshToken refreshToken);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByKeyIdAndUserAgent(String co_email, String userAgent);
    void deleteByKeyIdAndUserAgent(String co_email, String userAgent);
}
