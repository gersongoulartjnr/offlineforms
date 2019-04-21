package br.unifesp.maritaca.ws.api.resp;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="error")
public class ErrorResponse extends MaritacaResponse {
	
	private String message;
	
	public ErrorResponse() { }
	
	public ErrorResponse(Status status) {
		super(status);
		setMessage(message);
	}
	
	public ErrorResponse(Throwable e) {
		super(Status.INTERNAL_SERVER_ERROR);
		setMessage(e.getMessage());
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
