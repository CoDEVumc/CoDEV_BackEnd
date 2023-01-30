package com.codevumc.codev_backend.controller.co_chat;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.service.co_project.CoProjectService;
import com.codevumc.codev_backend.service.co_project.CoProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ChatTestController {
    public static final int SHOW_COUNT = 5;
    private final CoProjectServiceImpl coProjectService;
    private final ChatTestService chatTestService;

    @Autowired
    public ChatTestController(CoProjectServiceImpl coProjectService, ChatTestService chatTestService) {
        this.coProjectService = coProjectService;
        this.chatTestService = chatTestService;
    }

    @GetMapping("/chatStart")
    public String coStart() {
        return "/index";
    }

}
