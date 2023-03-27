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
    public static final int SHOW_COUNT = 10;
    private final CoBoardSearchService coBoardSearchService;

    public CoBoardSearchController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoBoardSearchService coBoardSearchService) {
        super(jwtTokenProvider, jwtService);
        this.coBoardSearchService = coBoardSearchService;
    }


    @GetMapping("/search/{page}")
    public CoDevResponse getSearch(HttpServletRequest request, @PathVariable("page") int pageNum, @RequestParam("searchTag") String searchTag, @RequestParam("coMyBoard") boolean coMyBoard, @RequestParam("sortingTag") String sortingTag, @RequestParam("type") String type) throws Exception{
        int limit = getLimitCnt(pageNum);
        int offset = limit - SHOW_COUNT;
        return coBoardSearchService.searchBoard(getCoUserEmail(request), SHOW_COUNT, offset, pageNum, coMyBoard, searchTag, sortingTag.toUpperCase(), type);

    }


    private int getLimitCnt(int pageNum) {
        int limit = SHOW_COUNT;
        for(int i = 0; i <= pageNum; i++) {
            if(i != 0)
                limit += SHOW_COUNT;
        }

        return limit;
    }

}
