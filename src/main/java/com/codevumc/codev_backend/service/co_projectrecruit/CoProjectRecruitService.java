package com.codevumc.codev_backend.service.co_projectrecruit;

import com.codevumc.codev_backend.domain.CoRecruitOfProject;
import com.codevumc.codev_backend.domain.CoTempSaveApplicants;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;

public interface CoProjectRecruitService {
    CoDevResponse insertCoRecruitOfProject(CoRecruitOfProject coRecruitOfProject);
    CoDevResponse cancelCoRecruitOfProject(CoRecruitOfProject coRecruitOfProject);
    CoDevResponse getCoApplicantsOfProject(String co_email, long co_projectId, String co_part);
    CoDevResponse closeCoProjectDeadLine(String co_email, Long co_projectId, JSONArray co_applicantList);
    CoDevResponse getCoPortfolioOfApplicant(String co_email, long co_projectId, long co_portfolioId);
    CoDevResponse saveCoApplicantsTemporarily(String co_email, long co_projectId, CoTempSaveApplicants coTempSaveApplicants);
}
