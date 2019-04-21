package br.unifesp.maritaca.ws.api.resp;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;

public class ReportItemResponse extends MaritacaResponse {

	private String operation;
	
	private String questionType;
	
	private int itemId;
	
	public ReportItemResponse() { }
	
	public ReportItemResponse(Status status) {
		super(status);
	}
	
	@XmlElement(name="op")
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}

	@XmlElement(name="type")
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	@XmlElement(name="itemId")
	public int getItemId() {
		return itemId;
	}
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
}
