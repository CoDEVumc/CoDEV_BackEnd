package com.codevumc.codev_backend.service.firebase;

import com.codevumc.codev_backend.domain.firebase.FCM;
import com.codevumc.codev_backend.domain.firebase.FcmMessage;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoUserMapper;
import com.codevumc.codev_backend.service.ResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class FirebaseCloudMessageServiceImpl extends ResponseService implements FirebaseCloudMessageService{
    public static final String LOGO = "http://semtle.catholic.ac.kr:8080/image?name=Codev_logo20230214013923.png";
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/codev-14c11/messages:send";
    private final ObjectMapper objectMapper;
    private final CoUserMapper coUserMapper;

    @Override
    public CoDevResponse sendMessageTo(HttpServletRequest request,  FCM fcm) {
        try {
            List<String> FCMToken = getFCMToken(fcm);
            for(String token : FCMToken) {
                sendMessage(token, fcm.getTitle(), fcm.getBody());
            }
            return setResponse(200, "message", "푸쉬알림을 보냈습니다.");
        }catch (Exception e) {
            request.setAttribute("exception", "FAILEDPUSHNOTIFICATION");
            throw new AuthenticationCustomException(ErrorCode.FAILEDPUSHNOTIFICATION);
        }

    }

    private List<String> getFCMToken(FCM fcm) {
        Map<String, Object> FCMDto = new HashMap<>();
        FCMDto.put("co_emails", fcm.getCo_emails());

        return this.coUserMapper.getFCMToken(FCMDto);
    }

    private void sendMessage(String targetToken, String title, String body) throws IOException{
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }
}
