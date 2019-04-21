package br.unifesp.maritaca.ws.api.resp;

import java.util.List;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ListReportItemResponse extends ReportItemResponse {

	private List<String> result;
	
	public ListReportItemResponse() {
		super(Status.OK);
	}

	@XmlElementWrapper(name="results")
	@XmlElement(name="result")
	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}
	
	
	
}
