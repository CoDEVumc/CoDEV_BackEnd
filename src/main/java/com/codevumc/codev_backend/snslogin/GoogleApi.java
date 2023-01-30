package com.codevumc.codev_backend.snslogin;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;

@Component
public class GoogleApi {
    final static String CLIENT_ID = "242179001869-snj41p9nfda06ves81efofk8b0g6q9t2.apps.googleusercontent.com";

    public HashMap<String, Object> getUserInfo(String id_token) {
        HashMap<String, Object> userInfo = new HashMap<>();
        HttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(id_token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                userInfo.put("co_email", payload.getEmail());
                userInfo.put("co_password", payload.getSubject());
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfo;

    }

}
