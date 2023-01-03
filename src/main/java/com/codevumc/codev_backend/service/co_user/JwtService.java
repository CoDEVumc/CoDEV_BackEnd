package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.domain.RefreshToken;
import com.codevumc.codev_backend.domain.Token;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
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

            CoUser userinfo = coUserMapper.findByEmail(user.get("co_email")).get();
            Token token = jwtTokenProvider.createToken(user.get("co_email"), userinfo.getRoles());
            //RefreshToken을 DB에 저장
            RefreshToken refreshToken = RefreshToken.builder()
                    .keyId(token.getKey())
                    .refreshToken(token.getRefreshToken()).
                    userAgent(userAgent).build();

            Optional<RefreshToken> tokenOptional = tokenMapper.findByKeyId(user.get("co_email"));

            if(tokenOptional.isPresent()) {
                if(!tokenOptional.get().getUserAgent().equals(userAgent)) {
                    tokenMapper.deleteByKeyId(user.get("co_email"));
                    tokenMapper.insertRefreshToken(refreshToken);
                }
            }else {
                tokenMapper.insertRefreshToken(refreshToken);
            }
//            String loginUserId = refreshToken.getKeyId();

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
