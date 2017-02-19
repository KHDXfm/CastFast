package me.jacobturner.castfast;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import me.jacobturner.castfast.CastFastOptions;

public class CastFastEmail {
	private static Properties mailServerProperties;
	private static Session mailSession;
	private static MimeMessage mailMessage;
	private static CastFastOptions options = new CastFastOptions();

	public static void sendEmail(String[] toEmails, String subject, String link, String showName) throws AddressException, MessagingException, IOException {
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", Integer.parseInt(options.getValue("port")));
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", options.getValue("use_ssl_tls"));
		mailSession = Session.getDefaultInstance(mailServerProperties, null);
		mailMessage = new MimeMessage(mailSession);
		mailMessage.setFrom(new InternetAddress(options.getValue("email_address"), "CastFast for " + options.getValue("station_name")));
		for (String toEmail : toEmails) {
			mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
		}
		mailMessage.setSubject(subject);
		String emailBody = CastFastFile.readEmailTemplate().replaceAll("\\{\\{LINK\\}\\}", link).replaceAll("\\{\\{SHOWNAME\\}\\}", showName);
		mailMessage.setContent(emailBody, "text/html; charset=utf-8");
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(options.getValue("smtp_server"), options.getValue("username"), options.getValue("password"));
		transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		transport.close();
	}
}
