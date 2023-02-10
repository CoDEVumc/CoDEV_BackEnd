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
                .co_loginType(user.get("co_loginType").toString())
                .role(role)
                .roles(Collections.singletonList(role.getValue())).build();
        CoDevResponse result = coUserService.signUpCoUser(coUser);
        if(file != null) {
            coFileService.storeFile(file, user.get("co_email").toString(), "USER");
            coUserService.updateProfileImg(coFileService.getCo_MainImg("USER", user.get("co_email").toString()), user.get("co_email").toString());
        }
        return result;
    }

    @PutMapping(value = "/update/profile", consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse updateProfile(HttpServletRequest request, @RequestPart Map<String, Object> user, @RequestPart(required = false) MultipartFile file) throws Exception {
        CoUser coUser = CoUser.builder()
                .co_email(getCoUserEmail(request))
                .co_name(user.get("co_name").toString())
                .co_nickName(user.get("co_nickName").toString()).build();
        CoDevResponse result = coUserService.updateProfile(coUser);

        coFileService.deleteFile(coUser.getCo_email(), "USER");
        if(file != null) {
            coFileService.storeFile(file, coUser.getCo_email(), "USER");
            coUserService.updateProfileImg(coFileService.getCo_MainImg("USER", coUser.getCo_email()), coUser.getCo_email());
        }
        return result;
    }

    @PostMapping("/login")
    public CoDevResponse login(HttpServletRequest request, @RequestBody Map<String, String> user, @RequestHeader("User-Agent") String userAgent) {
        return getJwtService().login(request, user, userAgent);
    }


    @PostMapping("/token/refresh")
    public CoDevResponse validateRefreshToken(@RequestBody RefreshToken bodyJson) throws Exception {
        return getJwtService().newAccessToken(bodyJson);
    }

    @PostMapping("/sns/login")
    public CoDevResponse snsLogin(HttpServletRequest request, @RequestBody Map<String, String> user, @RequestHeader("User-Agent") String userAgent) throws Exception{
        String co_loginType = user.get("co_loginType");
        Map<String, Object> userInfo = co_loginType.equals(CoUser.loginType.GOOGLE.getValue()) ? googleApi.getUserInfo(user.get("accessToken")) : gitHubApi.getUserInfo(gitHubApi.getAccessTocken(user.get("code")));
        if(coUserService.isSignup(userInfo.get("co_email").toString())) {
            return getJwtService().snsLogin(request, userInfo.get("co_email").toString(), co_loginType, userAgent);
        }else {
            CoUser coUser = CoUser.builder()
                    .co_email(userInfo.get("co_email").toString())
                    .co_password(userInfo.get("co_password").toString())
                    .build();
            return coUserService.snsLoginMessage(coUser);
        }
    }

    @PostMapping("/duplication")
    public CoDevResponse checkEmail(@RequestParam String email) throws Exception {
        return coUserService.isExistedEmail(email);
    }

    @PatchMapping("/update/password")
    public CoDevResponse updatePassword(HttpServletRequest request, @RequestBody Map<String, Object> user) throws Exception {
        CoUser coUser = CoUser.builder()
                .co_email(getCoUserEmail(request))
                .co_password(user.get("co_password").toString())
                .co_newPassword(passwordEncoder.encode(user.get("co_newPassword").toString())).build();
        return coUserService.updatePassword(request, coUser);
    }




}
