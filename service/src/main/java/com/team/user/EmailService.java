package com.team.user;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "fromAddress@gmail.com";

    public void mailSend(String email, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(email);
        message.setSubject(title);
        message.setText(text);
        mailSender.send(message);
    }
}
