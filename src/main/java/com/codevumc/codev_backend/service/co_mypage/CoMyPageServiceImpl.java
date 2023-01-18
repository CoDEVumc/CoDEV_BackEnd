package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoMyPageMapper;
import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
    public void updateCoPortfolio(CoPortfolio coPortfolio, JSONArray co_portfoliolinks, JSONArray co_portfolioLanguages) {


        Map<String, Object> coPortfolioDto = new HashMap<>();
        this.coMyPageMapper.updateCoPortfolio(coPortfolio);

                /*
                for (int i = 0; i < co_portfolioLanguages.length(); ++i) {
                    JSONObject co_portfoliolaguage = co_portfolioLanguages.getJSONObject(i);
                    long co_languageId = co_portfoliolaguage.getLong("co_portfolioLanguages");
                    this.coMyPageMapper.updateCoLaguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_languageId);

                }


                for (int i = 0; i < co_portfoliolinks.length(); ++i) {
                    JSONObject co_portfoliolink = co_portfoliolinks.getJSONObject(i);
                    coPortfolioDto.put("co_portfolioId", coPortfolio.getCo_portfolioId());
                    coPortfolioDto.put("co_link", co_portfoliolink.get("co_link").toString());
                    this.coMyPageMapper.updateCoLinkOfPortfolio(coPortfolioDto);

                }
                */

        for (Object co_portfoliolaguage : co_portfolioLanguages) {
            long co_laugageId = (long) co_portfoliolaguage;
            this.coMyPageMapper.updateCoLaguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_laugageId);
        }

        JSONObject jsonObj;
        for (Object co_portfoliolink : co_portfoliolinks) {
            jsonObj = (JSONObject) co_portfoliolink;
            coPortfolioDto.put("co_portfolioId", coPortfolio.getCo_portfolioId());
            coPortfolioDto.put("co_link", jsonObj.get("co_link").toString());
            this.coMyPageMapper.updateCoLinkOfPortfolio(coPortfolioDto);
        }
/*
        try {
            //포폴 테이블에 있는 값이 존재할경우 업데이트
            Optional<CoPortfolio> coPortfolio1 = Optional.ofNullable(coPortfolioMapper.getCoPortfolio(coPortfolio.getCo_portfolioId()));
            if(coPortfolio1.isPresent()) {



            }
            return setResponse(200, "success", coPortfolio);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }
    */
    }
}
