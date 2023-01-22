package com.codevumc.codev_backend.service.co_projectheart;

import com.codevumc.codev_backend.errorhandler.CoDevResponse;

public interface CoProjectHeartService {

    CoDevResponse changeHeart(String co_email, Long co_projectId);
}
