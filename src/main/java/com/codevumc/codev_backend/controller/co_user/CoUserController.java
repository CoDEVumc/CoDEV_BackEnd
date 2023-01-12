package com.codevumc.codev_backend.controller.co_user;


import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.RefreshToken;
import com.codevumc.codev_backend.domain.role.Role;
import com.codevumc.codev_backend.email.EmailService;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_user.CoUserServiceImpl;
import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.service.co_user.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
public class CoUserController extends JwtController {
    //사용자가 프로필이미지를 직접 주입 안하면, 이 이미지로 프론트가 처리 해준다.
    private final PasswordEncoder passwordEncoder;
    private final CoUserServiceImpl coUserService;
    private final EmailService emailService;

    public CoUserController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, PasswordEncoder passwordEncoder, CoUserServiceImpl coUserService, EmailService emailService) {
        super(jwtTokenProvider, jwtService);
        this.passwordEncoder = passwordEncoder;
        this.coUserService = coUserService;
        this.emailService = emailService;
    }

    @PostMapping("/mailConfirm")
    public String mailConfirm(@RequestParam String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        return code;
    }


    @PostMapping("/join")
    public CoDevResponse signUp(@RequestBody Map<String, String> user) {
        Role role = Role.from(user.get("role"));
        CoUser coUser = CoUser.builder()
                .co_email(user.get("co_email"))
                .co_password(passwordEncoder.encode(user.get("co_password")))
                .co_nickName(user.get("co_nickName"))
                .profileImg(user.get("profileImg"))
                .role(role)
                .roles(Collections.singletonList(role.getValue())).build();
//        if(user.get("profileImg") != null)
//            coUser.setProfileImg(user.get("profileImg"));
        //TO-DO
        // 사용자가 이미지를 삽입하면, 파일에 저장한 후 파일 링크를 setProfileImg
        return coUserService.signUpCoUser(coUser);
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
    public CoDevResponse gitHubLogin(@RequestBody @RequestParam(value = "code") String code) throws Exception {
        return coUserService.githubTest(code);
    }


}
