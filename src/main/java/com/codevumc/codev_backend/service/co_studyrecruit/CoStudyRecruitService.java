package com.codevumc.codev_backend.service.co_studyrecruit;

import com.codevumc.codev_backend.domain.CoRecruitOfStudy;
import com.codevumc.codev_backend.domain.CoStudy;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;


public interface CoStudyRecruitService {
    CoDevResponse cancelRecruitStudy(String co_email, long co_studyId);
    CoDevResponse submitCoStudy(CoRecruitOfStudy coRecruitOfStudy);
    CoDevResponse completeCoStudyRecruitment(String co_email, long co_studyId, CoStudy co_applicantList);
    CoDevResponse getCoApplicantsOfStudy(String co_email, long co_studyId, String co_part);
    CoDevResponse getCoPortfolioOfApplicant(String co_email,long co_studyId,long co_portfolioId);
}
