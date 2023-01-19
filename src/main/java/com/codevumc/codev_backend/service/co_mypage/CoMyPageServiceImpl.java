package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoMyPageMapper;
import com.codevumc.codev_backend.mapper.CoPortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
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
    public CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links) {
        try{
            Optional<CoPortfolio> coPortfolio1 = coPortfolioMapper.getCoPortfolio(coPortfolio.getCo_portfolioId());
            if(coPortfolio1.isPresent()){
                if(coMyPageMapper.updateCoPortfolio(coPortfolio)){
                    for (Object co_language : co_languages) {
                        long co_languageId = (long) co_language;
                        return this.coPortfolioMapper.insertCoLanguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_languageId)?setResponse(200,"success","수정완료") : setResponse(403,"Forbidden","수정권한이 없습니다.");
                    }
                    for (Object co_plink : co_links) {
                        String co_link = (String) co_plink;
                        return this.coPortfolioMapper.insertCoLinkOfPortfolio(coPortfolio.getCo_portfolioId(), co_link)?setResponse(200,"success","수정완료") : setResponse(403,"Forbidden","수정권한이 없습니다.");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
