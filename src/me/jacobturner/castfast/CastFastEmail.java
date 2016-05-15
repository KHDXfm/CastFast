package me.jacobturner.castfast;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import me.jacobturner.castfast.CastFastOptions;

public class CastFastEmail {
	private static Properties mailServerProperties;
	private static Session getMailSession;
	private static MimeMessage generateMailMessage;
	private static CastFastOptions options = new CastFastOptions();

	public static void sendEmail(String toEmail, String subject, String link) throws AddressException, MessagingException {
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", Integer.parseInt(options.getValue("port")));
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", options.getValue("use_ssl_tls"));
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.setFrom(options.getValue("email_address"));
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
		generateMailMessage.setSubject(subject);
		String emailBody = "Your podcast has successfully uploaded and is now available at the following link:<br /><a href='" + link +"'>" + link + "</a>";
		generateMailMessage.setContent(emailBody, "text/html");
		Transport transport = getMailSession.getTransport("smtp");
		transport.connect(options.getValue("smtp_server"), options.getValue("username"), options.getValue("password"));
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
}
