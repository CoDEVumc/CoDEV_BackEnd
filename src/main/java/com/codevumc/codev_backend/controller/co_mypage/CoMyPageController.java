package com.codevumc.codev_backend.controller.co_mypage;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_mypage.CoMyPageServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/p1/co_mypage")
public class CoMyPageController extends JwtController {
    private final CoMyPageServiceImpl coMyPageService;

    public CoMyPageController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoMyPageServiceImpl coMyPageService) {
        super(jwtTokenProvider, jwtService);
        this.coMyPageService = coMyPageService;
    }
}
