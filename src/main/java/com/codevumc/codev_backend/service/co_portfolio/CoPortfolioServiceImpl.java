package com.codevumc.codev_backend.service.co_portfolio;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CoPortfolioServiceImpl extends ResponseService implements CoPortfolioService {
    private final CoPortfolioMapper coPortfolioMapper;

    @Override
    public void insertCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links) {
        this.coPortfolioMapper.insertCoPortfolio(coPortfolio);
        for (Object co_language : co_languages) {
            long co_languageId = (long) co_language;
            this.coPortfolioMapper.insertCoLanguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_languageId);
        }
        for (Object co_plink : co_links) {
            String co_link = (String) co_plink;
            this.coPortfolioMapper.insertCoLinkOfPortfolio(coPortfolio.getCo_portfolioId(), co_link);
        }
    }

    @Override
    public CoDevResponse getCoPortfolio(long co_portfolioId) {
        try {
            Optional<CoPortfolio> coPortfolio = coPortfolioMapper.getCoPortfolio(co_portfolioId);
            if (coPortfolio.isPresent()){
                return setResponse(200,"Complete",coPortfolio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

