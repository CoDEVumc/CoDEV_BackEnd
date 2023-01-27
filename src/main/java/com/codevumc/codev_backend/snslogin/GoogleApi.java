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

    final static String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
    final static String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";


    String clientId = "413806176191-5ubglt67tr3gdl7u45l4qmepgcj5h71k.apps.googleusercontent.com";
    String clientSecret = "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";

    public String getAccessToken(String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";

        try {
            URL url = new URL(GOOGLE_TOKEN_BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("client_id=" + clientId);
            sb.append("&client_secret=" + clientSecret);
            sb.append("&code=" + authorize_code);
            sb.append("&grant_type=authorization_code");
            sb.append("&redirect_uri=http://localhost:8080/codev/user/google/login");
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuffer result = new StringBuffer();

            while ((line = br.readLine()) != null)
                result.append(line);
            //Gson 라이브러르에 포함된 클래스로 json파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_Token;
    }


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
