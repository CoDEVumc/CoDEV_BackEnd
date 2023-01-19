package com.codevumc.codev_backend.service.co_portfolio;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CoPortfolioServiceImpl extends ResponseService implements CoPortfolioService {
    private final CoPortfolioMapper coPortfolioMapper;

    @Override
    public CoDevResponse getCoPortfolio(long co_portfolioId) {
        try {
            Optional<CoPortfolio> coPortfolio = coPortfolioMapper.getCoPortfolio(co_portfolioId);
            return setResponse(200, "Complete", coPortfolio);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

