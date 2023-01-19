package com.codevumc.codev_backend.service.co_portfolio;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoPortfolioService {
    CoDevResponse getCoPortfolio(long co_portfolioId);
}
