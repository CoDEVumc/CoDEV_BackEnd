package com.codevumc.codev_backend.service.co_study;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoStudyService {
    CoDevResponse getCoStudies(String co_email, String co_locationTag, String co_partTag, String co_keyword, String co_processTag);
}
