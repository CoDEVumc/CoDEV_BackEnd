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

        for (Object co_portfoliolaguage : co_portfolioLanguages) {
            long co_languageId = (long) co_portfoliolaguage;
            this.coMyPageMapper.updateCoLanguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_languageId);
        }

        for (Object co_portfoliolink : co_portfoliolinks) {
            String co_link = (String)co_portfoliolink;
            this.coMyPageMapper.updateCoLinkOfPortfolio(coPortfolio.getCo_portfolioId(),co_link);
        }

    }
}
