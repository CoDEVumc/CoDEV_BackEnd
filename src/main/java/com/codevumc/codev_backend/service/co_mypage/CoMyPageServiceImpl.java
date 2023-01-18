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

    @Override
    public void updateCoPortfolio(CoPortfolio coPortfolio,JSONArray co_languages, JSONArray co_links) {
        this.coMyPageMapper.updateCoPortfolio(coPortfolio);
        for (Object co_language : co_languages) {
            long co_languageId = (long) co_language;
            this.coMyPageMapper.updateCoLanguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_languageId);
        }
        for (Object co_plink : co_links) {
            String co_link = (String) co_plink;
            this.coMyPageMapper.updateCoLinkOfPortfolio(coPortfolio.getCo_portfolioId(),co_link);
        }
    }

}
