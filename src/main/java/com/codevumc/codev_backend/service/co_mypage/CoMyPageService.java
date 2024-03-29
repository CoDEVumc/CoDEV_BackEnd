package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;

public interface CoMyPageService {
    CoDevResponse insertCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links);
    CoDevResponse getCoPortfolio(long co_portfolioId, String co_email);
    CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links);
    CoDevResponse deletePortfolio(String co_email, long co_portfolioId);
    CoDevResponse getHeartOfStudies(String co_email);
    CoDevResponse getHeartOfProjects(String co_email);
    CoDevResponse getCoPortfolios(String co_email);
    CoDevResponse getRecruitProjects(String co_email);
    CoDevResponse getRecruitStudies(String co_email);
    CoDevResponse getParticipateStudies(String coUserEmail);
    CoDevResponse getParticipateProjects(String co_email);
    CoDevResponse getMark(String co_email);
    CoDevResponse getMyStudies(String co_email);
    CoDevResponse getMyProjects(String co_email);
}
