package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoHeartOfProject;
import com.codevumc.codev_backend.domain.CoPortfolio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface CoPortfolioMapper {
    Optional<CoPortfolio> getCoPortfolio(@Param("co_portfolioId") long co_portfolioId);
}
