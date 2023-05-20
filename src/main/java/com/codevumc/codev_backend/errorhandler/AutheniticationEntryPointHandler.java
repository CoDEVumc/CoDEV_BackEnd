package com.codevumc.codev_backend.errorhandler;

import com.codevumc.codev_backend.domain.ChatRoom;
import com.codevumc.codev_backend.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AutheniticationEntryPointHandler extends ResponseService implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        ErrorCode errorCode;

        if(exception == null) {
            errorCode = ErrorCode.UNAUTHORIZEDException;
            setResponse(response, errorCode);
            return;
        }

        if(exception.equals("FAILEDPUSHNOTIFICATION")) {
            errorCode = ErrorCode.FAILEDPUSHNOTIFICATION;
            setResponse(response, errorCode);
        }

        if(exception.equals("DUPLICATEERROR")) {
            errorCode = ErrorCode.DUPLICATEERROR;
            setChatDuplicateResponse(response, (ChatRoom) request.getAttribute("roomInfo"));
        }
        
        if(exception.equals("PasswordNotFoundException")) {
            errorCode = ErrorCode.PasswordNotFoundException;
            setResponse(response, errorCode);
        }

        if(exception.equals("ForbiddenException")) {
            errorCode = ErrorCode.ForbiddenException;
            setResponse(response, errorCode);
            return;
        }

        //토큰이 만료된 경우
        if(exception.equals("ExpiredJwtException")) {
            errorCode = ErrorCode.ExpiredJwtException;
            setResponse(response, errorCode);
            return;
        }

        //아이디 비밀번호가 다를 경우
        if(exception.equals("UsernameOrPasswordNotFoundException")) {
            errorCode = ErrorCode.UsernameOrPasswordNotFoundException;
            setResponse(response, errorCode);
        }

    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        json.put("code", errorCode.getCode());
        json.put("message", errorCode.getMessage());
        response.getWriter().print(json);

    }

    private void setChatDuplicateResponse(HttpServletResponse response, ChatRoom chatRoom) throws IOException {
        JSONObject json = new JSONObject();
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        json.put("code", 401);
        json.put("message", chatRoom);
        response.getWriter().write(objectMapper.writeValueAsString(json));

    }
}