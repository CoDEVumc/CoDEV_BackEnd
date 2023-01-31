package com.codevumc.codev_backend.service.co_projectrecruit;

import com.codevumc.codev_backend.domain.*;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoProjectRecruitService {
    CoDevResponse insertCoRecruitOfProject(CoRecruitOfProject coRecruitOfProject);
    CoDevResponse cancelCoRecruitOfProject(String co_email, long co_projectId);
    CoDevResponse getCoApplicantsOfProject(String co_email, long co_projectId, String co_part);
    CoDevResponse closeCoProjectDeadLine(String co_email, Long co_projectId, CoProject co_applicantsList);
}
