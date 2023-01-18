package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoMyPageMapper;
import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
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
    public CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio, JSONArray co_portfoliolinks, JSONArray co_portfolioLaguages) {

        try {
            //포폴 테이블에 있는 값이 존재할경우 업데이트
            Optional<CoPortfolio> coPortfolio1 = Optional.ofNullable(coPortfolioMapper.getCoPortfolio(coPortfolio.getCo_portfolioId()));
            if(coPortfolio1.isPresent()){

                Map<String, Object> coPortfolioDto = new HashMap<>();
                this.coMyPageMapper.updateCoPortfolio(coPortfolio);

                for (Object co_portfoliolaguage : co_portfolioLaguages){
                    long co_laugageId = (long) co_portfoliolaguage;
                    this.coMyPageMapper.updateCoLaguageOfPortfolio(coPortfolio.getCo_portfolioId(),co_laugageId);
                }
                JSONObject jsonObj;
                for (Object co_portfoliolink : co_portfoliolinks){
                    jsonObj = (JSONObject) co_portfoliolink;
                    coPortfolioDto.put("co_portfolioId",coPortfolio.getCo_portfolioId());
                    coPortfolioDto.put("co_link",jsonObj.get("co_link").toString());
                    this.coMyPageMapper.updateCoLinkOfPortfolio(coPortfolioDto);
                }
                }
            return setResponse(200, "success", coPortfolio);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }

}
