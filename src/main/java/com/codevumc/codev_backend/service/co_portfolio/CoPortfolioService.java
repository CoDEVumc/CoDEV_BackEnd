package com.codevumc.codev_backend.service.co_portfolio;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.json.simple.JSONArray;

public interface CoPortfolioService {
    void insertCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links);
}
