package br.unifesp.maritaca.ws.api.resp;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class GenericResponse extends MaritacaResponse {

	private String value;
	
	private String extra;

	public String getValue() {
		return value;
	}
	
	public GenericResponse() {
		super(Status.OK);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
	
}
