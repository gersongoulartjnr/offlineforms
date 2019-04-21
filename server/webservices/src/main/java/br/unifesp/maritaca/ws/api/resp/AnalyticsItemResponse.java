package br.unifesp.maritaca.ws.api.resp;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;


public class AnalyticsItemResponse extends MaritacaResponse {
	
	private int itemId;
	private String result;
	
	public AnalyticsItemResponse() {
		super(Status.OK);
	}
	
	@XmlElement(name="itemId")
	public int getItemId() {
		return itemId;
	}	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	@XmlElement
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
}