package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.springframework.security.core.userdetails.UserDetails;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public interface CoUserService {
    UserDetails loadUserByUsername(String co_email);

    CoDevResponse findALlUser(String email);

    CoDevResponse signUpCoUser(CoUser coUser);

    CoDevResponse githubLogin(CoUser coUser, String userAgent, String pw);

    CoDevResponse googleLogin(CoUser coUser);

    boolean isSignup(String co_email);

    MimeMessage createMessage(String to, String randomNumber) throws MessagingException, UnsupportedEncodingException;

    CoDevResponse sendSimpleMessage(String to)throws Exception;

}
