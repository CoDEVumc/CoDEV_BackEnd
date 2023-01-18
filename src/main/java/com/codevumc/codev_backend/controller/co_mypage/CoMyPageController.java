package com.codevumc.codev_backend.controller.co_mypage;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_mypage.CoMyPageServiceImpl;
import com.codevumc.codev_backend.service.co_portfolio.CoPortfolioServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
    public CoDevResponse updateCoPortfolio(HttpServletRequest request, @PathVariable("co_portfolioId") Long co_portfolioId, @RequestBody Map<String,Object> portfolio) throws Exception{

            //유저 email 일치한지 확인 코드 필요

        CoPortfolio coPortfolio = CoPortfolio.builder()
                .co_portfolioId(co_portfolioId)
                .co_email(getCoUserEmail(request))
                .co_name(portfolio.get("co_name").toString())
                .co_gender(portfolio.get("co_gender").toString())
                .co_birth(portfolio.get("co_birth").toString())
                .co_rank(portfolio.get("co_rank").toString())
                .co_headLine(portfolio.get("co_headLine").toString())
                .co_introduction(portfolio.get("co_introduction").toString()).build();
            //title추가해야함

        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String co_portfolioLinks = gson.toJson(portfolio.get("co_link"));
        JSONArray co_portfoliolinksList = (JSONArray) parser.parse(co_portfolioLinks);
        String co_portfolioLanguages = gson.toJson(portfolio.get("co_portfolioLanguages"));
        JSONArray co_portfolioLaguagesList = (JSONArray) parser.parse(co_portfolioLanguages);
        this.coMyPageService.updateCoPortfolio(coPortfolio,co_portfoliolinksList,co_portfolioLaguagesList);

        return null;
    }



}
