package br.unifesp.maritaca.rabbit;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class SenderEmail {

	private static SenderEmail senderEmail;
	
	private SenderEmail() {	}
	
	public static SenderEmail getInstance() {
		if (senderEmail == null) {
			return new SenderEmail();
		}
		return senderEmail;
	}
	
	public void sendSimpleEmail(List<String> emails, String subject, String body) throws EmailException{
		SimpleEmail simpleEmail;
		for (String email : emails) {
			simpleEmail = new SimpleEmail();
			simpleEmail.setHostName("smtp.gmail.com");
			simpleEmail.setAuthentication("test.maritaca", "123412abc");
			simpleEmail.setSSL(true);
			simpleEmail.setFrom("test.maritaca@gmail.com");
			simpleEmail.addTo(email);
			simpleEmail.setSubject(subject);
			simpleEmail.setMsg(body);
			simpleEmail.send(); 
		}
	}
	
}
