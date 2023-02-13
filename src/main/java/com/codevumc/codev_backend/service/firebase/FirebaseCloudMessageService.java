package com.codevumc.codev_backend.service.firebase;

import com.codevumc.codev_backend.domain.firebase.FCM;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface FirebaseCloudMessageService {
    CoDevResponse sendMessageTo(HttpServletRequest request, FCM fcm) throws IOException;
}
