package com.codevumc.codev_backend.service.co_studyrecruit;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoStudyRecruitService {
    CoDevResponse cancelRecruitStudy(String co_email, long co_studyId);
}
