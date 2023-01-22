package com.codevumc.codev_backend.service.co_studyheart;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoStudyHeartService {
    CoDevResponse changeHeart(String co_email, Long co_studyId);
}
