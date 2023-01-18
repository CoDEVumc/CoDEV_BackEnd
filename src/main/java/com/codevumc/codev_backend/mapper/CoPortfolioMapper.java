package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface CoPortfolioMapper {

    CoPortfolio getCoPortfolio(Long co_portfolioId);
}
