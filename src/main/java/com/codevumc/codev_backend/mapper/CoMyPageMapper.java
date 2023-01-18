package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CoMyPageMapper {
    void updateCoPortfolio(CoPortfolio coPortfolio);
    void updateCoLaguageOfPortfolio(@Param("co_portfolioId") long co_portfolioId, @Param("co_laugagueId") long co_laugageId);
    void updateCoLinkOfPortfolio(Map<String, Object> coPortfolioDto);
}
