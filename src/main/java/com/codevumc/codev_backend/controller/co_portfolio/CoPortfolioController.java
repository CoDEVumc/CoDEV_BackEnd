package com.codevumc.codev_backend.controller.co_portfolio;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_portfolio.CoPortfolioServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/p1/co_portfolio")
public class CoPortfolioController extends JwtController {
    private final CoPortfolioServiceImpl coPortfolioService;

    public CoPortfolioController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoPortfolioServiceImpl coPortfolioService) {
        super(jwtTokenProvider, jwtService);
        this.coPortfolioService = coPortfolioService;
    }

    @PostMapping(value = "/write", consumes = { MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public CoDevResponse write(HttpServletRequest request, @RequestPart Map<String, Object> portfolio) throws Exception {
        CoPortfolio coPortfolio = CoPortfolio.builder()
                .co_email(getCoUserEmail(request))
                .co_title(portfolio.get("co_title").toString())
                .co_rank(portfolio.get("co_rank").toString())
                .co_introduction(portfolio.get("co_introduction").toString())
                .build();

        JSONParser parser = new JSONParser();
        Gson gson = new Gson();

        String co_portfolioLanguages = gson.toJson(portfolio.get("co_portfolioLanguages"));
        JSONArray co_portfolioLanguagesList = (JSONArray) parser.parse(co_portfolioLanguages);
        String co_portfolioLinks = gson.toJson(portfolio.get("co_portfolioLinks"));
        JSONArray co_portfolioLinksList = (JSONArray) parser.parse(co_portfolioLinks);
        this.coPortfolioService.insertCoPortfolio(coPortfolio, co_portfolioLanguagesList, co_portfolioLinksList);

        return null;
    }
}
