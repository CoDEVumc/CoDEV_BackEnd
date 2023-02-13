package com.codevumc.codev_backend.service.co_studyrecruit;

import com.codevumc.codev_backend.domain.CoRecruitOfStudy;
import com.codevumc.codev_backend.domain.CoTempSaveApplicants;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import org.json.simple.JSONArray;


public interface CoStudyRecruitService {
    CoDevResponse cancelRecruitStudy(CoRecruitOfStudy coRecruitOfStudy);
    CoDevResponse submitCoStudy(CoRecruitOfStudy coRecruitOfStudy);
    CoDevResponse completeCoStudyRecruitment(String co_email, long co_studyId, JSONArray co_applicantList);
    CoDevResponse getCoApplicantsOfStudy(String co_email, long co_studyId, String co_part);
    CoDevResponse getCoPortfolioOfApplicant(String co_email,long co_studyId,long co_portfolioId);
    CoDevResponse saveCoApplicantsTemporarily(String co_email, long co_studyId, CoTempSaveApplicants coTempSaveApplicants);
}
