package com.codevumc.codev_backend.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private int refreshTokenId;
    private String refreshToken;
    private String keyId;
    private String userAgent;
}
