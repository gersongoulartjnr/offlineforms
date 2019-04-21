package br.unifesp.maritaca.business.messaging.dto;

import java.io.Serializable;
import java.util.List;

public class EmailDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String subject;
	private String content;
	private List<String> emails;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
}