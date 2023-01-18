package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoMyPageMapper;
import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CoMyPageServiceImpl extends ResponseService implements CoMyPageService{
    private final CoMyPageMapper coMyPageMapper;
    private final CoPortfolioMapper coPortfolioMapper;


    @Override
    public CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio) {

        try {
            //포폴 테이블에 있는 값이 존재할경우 업데이트
            Optional<CoPortfolio> coPortfolio1 = Optional.ofNullable(coPortfolioMapper.getCoPortfolio(coPortfolio.getCo_portfolioId()));
            if(coPortfolio1.isPresent()){
                //if 본인이 맞을경우
                coMyPageMapper.updateCoPortfolio(coPortfolio);
                coMyPageMapper.updateCoLaguageOfPortfolio(coPortfolio);
                coMyPageMapper.updateCoLinkOfPortfolio(coPortfolio);
                }
            return setResponse(200, "success", coPortfolio);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }

}
