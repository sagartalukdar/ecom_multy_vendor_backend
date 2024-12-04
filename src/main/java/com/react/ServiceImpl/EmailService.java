package com.react.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendVerificationOtpEmail(String userEmail,String otp,String subject,String text) throws Exception {
		try {
			MimeMessage mimeMessage=javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,"utf-8");
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text);
			mimeMessageHelper.setTo(userEmail);
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new Exception("failed to send mail");
		}
	}
}
