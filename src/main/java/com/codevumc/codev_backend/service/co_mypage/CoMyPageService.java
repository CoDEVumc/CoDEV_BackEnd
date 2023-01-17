package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoMyPageService {
    public CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio);
}
