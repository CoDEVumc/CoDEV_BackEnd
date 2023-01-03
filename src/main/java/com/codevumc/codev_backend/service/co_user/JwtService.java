package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.domain.RefreshToken;
import com.codevumc.codev_backend.domain.Token;
import com.codevumc.codev_backend.errorhandler.ApiResponse;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.mapper.CoUserMapper;
import com.codevumc.codev_backend.mapper.TokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenMapper tokenMapper;
    private final CoUserMapper coUserMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ApiResponse login(HttpServletRequest request, Map<String, String> user, String userAgent) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.get("co_email"), user.get("co_password"))
            );

            CoUser userinfo = coUserMapper.findByEmail(user.get("co_email")).get();
            Token token = jwtTokenProvider.createToken(user.get("co_email"), userinfo.getRoles());

            //RefreshToken을 DB에 저장
            RefreshToken refreshToken = RefreshToken.builder()
                    .keyId(token.getKey())
                    .refreshToken(token.getRefreshToken()).
                    userAgent(userAgent).build();
            String loginUserId = refreshToken.getKeyId();

            //다음번 로그인시 UserAgent값이 다를 경우 다른 기기에서 로그인한경우인데 여기 if문에서 처리해주면 된다
            if(tokenMapper.findByKeyIdAndUserAgent(loginUserId, userAgent).isEmpty()) {
                tokenMapper.deleteByKeyIdAndUserAgent(loginUserId, userAgent);
            }
            tokenMapper.insertRefreshToken(refreshToken);

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

    public ApiResponse newAccessToken(RefreshToken refreshToken) {
        ApiResponse.ResponseMap result = new ApiResponse.ResponseMap();
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
