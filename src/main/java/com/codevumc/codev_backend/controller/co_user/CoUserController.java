package com.codevumc.codev_backend.controller.co_user;


import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.RefreshToken;
import com.codevumc.codev_backend.domain.role.Role;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_user.CoUserServiceImpl;
import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.service.co_user.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class CoUserController extends JwtController {
    private final PasswordEncoder passwordEncoder;
    private final CoUserServiceImpl coUserService;


    public CoUserController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, PasswordEncoder passwordEncoder, CoUserServiceImpl coUserService) {
        super(jwtTokenProvider, jwtService);
        this.passwordEncoder = passwordEncoder;
        this.coUserService = coUserService;
    }


    @PostMapping("/join")
    public CoDevResponse signUp(@RequestBody Map<String, String> user) {
        Role role = Role.from(user.get("role"));
        CoUser coUser = CoUser.builder()
                .co_email(user.get("co_email"))
                .co_id(user.get("co_id"))
                .co_password(passwordEncoder.encode(user.get("co_password")))
                .co_nickName(user.get("co_nickName"))
                .co_gender(user.get("co_gender"))
                .co_phone(user.get("co_phone"))
                .role(role)
                .roles(Collections.singletonList(role.getValue()))
                .profileImg(user.get("profileImg")).build();
        return coUserService.signUpCoUser(coUser);
    }

    @PostMapping("/login")
    public CoDevResponse login(HttpServletRequest request, @RequestBody Map<String, String> user, @RequestHeader("User-Agent") String userAgent) {
        return getJwtService().login(request, user, userAgent);
    }

    @RequestMapping("/testJWT")
    public CoDevResponse getCoUserList(HttpServletRequest request) {
        return coUserService.findALlUser();
    }

    @PostMapping("/token/refresh")
    public CoDevResponse validateRefreshToken(@RequestBody RefreshToken bodyJson) {
        return getJwtService().newAccessToken(bodyJson);
    }
}
