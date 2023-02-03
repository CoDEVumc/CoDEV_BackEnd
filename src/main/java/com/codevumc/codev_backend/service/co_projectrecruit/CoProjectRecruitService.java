package com.codevumc.codev_backend.service.co_projectrecruit;

import com.codevumc.codev_backend.domain.CoApplicantInfo;
import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoRecruitOfProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

import java.util.List;

public interface CoProjectRecruitService {
    CoDevResponse insertCoRecruitOfProject(CoRecruitOfProject coRecruitOfProject);
    CoDevResponse cancelCoRecruitOfProject(String co_email, long co_projectId);
    CoDevResponse getCoApplicantsOfProject(String co_email, long co_projectId, String co_part);
    CoDevResponse closeCoProjectDeadLine(String co_email, Long co_projectId, CoProject co_applicantsList);
    CoDevResponse getCoPortfolioOfApplicant(String co_email, long co_projectId, long co_portfolioId);
    CoDevResponse saveCoApplicantsTemporarily(String co_email, long co_projectId, List<CoApplicantInfo> coApplicantInfos);
}
