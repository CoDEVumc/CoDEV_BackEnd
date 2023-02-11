package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoUserMapper;
import com.codevumc.codev_backend.mapper.TokenMapper;
import com.codevumc.codev_backend.mongo_repository.ChatMessageRepository;
import com.codevumc.codev_backend.service.ResponseService;
import com.codevumc.codev_backend.snslogin.GitHubApi;
import com.codevumc.codev_backend.snslogin.GoogleApi;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.CacheableOperation;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class CoUserServiceImpl extends ResponseService implements CoUserService, UserDetailsService {
    private final CoUserMapper coUserMapper;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public CoUserServiceImpl(CoUserMapper coUserMapper, ChatMessageRepository chatMessageRepository) {
        this.coUserMapper = coUserMapper;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public CoDevResponse isExistedEmail(String co_email) {
        try {
            boolean isExisted = coUserMapper.isExistedEmail(co_email);
            if (isExisted) {
                return setResponse(402, "message", "이미 가입된 이메일입니다.");
            } else {
                return setResponse(200, "message", "가입 가능한 아이디입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String co_email) throws UsernameNotFoundException {
        Optional<CoUser> coUser = coUserMapper.findByEmail(co_email);

        if(coUser.isPresent()) return coUser.get();
        else throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
    }

    @Override
    public CoDevResponse signUpCoUser(CoUser coUser) {
        try {
            coUserMapper.insertCoUser(coUser);
            return setResponse(200, "message", "회원가입이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.FAILEDSIGNUP);
        }
    }

    @Override
    public CoDevResponse updateProfile(CoUser coUser) {
        try {
            coUserMapper.updateProfile(coUser.getCo_email(), coUser.getCo_name(), coUser.getCo_nickName());
            return setResponse(200, "message", "프로필이 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.REQUESTFAILED);
        }
    }

    public void updateProfileImg(String profileImg, String co_email) {
        coUserMapper.updateProfileImg(profileImg, co_email);

    }

    @Override
    public CoDevResponse snsLoginMessage(CoUser coUser) {
        CoDevResponse result = null;
        try {
            result = setResponse(505, "message", "/join 으로 이동하여 추가정보를 입력해주세요.");
            addResponse("co_email", coUser.getCo_email());
            addResponse("co_password", coUser.getCo_password());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public boolean isSignup(String co_email) {
        Optional<CoUser> coUser = this.coUserMapper.findByEmail(co_email);

        return coUser.isPresent();
    }

    @Override
    public CoDevResponse updatePassword(HttpServletRequest request, CoUser coUser) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            Optional<CoUser> coUserDto = this.coUserMapper.findByEmail(coUser.getCo_email());
            if (encoder.matches(coUser.getCo_password(),coUserDto.get().getCo_password())){
                this.coUserMapper.updatePassword(coUser);
                return setResponse(200,"message","비밀번호가 변경되었습니다");
            }
            throw new SecurityException();
        } catch (SecurityException e) {
            request.setAttribute("exception", "PasswordNotFoundException");
            throw new AuthenticationCustomException(ErrorCode.PasswordNotFoundException);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    //참고
    private boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true;
        }
        return err;
    }
}
