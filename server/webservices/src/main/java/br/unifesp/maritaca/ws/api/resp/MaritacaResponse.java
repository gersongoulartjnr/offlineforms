package br.unifesp.maritaca.ws.api.resp;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Response of Maritaca, mainly used in RESTful
 * A MaritacaResponse wrappers the response on the system
 * to be exported as XML or JSON, even if the response
 * is an Exception
 * 
 * @author alvarohenry
 * @author emigueltriana
 */
public class MaritacaResponse {
	
	public static final String FORM_TYPE = "form";
	public static final String RESPONSE_TYPE = "answer";
	public static final String MESSAGE_TYPE = "message";
	public static final String REPORT_ITEM_TYPE = "reportItem";
	public static final String FAIL = "FAIL";

	private String status;
	private int code;
	
	public MaritacaResponse() {	}
	
	public MaritacaResponse(Status status) {
		this.status = status.getReasonPhrase();
		this.code   = status.getStatusCode();
	}

	@XmlAttribute
	public String getStatus() {
		return status;
	}

	@XmlAttribute
	public int getCode() {
		return code;
	}
}