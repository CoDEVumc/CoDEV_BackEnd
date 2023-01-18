package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoPortfolioMapper {
    void insertCoPortfolio(CoPortfolio coPortfolio);

    void insertCoLanguageOfPortfolio(long co_portfolioId, long co_languageId);

    void insertCoLinkOfPortfolio(long co_portfolioId, String co_link);
}
