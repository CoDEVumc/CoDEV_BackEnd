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

    CoDevResponse githubTest(String authorize_code);

    CoDevResponse googleTest(String authorize_code);

    MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;

    CoDevResponse sendSimpleMessage(String to)throws Exception;

}
