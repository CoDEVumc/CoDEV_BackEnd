package com.codevumc.codev_backend.snslogin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Controller
public class GoogleApi {

    final static String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";

    public HashMap<String, Object> getUserInfo(String id_token) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String url =  GOOGLE_USERINFO_REQUEST_URL+"?access_token="+id_token;
        try {
            HttpURLConnection con;
            URL googleURL = new URL(url);
            con = (HttpURLConnection) googleURL.openConnection();

            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader in = new BufferedReader(isr);

            String line = "";
            StringBuffer result = new StringBuffer();

            while ((line = in.readLine()) != null)
                result.append(line);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());
            String co_password = null;
            String co_email = null;
            int responseCode = con.getResponseCode();
            if(responseCode == 200) {
                co_password = element.getAsJsonObject().get("id").getAsString();
                co_email = element.getAsJsonObject().get("email").getAsString();
                userInfo.put("co_password", co_password);
                userInfo.put("co_email", co_email);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return userInfo;

    }

}
