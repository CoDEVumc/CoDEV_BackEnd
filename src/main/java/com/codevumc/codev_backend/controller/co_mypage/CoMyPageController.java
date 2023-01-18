package com.codevumc.codev_backend.controller.co_mypage;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_mypage.CoMyPageServiceImpl;
import com.codevumc.codev_backend.service.co_portfolio.CoPortfolioServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/p1/co_mypage")
public class CoMyPageController extends JwtController {
    private final CoMyPageServiceImpl coMyPageService;

    public CoMyPageController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoMyPageServiceImpl coMyPageService, CoPortfolioServiceImpl coPortfolioService) {
        super(jwtTokenProvider, jwtService);
        this.coMyPageService = coMyPageService;
    }


    /**
     * 포트폴리오 수정
     *
     * */
    @ResponseBody
    @PatchMapping("/{co_portfolioId}")
    public CoDevResponse updateCoPortfolio(HttpServletRequest request, @PathVariable("co_portfolioId") Long co_portfolioId, @RequestBody CoPortfolio coPortfolio) throws Exception{
            //유저 email 일치한지 확인 코드 필요
            coPortfolio.setCo_email(getCoUserEmail(request));
            coPortfolio.setCo_portfolioId(co_portfolioId);
            return coMyPageService.updateCoPortfolio(coPortfolio);
    }
}
