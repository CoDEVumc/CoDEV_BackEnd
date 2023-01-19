package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

@Mapper
public interface CoPortfolioMapper {
    void insertCoPortfolio(CoPortfolio coPortfolio);
    void insertCoLanguageOfPortfolio(@Param("co_portfolioId") long co_portfolioId, @Param("co_languageId") long co_languageId);
    void insertCoLinkOfPortfolio(@Param("co_portfolioId") long co_portfolioId, @Param("co_link") String co_link);
    Optional<CoPortfolio> getCoPortfolio(Long co_portfolioId);
}
