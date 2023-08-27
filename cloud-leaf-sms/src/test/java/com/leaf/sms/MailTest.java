package com.leaf.sms;

import com.leaf.mail.service.IMailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;


@SpringBootTest
public class MailTest {

    /*@Autowired
    private MailSender mailSender;*/

    @Test
    public void test() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("This is a spring-boot test email!");
        message.setFrom("xxxx@163.com");
        message.setTo("ooooo@qq.com");

        message.setSentDate(new Date());
        message.setText("This is mail content!");

        System.out.println(message.toString());

//        mailSender.send(message);
    }

    @Autowired
    IMailService mailService;
    @Test
    public void sendMailTest(){
        mailService.send("1223752287@qq.com","12345");
    }

    @Test
    public void testStr(){
        String s= "daabcbaabcbc";

        System.out.println(s.contains("abc"));
        String abc = s.replaceFirst("abc", "");
        System.out.println(abc);
    }
}
