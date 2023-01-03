package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.mapper.CoUserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class CoUserServiceImpl extends CoUserService implements UserDetailsService {
    private final CoUserMapper coUserMapper;

    @Override
    public UserDetails loadUserByUsername(String co_email) throws UsernameNotFoundException {
        Optional<CoUser> coUser = coUserMapper.findByEmail(co_email);

        if(coUser.isPresent()) return coUser.get();
        else throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
    }

    public CoDevResponse findALlUser() {
        try {
            List<CoUser> coUserList = coUserMapper.findAll();
            return setResponse(200, "Success", coUserList);
        }catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.ForbiddenException);
        }

    }

    public CoDevResponse signUpCoUser(CoUser coUser) {
        try {
            coUserMapper.insertCoUser(coUser);
            return setResponse(200, "coUser", coUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.FAILEDSIGNUP);
        }
    }


}
