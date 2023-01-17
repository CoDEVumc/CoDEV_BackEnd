package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoMyPageMapper;
import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoMyPageServiceImpl extends ResponseService implements CoMyPageService{
    private final CoMyPageMapper coMyPageMapper;
    private final CoPortfolioMapper coPortfolioMapper;


    @Override
    public CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio) {
        try {
            coPortfolioMapper.updateCoPortfolio(coPortfolio);
            return setResponse(200, "success", coPortfolio);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }

}
