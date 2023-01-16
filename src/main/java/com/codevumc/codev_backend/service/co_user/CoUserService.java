package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface CoUserService {
    UserDetails loadUserByUsername(String co_email);

    CoDevResponse findALlUser(String email);

    CoDevResponse signUpCoUser(CoUser coUser);

    CoDevResponse githubTest(String authorize_code);

    CoDevResponse googleTest(String authorize_code);
}
