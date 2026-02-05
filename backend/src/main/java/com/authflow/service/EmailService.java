package com.authflow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.properties.mail.smtp.from}")
	private String fromEmail;
	
	public void sendOtpEmail(String toEmail , String otp) throws MessagingException {
		 MimeMessage mimeMessage = mailSender.createMimeMessage();
		 MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		 mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setTo(toEmail);
			mimeMessageHelper.setSubject("Verify OTP CODE ");
			mimeMessageHelper.setText("Please use this code to verify your Account for your personal AuthFlow account "
					+ toEmail + "\n Here is your code:\n\n" + otp
					+ "\n\nIf you don't recognize your personal AuthFlow, you can contact our helpline\n\n" + "Thanks,\n\n"
					+ "The AuthFlow account team");
			mailSender.send(mimeMessage);
	}

	public void sendResetOtp(String toEmail, String otp) throws MessagingException {
		System.out.println("The function  which is used to send otp to email  : " +toEmail );
		MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setTo(toEmail);
			mimeMessageHelper.setSubject("Password Reset CODE ");
			mimeMessageHelper.setText("Please use this code to reset the password for your personal AuthFlow account "
					+ toEmail + "\n Here is your code:\n\n" + otp
					+ "\n\nIf you don't recognize your personal AuthFlow, you can contact our helpline\n\n" + "Thanks,\n\n"
					+ "The AuthFlow account team");
			mailSender.send(mimeMessage);
			System.out.println("✅ Email sent successfully to " + toEmail);

		} 

	public void sendWelcome(String toEmail, String name) {
		System.out.println("✅ Email message called  successfully ");
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//			System.out.println("The email which is used to send wlm : " + fromEmail);
			mimeMessageHelper.setFrom(fromEmail.toLowerCase());
			mimeMessageHelper.setTo(toEmail);
			mimeMessageHelper.setSubject("Welcome to AuthFlow!");
			mimeMessageHelper
					.setText("Hello " + name + ",\n\n  Thank you for signing up! \n\n Regards, \nAuthFlow Team");
			mailSender.send(mimeMessage);
//			System.out.println("✅ Email sent successfully to " + toEmail);
		} catch (MessagingException e) {
			System.out.println("Email sending failed");
			e.printStackTrace();
		}

	}
}
