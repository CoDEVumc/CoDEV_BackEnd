package com.codevumc.codev_backend.service.co_portfolio;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.json.simple.JSONArray;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoPortfolioService {
    void insertCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links);
    CoDevResponse getCoPortfolio(long co_portfolioId, String co_email);
    CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links);
}
