package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoStudyService {
    CoDevResponse getCoStudy(long co_studyId);
}
