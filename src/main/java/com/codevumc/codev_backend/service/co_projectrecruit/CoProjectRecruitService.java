package com.codevumc.codev_backend.service.co_projectrecruit;

import com.codevumc.codev_backend.domain.CoProject;
import com.codevumc.codev_backend.domain.CoRecruitOfProject;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoProjectRecruitService {
    CoDevResponse insertCoRecruitOfProject(CoRecruitOfProject coRecruitOfProject);
    CoDevResponse cancelCoRecruitOfProject(String co_email, long co_projectId);
    CoDevResponse closeCoProjectDeadLine(String co_email, Long co_projectId, CoProject co_applicantsList);
}
