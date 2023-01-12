package com.codevumc.codev_backend.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    //인증번호 생성
    private final String ePw = createKey();

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String to)throws MessagingException, UnsupportedEncodingException {
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
        msgg.append(ePw + "</strong><div><br/> ");
        msgg.append("</div>");


        message.setText(msgg.toString(), "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(id,"CoDev팀")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    // 인증코드 만들기
    public static String createKey() {
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
    public String sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
    }
}
