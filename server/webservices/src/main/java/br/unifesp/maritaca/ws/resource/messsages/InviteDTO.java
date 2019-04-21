package br.unifesp.maritaca.ws.resource.messsages;

import java.io.Serializable;
import java.util.List;

public class InviteDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> emails;
	
	private String subject;
	
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

}
