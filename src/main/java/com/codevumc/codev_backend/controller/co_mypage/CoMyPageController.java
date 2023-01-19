package com.codevumc.codev_backend.controller.co_mypage;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_mypage.CoMyPageServiceImpl;
import com.codevumc.codev_backend.service.co_portfolio.CoPortfolioServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/codev/my-page")
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
    @PatchMapping("/portfolio/{coPortfolioId}")
    public CoDevResponse updateCoPortfolio(HttpServletRequest request, @PathVariable("coPortfolioId") Long co_portfolioId, @RequestBody Map<String,Object> portfolio) throws Exception{
        CoPortfolio coPortfolio = CoPortfolio.builder()
                .co_email(getCoUserEmail(request))
                .co_portfolioId(co_portfolioId)
                .co_title(portfolio.get("co_title").toString())
                .co_rank(portfolio.get("co_rank").toString())
                .co_headLine(portfolio.get("co_headLine").toString())
                .co_introduction(portfolio.get("co_introduction").toString())
                .build();

        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String co_languages = gson.toJson(portfolio.get("co_languages"));
        JSONArray co_languagesList = (JSONArray) parser.parse(co_languages);
        String co_links = gson.toJson(portfolio.get("co_links"));
        JSONArray co_linksList = (JSONArray) parser.parse(co_links);
        return coMyPageService.updateCoPortfolio(coPortfolio, co_languagesList, co_linksList);

    }



}
