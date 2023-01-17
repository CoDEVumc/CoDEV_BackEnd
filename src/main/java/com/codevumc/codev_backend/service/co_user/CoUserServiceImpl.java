package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoUserMapper;
import com.codevumc.codev_backend.service.ResponseService;
import com.codevumc.codev_backend.snslogin.GitHubApi;
import com.codevumc.codev_backend.snslogin.GoogleApi;
import lombok.AllArgsConstructor;
import org.springframework.cache.interceptor.CacheableOperation;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@AllArgsConstructor
@Service
public class CoUserServiceImpl extends ResponseService implements CoUserService, UserDetailsService {
    private final CoUserMapper coUserMapper;
    private final GitHubApi gitHubApi;
    private final GoogleApi googleApi;
    @Override
    public UserDetails loadUserByUsername(String co_email) throws UsernameNotFoundException {
        Optional<CoUser> coUser = coUserMapper.findByEmail(co_email);

        if(coUser.isPresent()) return coUser.get();
        else throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
    }

    @Override
    public CoDevResponse findALlUser(String email) {
        try {

            return setResponse(200, "Success", email);
        }catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.ForbiddenException);
        }

    }

    @Override
    public CoDevResponse signUpCoUser(CoUser coUser) {
        try {
            coUserMapper.insertCoUser(coUser);
            return setResponse(200, "coUser", coUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.FAILEDSIGNUP);
        }
    }

    @Override
    public CoDevResponse githubTest(String authorize_code) {
        try {
            Map<String, Object> userInfo =  gitHubApi.getUserInfo(gitHubApi.getAccessTocken(authorize_code));
            CoUser coUser = CoUser.builder()
                    .co_email(userInfo.get("co_email").toString())
                    .co_password(userInfo.get("co_password").toString())
                    .build();
            return setResponse(200, "success", coUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse googleTest(String authorize_code){
        try {
            Map<String, Object> userInfo =  googleApi.getUserInfo(googleApi.getAccessToken(authorize_code));
            CoUser coUser = CoUser.builder()
                    .co_email(userInfo.get("co_email").toString())
                    .co_password(userInfo.get("co_password").toString())
                    .build();
            return setResponse(200, "success", coUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
