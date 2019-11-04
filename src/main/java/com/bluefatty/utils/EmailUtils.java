package com.bluefatty.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 邮箱工具
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-04
 */
@Component
public class EmailUtils {
    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.subject}")
    private String subject;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo("912094062@qq.com");
        smm.setSubject(subject);
        smm.setText("123456");
        javaMailSender.send(smm);
    }
}
