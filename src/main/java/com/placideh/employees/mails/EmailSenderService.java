package com.placideh.employees.mails;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendCommunicationEmail(String toEmail,String body,String subject){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("jumpmantest97@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("message sent");
    }
}
