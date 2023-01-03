package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;


public abstract class CoUserService {



    public CoDevResponse setResponse(int code, String message, Object object) throws Exception {
        CoDevResponse.ResponseMap result = new CoDevResponse.ResponseMap();
        result.setCode(code);
        result.setResponseData(message, object);
        return result;
    }



}
