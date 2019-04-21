package br.unifesp.maritaca.ws.resource.messsages;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMobile {
	
	private String value;
	
	private String androidVersion;

	public ErrorMobile() {}

	@XmlElement
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute
	public String getAndroidVersion() {
		return androidVersion;
	}
	
	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}
}