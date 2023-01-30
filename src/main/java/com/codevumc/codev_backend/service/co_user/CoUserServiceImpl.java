package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.errorhandler.CoDevResponse;
import com.codevumc.codev_backend.errorhandler.AuthenticationCustomException;
import com.codevumc.codev_backend.errorhandler.ErrorCode;
import com.codevumc.codev_backend.mapper.CoUserMapper;
import com.codevumc.codev_backend.mapper.TokenMapper;
import com.codevumc.codev_backend.service.ResponseService;
import com.codevumc.codev_backend.snslogin.GitHubApi;
import com.codevumc.codev_backend.snslogin.GoogleApi;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.CacheableOperation;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class CoUserServiceImpl extends ResponseService implements CoUserService, UserDetailsService {
    private final CoUserMapper coUserMapper;
    private final TokenMapper tokenMapper;
    private final JavaMailSender javaMailSender;

    @Autowired
    public CoUserServiceImpl(CoUserMapper coUserMapper, TokenMapper tokenMapper, JavaMailSender javaMailSender) {
        this.coUserMapper = coUserMapper;
        this.tokenMapper = tokenMapper;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String co_email) throws UsernameNotFoundException {
        Optional<CoUser> coUser = coUserMapper.findByEmail(co_email);

        if(coUser.isPresent()) return coUser.get();
        else throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
    }

    @Override
    public CoDevResponse findALlUser(String email) {
        try {

            return setResponse(200, "Success", email);
        }catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.ForbiddenException);
        }

    }

    @Override
    public CoDevResponse signUpCoUser(CoUser coUser) {
        try {
            coUserMapper.insertCoUser(coUser);
            return setResponse(200, "message", "회원가입이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.FAILEDSIGNUP);
        }
    }

    public void updateProfileImg(String profileImg, String co_email) {
        coUserMapper.updateProfileImg(profileImg, co_email);
    }

    @Override
    public CoDevResponse snsLoginMessage(CoUser coUser) {
        CoDevResponse result = null;
        try {
            result = setResponse(505, "message", "/join 으로 이동하여 추가정보를 입력해주세요.");
            addResponse("co_email", coUser.getCo_email());
            addResponse("co_password", coUser.getCo_password());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public boolean isSignup(String co_email) {
        Optional<CoUser> coUser = this.coUserMapper.findByEmail(co_email);

        return coUser.isPresent();
    }

    @Override
    public MimeMessage createMessage(String to, String randomNumber) throws MessagingException, UnsupportedEncodingException {
        MimeMessage  message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("CoDev 회원가입 인증 코드"); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        StringBuilder msgg = new StringBuilder();
        msgg.append("<div style='margin:0 auto;'>");
        msgg.append("<h1> 안녕하세요</h1>");
        msgg.append("<h1> \"CoDEV : 같이 개발해요!\" 입니다.</h1>");
        msgg.append("<br>");
        msgg.append("<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>");
        msgg.append("<br>");
        msgg.append("<p>저희 서비스를 이용해 주셔서 감사합니다.<p>");
        msgg.append("<br>");
        msgg.append("<div align='center' style='border:1px solid black; font-family:verdana'; margin:0 auto;>");
        msgg.append("<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>");
        msgg.append("<div style='font-size:130%'>");
        msgg.append("CODE : <strong>");
        msgg.append(randomNumber + "</strong><div><br/> ");
        msgg.append("</div>");


        message.setText(msgg.toString(), "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress("co_dev@naver.com","CoDev팀")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    // 인증코드 만들기
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    /*
       메일 발송
       sendSimpleMessage의 매개변수로 들어온 to는 인증번호를 받을 메일주소
       MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
       bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
    */
    @Override
    public CoDevResponse sendSimpleMessage(String to) throws Exception {
        String randomNumber = createKey();
        MimeMessage message = createMessage(to, randomNumber);
        try{
            if(isValidEmail(to)) {
                javaMailSender.send(message); // 메일 발송
                return setResponse(200, "success", randomNumber);
            }else
                return setResponse(400, "error", "이메일 형식이 아닙니다.");

        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

    }

    private boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true;
        }
        return err;
    }

    private Map<String, Object> connectLogin(CoUser coUser, String userAgent, String pw) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            System.out.println("@@@");
            URL url = new URL("http://localhost:8080/codev/user/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", userAgent);

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            System.out.println("coUSer toString() " + coUser.getLoginJson(coUser.getUsername(), pw));
            writer.write(coUser.getLoginJson(coUser.getUsername(), pw));
            writer.close();
            os.flush();
            os.close();

            JsonParser parser = null;
            JsonElement element = null;
            int responseCode = conn.getResponseCode();
            if(responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                StringBuffer result = new StringBuffer();

                while ((line = br.readLine()) != null)
                    result.append(line);

                parser = new JsonParser();
                element = parser.parse(result.toString());
                conn.disconnect();
                resultMap.put("accessToken", element.getAsJsonObject().get("result").getAsJsonObject().get("accessToken").getAsString());
                resultMap.put("key", element.getAsJsonObject().get("result").getAsJsonObject().get("key").getAsString());
                resultMap.put("refreshToken", element.getAsJsonObject().get("result").getAsJsonObject().get("refreshToken").getAsString());
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCustomException(ErrorCode.UsernameOrPasswordNotFoundException);
        }
    }
}
