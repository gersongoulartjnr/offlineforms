package br.unifesp.maritaca.ws.resource.messsages;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="invite")
public class Invitation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String formId;
	
	private List<String> friends;

	@XmlElement
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@XmlElementWrapper(name="friends")
	@XmlElement(name="friend")
	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}	
	

}
