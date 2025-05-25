package com.bumsoap.notes.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender mailSender;

  public void sendPasswordResetMail(String toEmail, String resetUrl) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("jbpark03@gmail.com");
    message.setTo(toEmail);
    message.setSubject("패스워드 재 설정 링크");
    message.setText("패스워드 재 설정 링크: " + resetUrl);
    mailSender.send(message);
  }
}
