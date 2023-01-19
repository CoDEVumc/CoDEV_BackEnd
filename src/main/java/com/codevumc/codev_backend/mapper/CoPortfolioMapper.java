package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface CoPortfolioMapper {

    Optional<CoPortfolio> getCoPortfolio(Long co_portfolioId);
}
