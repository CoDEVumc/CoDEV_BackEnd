package com.codevumc.codev_backend.controller.co_board_search;


import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_board_search.CoBoardSearchService;
import com.codevumc.codev_backend.service.co_user.JwtService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/codev/board")
public class CoBoardSearchController extends JwtController {

    private final CoBoardSearchService coBoardSearchService;

    public CoBoardSearchController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoBoardSearchService coBoardSearchService) {
        super(jwtTokenProvider, jwtService);
        this.coBoardSearchService = coBoardSearchService;
    }


    @GetMapping("/search")
    public CoDevResponse getSearch(HttpServletRequest request, @RequestParam("searchTag") String searchTag) throws Exception{

        return coBoardSearchService.searchBoard(getCoUserEmail(request), searchTag);

    }

}
