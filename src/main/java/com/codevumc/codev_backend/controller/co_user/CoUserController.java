package com.codevumc.codev_backend.controller.co_user;


import com.codevumc.codev_backend.service.co_user.CoUserServiceImpl;
import com.codevumc.codev_backend.domain.CoUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoUserController {


    private CoUserServiceImpl coUserService;

    public CoUserController(CoUserServiceImpl coUserService) {
        this.coUserService = coUserService;
    }

    @RequestMapping("/test")
    public List<CoUser> getCoUserList() {
        return coUserService.findALlUser();
    }
}
