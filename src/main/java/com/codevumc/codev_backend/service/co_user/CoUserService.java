package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

public interface CoUserService {
    UserDetails loadUserByUsername(String co_email);

    CoDevResponse signUpCoUser(CoUser coUser);

    CoDevResponse updateProfile(CoUser coUser);

    boolean isSignup(String co_email);

    CoDevResponse snsLoginMessage(CoUser coUser);

    CoDevResponse isExistedEmail(String co_email);

    CoDevResponse updatePassword(HttpServletRequest request, CoUser coUser);

    void updateProfileImg(String profileImg, String co_email);
}
