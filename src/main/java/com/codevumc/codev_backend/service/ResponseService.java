package com.codevumc.codev_backend.service;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;


public abstract class ResponseService {

    private CoDevResponse.ResponseMap result;

    public CoDevResponse setResponse(int code, String message, Object object) throws Exception {
        result = new CoDevResponse.ResponseMap();
        result.setCode(code);
        result.setResponseData(message, object);
        return result;
    }

    public CoDevResponse addResponse(String message, Object object) throws Exception {
        result.setResponseData(message, object);
        return result;
    }



}
