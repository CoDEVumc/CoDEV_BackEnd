package com.codevumc.codev_backend.service.co_portfolio;

import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoPortfolioServiceImpl extends ResponseService implements CoPortfolioService {
    private final CoPortfolioMapper coPortfolioMapper;
}

