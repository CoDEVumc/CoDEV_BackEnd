package com.codevumc.codev_backend.mapper;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoMyPageMapper {
    void updateCoPortfolio(CoPortfolio coPortfolio);
    void updateCoLaguageOfPortfolio(CoPortfolio coPortfolio);
    void updateCoLinkOfPortfolio(CoPortfolio coPortfolio);
}
