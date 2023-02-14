package com.codevumc.codev_backend.controller.firebase;


import com.codevumc.codev_backend.domain.firebase.FCM;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.service.firebase.FirebaseCloudMessageServiceImpl;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/codev/notification")
public class FirebaseController {
    private final FirebaseCloudMessageServiceImpl firebaseCloudMessageService;

    public FirebaseController(FirebaseCloudMessageServiceImpl firebaseCloudMessageService) {
        this.firebaseCloudMessageService = firebaseCloudMessageService;
    }

    @GetMapping("/push")
    public CoDevResponse pushNotification(HttpServletRequest request, @RequestBody FCM fcm) {
        return firebaseCloudMessageService.sendMessageAll(request, fcm);
    }

    private JSONArray getJSONArray(Object object) throws Exception{
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(object);
        return (JSONArray) parser.parse(jsonArray);
    }

}
