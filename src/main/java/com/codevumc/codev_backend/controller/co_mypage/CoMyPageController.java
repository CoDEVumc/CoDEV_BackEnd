package com.codevumc.codev_backend.controller.co_mypage;

import com.codevumc.codev_backend.controller.JwtController;
import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import com.codevumc.codev_backend.service.co_mypage.CoMyPageServiceImpl;
import com.codevumc.codev_backend.service.co_user.JwtService;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codev/my-page")
public class CoMyPageController extends JwtController {
    private final CoMyPageServiceImpl coMyPageService;
    public CoMyPageController(JwtTokenProvider jwtTokenProvider, JwtService jwtService, CoMyPageServiceImpl coMyPagePortfolioService) {
        super(jwtTokenProvider, jwtService);
        this.coMyPageService = coMyPagePortfolioService;
    }

    @ResponseBody
    @PostMapping(value = "/portfolio")
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
        this.coMyPageService.insertCoPortfolio(coPortfolio, co_languagesList, co_linksList);
        return null;
    }

    @GetMapping("/portfolio/{coPortfolioId}")
    public CoDevResponse getPortfolio(HttpServletRequest request, @PathVariable("coPortfolioId") long co_portfolioId, String email)throws Exception{
        String co_email = getCoUserEmail(request);
        return coMyPageService.getCoPortfolio(co_portfolioId,co_email);
    }

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

    @DeleteMapping("/portfolio/{coPortfolioId}")
    public CoDevResponse deletePortfolio(HttpServletRequest request, @PathVariable("coPortfolioId") long coPortfolioId) throws Exception {
        String co_email = getCoUserEmail(request);
        return this.coMyPageService.deletePortfolio(co_email, coPortfolioId);
    }

    @GetMapping("/hearts/studies")
    public CoDevResponse getHeartOfStudies(HttpServletRequest request) throws Exception{
        String co_email = getCoUserEmail(request);
        return this.coMyPageService.getHeartOfStudies(co_email);
    }

    @GetMapping("/hearts/projects")
    public CoDevResponse getHeartOfProjects(HttpServletRequest request) throws Exception{
        String co_email = getCoUserEmail(request);
        return this.coMyPageService.getHeartOfProjects(co_email);
    }

    @GetMapping("/portfolioList")
    public CoDevResponse getPortfolioList(HttpServletRequest request) throws Exception {
        return this.coMyPageService.getCoPortfolios(getCoUserEmail(request));
    }
}
