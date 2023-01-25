package com.codevumc.codev_backend.service.co_mypage;

import com.codevumc.codev_backend.domain.CoPortfolio;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.mapper.CoMyPageMapper;
import com.codevumc.codev_backend.mapper.CoMyPagePortfolioMapper;
import com.codevumc.codev_backend.service.ResponseService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class CoMyPageServiceImpl extends ResponseService implements CoMyPageService {
    private final CoMyPagePortfolioMapper coMyPagePortfolioMapper;
    private final CoMyPageMapper coMyPageMapper;

    @Override
    public CoDevResponse insertCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links) {
        try {
            this.coMyPagePortfolioMapper.insertCoPortfolio(coPortfolio);
            for (Object co_language : co_languages) {
                long co_languageId = (long) co_language;
                this.coMyPagePortfolioMapper.insertCoLanguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_languageId);
            }
            for (Object co_plink : co_links) {
                String co_link = (String) co_plink;
                this.coMyPagePortfolioMapper.insertCoLinkOfPortfolio(coPortfolio.getCo_portfolioId(), co_link);
            }
            return setResponse(200, "message", "포트폴리오가 작성되었습니다.");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public CoDevResponse getCoPortfolio(long co_portfolioId,String co_email) {
        Map<String, Object> coPortfolioDto = new HashMap<>();
        coPortfolioDto.put("co_email",co_email);
        coPortfolioDto.put("co_portfolioId",co_portfolioId);
        try {
            Optional<CoPortfolio> coPortfolio = coMyPagePortfolioMapper.getCoPortfolio(coPortfolioDto);
            if (coPortfolio.isPresent()){
                return setResponse(200,"Complete", coPortfolio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse updateCoPortfolio(CoPortfolio coPortfolio, JSONArray co_languages, JSONArray co_links) {
        Map<String,Object> coPortfolioDto = new HashMap<>();
        coPortfolioDto.put("co_email",coPortfolio.getCo_email());
        coPortfolioDto.put("co_portfolioId",coPortfolio.getCo_portfolioId());
        try{
            Optional<CoPortfolio> coPortfolio1 = coMyPagePortfolioMapper.getCoPortfolio(coPortfolioDto);
            if(coPortfolio1.isPresent()){
                if(coMyPagePortfolioMapper.updateCoPortfolio(coPortfolio)){
                    for (Object co_language : co_languages) {
                        long co_languageId = (long) co_language;
                        coMyPagePortfolioMapper.insertCoLanguageOfPortfolio(coPortfolio.getCo_portfolioId(), co_languageId);
                    }
                    for (Object co_plink : co_links) {
                        String co_link = (String) co_plink;
                        coMyPagePortfolioMapper.insertCoLinkOfPortfolio(coPortfolio.getCo_portfolioId(), co_link);
                    }
                    return setResponse(200,"Complete","수정 완료되었습니다.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse deletePortfolio(String co_email, long co_portfolioId) {
        Map<String, Object> coPortfolioDto = new HashMap<>();
        coPortfolioDto.put("co_email", co_email);
        coPortfolioDto.put("co_portfolioId", co_portfolioId);
        try {
            Optional<CoPortfolio> coPortfolio = coMyPagePortfolioMapper.getCoPortfolio(coPortfolioDto);
            if (coPortfolio.isPresent()) {
                this.coMyPagePortfolioMapper.deletePortfolio(coPortfolioDto);
                return setResponse(200, "Complete", "삭제되었습니다.");
            } else {
                return setResponse(403, "Forbidden", "수정 권한이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getHeartOfStudies(String co_email) {
        try{
            List<CoStudy> coHeartsOfStudy = coMyPageMapper.getCoHeartsOfStudy(co_email);
            return setResponse(200, "Complete", coHeartsOfStudy);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getHeartOfProjects(String co_email) {
        try {
            List<CoProject> coHeartOfProjects = coMyPageMapper.getCoHeartsOfProject(co_email);
            return setResponse(200, "Complete", coHeartOfProjects);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public CoDevResponse getCoPortfolios(String co_email) {
        try {
            List<CoPortfolio> coPortfolios = this.coMyPageMapper.getPortfolioByCo_email(co_email);
            Map<String, String> map = this.coMyPageMapper.getUserProfile(co_email);
            map.put("co_email", map.get("co_email"));
            map.put("co_name", map.get("co_name"));
            map.put("co_nickName", map.get("co_nickName"));
            map.put("profileImg", map.get("profileImg"));
            CoDevResponse result = setResponse(200, "Complete", map);
            if(coPortfolios != null) {
                addResponse("Portfolio", coPortfolios);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getParticipateStudies(String co_email) {
        try{
            List<CoStudy> coStudies = this.coMyPageMapper.getParticipateOfStudies(co_email);
            return setResponse(200,"Complete",coStudies);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getParticipateProjects(String co_email) {
        try{
            List<CoProject> coProjects = this.coMyPageMapper.getParticipateOfProjects(co_email);
            return setResponse(200,"Complete",coProjects);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getRecruitProjects(String co_email) {
        try{
            List<CoProject> coProjects = this.coMyPageMapper.getRecruitOfProjects(co_email);
            return setResponse(200,"Complete",coProjects);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoDevResponse getRecruitStudies(String co_email) {
        try{
            List<CoStudy> coStudies = this.coMyPageMapper.getRecruitOfStudies(co_email);
            return setResponse(200,"Complete",coStudies);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

