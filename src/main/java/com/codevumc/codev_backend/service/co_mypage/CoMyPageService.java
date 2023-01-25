package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import org.json.simple.JSONArray;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoMyPageService {
    CoDevResponse insertCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links);
    CoDevResponse getCoPortfolio(long co_portfolioId, String co_email);
    CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links);
    CoDevResponse deletePortfolio(String co_email, long co_portfolioId);
    CoDevResponse getHeartOfStudies(String co_email);
    CoDevResponse getHeartOfProjects(String co_email);
    CoDevResponse getCoPortfolios(String co_email);
    CoDevResponse getParticipateProjects(String co_email);
    CoDevResponse getParticipateStudies(String co_email);
}
