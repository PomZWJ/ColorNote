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

    public void sendEmail(String to,String code){
        System.out.println(code);
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(from);
        smm.setTo(to);
        smm.setSubject(subject);
        smm.setText(code);
        javaMailSender.send(smm);
    }
}
