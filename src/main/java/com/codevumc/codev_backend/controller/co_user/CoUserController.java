package com.codevumc.codev_backend.controller.co_user;


import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.domain.RefreshToken;
import com.codevumc.codev_backend.domain.role.Role;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_file.CoFileServiceImpl;
import com.codevumc.codev_backend.service.co_user.CoUserServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.codevumc.codev_backend.snslogin.GitHubApi;
import com.codevumc.codev_backend.snslogin.GoogleApi;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/codev/user")
public class CoUserController extends JwtController {
    //사용자가 프로필이미지를 직접 주입 안하면, 이 이미지로 프론트가 처리 해준다.
    private final PasswordEncoder passwordEncoder;
    private final CoFileServiceImpl coFileService;
    private final CoUserServiceImpl coUserService;
    private final GitHubApi gitHubApi;
    private final GoogleApi googleApi;

    public CoUserController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, PasswordEncoder passwordEncoder, CoFileServiceImpl coFileService, CoUserServiceImpl coUserService, GitHubApi gitHubApi, GoogleApi googleApi) {
        super(jwtTokenProvider, jwtService);
        this.passwordEncoder = passwordEncoder;
        this.coFileService = coFileService;
        this.coUserService = coUserService;
        this.gitHubApi = gitHubApi;
        this.googleApi = googleApi;
    }


    @PostMapping(value = "/join", consumes = { MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse signUp(@RequestPart Map<String, Object> user, @RequestPart(required = false) MultipartFile file) throws Exception {
        Role role = Role.from(user.get("role").toString());
        CoUser coUser = CoUser.builder()
                .co_email(user.get("co_email").toString())
                .co_password(passwordEncoder.encode(user.get("co_password").toString()))
                .co_nickName(user.get("co_nickName").toString())
                .co_name(user.get("co_name").toString())
                .co_birth(user.get("co_birth").toString())
                .co_gender(user.get("co_gender").toString())
                .role(role)
                .roles(Collections.singletonList(role.getValue())).build();
        CoDevResponse result = coUserService.signUpCoUser(coUser);
        if(file != null) {
            coFileService.storeFile(file, user.get("co_email").toString(), "USER");
            coUserService.updateProfileImg(coFileService.getCo_MainImg("USER", user.get("co_email").toString()), user.get("co_email").toString());
        }
        return result;
    }

    @PostMapping("/login")
    public CoDevResponse login(HttpServletRequest request, @RequestBody Map<String, String> user, @RequestHeader("User-Agent") String userAgent) {
        return getJwtService().login(request, user, userAgent);
    }

    @RequestMapping("/testJWT")
    public CoDevResponse getCoUserList(HttpServletRequest request) throws Exception {
        return coUserService.findALlUser(getCoUserEmail(request));
    }

    @PostMapping("/token/refresh")
    public CoDevResponse validateRefreshToken(@RequestBody RefreshToken bodyJson) throws Exception {
        return getJwtService().newAccessToken(bodyJson);
    }

    @GetMapping("/github/login")
    public CoDevResponse gitHubLogin(@RequestBody @RequestParam(value = "code") String code, @RequestHeader("User-Agent") String userAgent) throws Exception {
        Map<String, Object> userInfo =  gitHubApi.getUserInfo(gitHubApi.getAccessTocken(code));
        CoUser coUser = CoUser.builder()
                .co_email(userInfo.get("co_email").toString())
                .co_password(passwordEncoder.encode(userInfo.get("co_password").toString()))
                .co_nickName("")
                .co_name("")
                .co_birth("")
                .co_gender("")
                .role(Role.USER)
                .roles(Collections.singletonList(Role.USER.getValue()))
                .build();
        return coUserService.githubLogin(coUser, userAgent, userInfo.get("co_password").toString());
    }

    @GetMapping("/google/login")
    public CoDevResponse googleLogin(@RequestBody @RequestParam(value = "code") String code, @RequestHeader("User-Agent") String userAgent) throws Exception{
        Map<String, Object> userInfo =  googleApi.getUserInfo(googleApi.getAccessToken(code));
        CoUser coUser = CoUser.builder()
                .co_email(userInfo.get("co_email").toString())
                .co_password(userInfo.get("co_password").toString())
                .co_nickName("")
                .co_name("")
                .co_birth("")
                .co_gender("")
                .role(Role.USER)
                .roles(Collections.singletonList(Role.USER.getValue()))
                .build();
        return coUserService.googleLogin(coUser);
    }

    @PostMapping("/sns/login")
    public CoDevResponse snsLogin(HttpServletRequest request, @RequestBody Map<String, String> user, @RequestHeader("User-Agent") String userAgent) {
        if(coUserService.isSignup(user.get("co_email"))) {
            //회원가입 되어있을때
            System.out.println("already signup");
        }else {
            //회원가입이 안되어있을때
            System.out.println("start signup");
        }
        return null;
    }

    @GetMapping("/code/mail")
    public CoDevResponse mailConfirm(@RequestParam String email) throws Exception {
        return coUserService.sendSimpleMessage(email);
    }


}
