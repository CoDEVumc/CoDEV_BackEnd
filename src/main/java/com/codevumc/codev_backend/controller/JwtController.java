package com.codevumc.codev_backend.controller;

import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_user.JwtService;

import javax.servlet.http.HttpServletRequest;

public abstract class JwtController {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;


    public JwtController(JwtTokenProvider jwtTokenProvider, JwtService jwtService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtService = jwtService;
    }
    public JwtService getJwtService() { return jwtService; }
    public JwtTokenProvider getJwtTokenProvider() {
        return jwtTokenProvider;
    }

    public String getCoUserEmail(HttpServletRequest request) throws Exception {
        return jwtTokenProvider.getUserPk(jwtTokenProvider.getAccessToken(request));
    }

}
