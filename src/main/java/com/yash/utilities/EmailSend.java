package com.yash.utilities;

import java.util.Properties;

import com.yash.Configuration;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSend {
	final private String username = Configuration.SMTP_USERNAME, password = Configuration.SMTP_PASSWORD;
	private Session session;

	public EmailSend() {
		super();

		final Properties properties = new Properties();
		properties.put("mail.smtp.host", Configuration.SMTP_SERVER_HOST);	// Your SMTP server host
		properties.put("mail.smtp.port", Configuration.SMTP_SSL_TLS_PORT);	// TLS port
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");	// Enable STARTTLS

		session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	public EmailSend(final String email, final String subject, final String text) throws MessagingException {
		this();
		sendMessage(email, subject, text);
	}

	public void sendMessage(final String email, final String subject, final String text) throws MessagingException {
		final Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject(subject);
		message.setText(text);

		Transport.send(message);
	}

	public void buildTemplate() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
			"<!-- https://www.freecodecamp.org/news/how-to-create-a-responsive-html-email-template/ -->" +
			"" +
			"<!doctype html>" +
			"<html lang=\"en\">" +
			"	<head>" +
			"		<meta charset=\"utf-8\" />" +
			"		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />" +
			"		<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\" />" +
			"		<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin />" +
			"		<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap\" />" +
			"		<style type=\"text/css\"></style>" +
			"	</head>" +
			"	<body style=\"font-family: 'Poppins', Arial, sans-serif\">" +
			"		<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">" +
			"			<tr>" +
			"				<td align=\"center\" style=\"padding: 20px;\">" +
			"					<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">" +
			"						<!-- Header -->" +
			"						 <tr>" +
			"							<td class=\"header\" style=\"background-color: #345C72; padding: 40px; text-align: center; color: white; font-size: 24px;\">" +
			"								Tasker" +
			"							</td>" +
			"						 </tr>" +
			"						<!-- Body -->" +
			"						<tr>" +
			"							<td class=\"body\" style=\"padding: 40px; text-align: left; font-size: 16px; line-height: 1.6;\">"
		);
	}
}
