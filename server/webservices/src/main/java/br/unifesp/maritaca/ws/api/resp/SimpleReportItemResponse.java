package br.unifesp.maritaca.ws.api.resp;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="reportItem")
public class SimpleReportItemResponse extends ReportItemResponse {

	private String result;
	
	public SimpleReportItemResponse() {
		super(Status.OK);
	}
	
	@XmlElement
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
}
