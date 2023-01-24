package com.codevumc.codev_backend.service.co_projectrecruit;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoProjectRecruitService {
    CoDevResponse cancelCoRecruitOfProject(String co_email, long co_projectId);
}
