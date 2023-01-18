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
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @PostMapping(value = "/write")
    public CoDevResponse write(HttpServletRequest request, @RequestBody Map<String, Object> portfolio) throws Exception {
        CoPortfolio coPortfolio = CoPortfolio.builder()
                .co_email(getCoUserEmail(request))
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
        this.coPortfolioService.insertCoPortfolio(coPortfolio, co_languagesList, co_linksList);
        return null;
    }
}
