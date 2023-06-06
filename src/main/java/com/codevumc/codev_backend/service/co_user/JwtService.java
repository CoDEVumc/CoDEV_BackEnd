package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.domain.RefreshToken;
import com.codevumc.codev_backend.domain.Token;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.mapper.CoUserMapper;
import com.codevumc.codev_backend.mapper.TokenMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenMapper tokenMapper;
    private final CoUserMapper coUserMapper;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public CoDevResponse login(HttpServletRequest request, Map<String, String> user, String userAgent) {

        CoDevResponse.ResponseMap result = new CoDevResponse.ResponseMap();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.get("co_email"), user.get("co_password"))
            );

            coUserMapper.updateLoginType(CoUser.loginType.CODEV.getValue(), user.get("co_email"));

            Token token = getCoDevToken(user.get("co_email"), userAgent);

            //FCM토큰 새로 저장
            this.coUserMapper.updateFCMToken(user.get("FCMToken"), user.get("co_email"));
            result.setResponseData("accessToken", token.getAccessToken());
            result.setResponseData("refreshToken", token.getRefreshToken());
            result.setResponseData("key", token.getKey());
        }catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", "UsernameOrPasswordNotFoundException");
            throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
        }
        return result;
    }

    @Transactional
    public CoDevResponse snsLogin(HttpServletRequest request, String co_email, String loginType, String userAgent) {

        CoDevResponse.ResponseMap result = new CoDevResponse.ResponseMap();
        try {
            coUserMapper.updateLoginType(CoUser.loginType.from(loginType).getValue(), co_email);

            Token token = getCoDevToken(co_email, userAgent);

            result.setResponseData("accessToken", token.getAccessToken());
            result.setResponseData("refreshToken", token.getRefreshToken());
            result.setResponseData("key", token.getKey());
        }catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", "UsernameOrPasswordNotFoundException");
            throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
        }
        return result;
    }

    private Token getCoDevToken(String co_email, String userAgent) {
        Token token = jwtTokenProvider.createToken(co_email);
        //RefreshToken을 DB에 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .keyId(token.getKey())
                .refreshToken(token.getRefreshToken()).
                userAgent(userAgent).build();

        Optional<RefreshToken> tokenOptional = tokenMapper.findByKeyId(co_email);

        if(tokenOptional.isPresent()) {
            if(!tokenOptional.get().getUserAgent().equals(userAgent)) {
                tokenMapper.deleteByKeyId(co_email);
                tokenMapper.insertRefreshToken(refreshToken);
            }
        }else {
            tokenMapper.insertRefreshToken(refreshToken);
        }
        return token;
    }


    public CoDevResponse newAccessToken(RefreshToken refreshToken) {
        CoDevResponse.ResponseMap result = new CoDevResponse.ResponseMap();
        if(refreshToken.getRefreshToken() != null) {
            String newToken = jwtTokenProvider.validateRefreshToken(refreshToken);
            result.setResponseData("accessToken", newToken);
        }else {
            result.setResponseData("code", ErrorCode.ReLogin.getCode());
            result.setResponseData("message", ErrorCode.ReLogin.getMessage());
            result.setResponseData("HttpStatus", ErrorCode.ReLogin.getStatus());
        }
        return result;
    }
}
