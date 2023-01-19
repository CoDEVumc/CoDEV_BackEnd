package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CoMyPageMapper {
    boolean updateCoPortfolio(CoPortfolio coPortfolio);
    boolean updateCoLanguageOfPortfolio(@Param("co_portfolioId") long co_portfolioId, @Param("co_languageId") long co_languageId);
    boolean updateCoLinkOfPortfolio(@Param("co_portfolioId") long co_portfolioId, @Param("co_link") String co_link);
}
