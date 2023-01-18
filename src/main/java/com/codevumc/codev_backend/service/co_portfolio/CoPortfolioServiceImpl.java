package com.codevumc.codev_backend.service.co_portfolio;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class CoPortfolioServiceImpl extends ResponseService implements CoPortfolioService {
    private final CoPortfolioMapper coPortfolioMapper;

    @Override
    public void insertCoPortfolio(CoPortfolio coPortfolio, JSONArray co_portfolioLanguages, JSONArray co_portfolioLinks) {
        Map<String, Object> coPortfolioDto = new HashMap<>();
        this.coPortfolioMapper.insertCoPortfolio(coPortfolio);

        for (Object co_portfolioLanguage : co_portfolioLanguages) {
            long co_languageId = (long) co_portfolioLanguage;
            this.coPortfolioMapper.insertCoLanguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_languageId);
        }

        for (Object co_portfolioLink : co_portfolioLinks) {
            String co_link = (String) co_portfolioLink;
            this.coPortfolioMapper.insertCoLinkOfPortfolio(coPortfolio.getCo_portfolioId(), co_link);
        }
    }
}

