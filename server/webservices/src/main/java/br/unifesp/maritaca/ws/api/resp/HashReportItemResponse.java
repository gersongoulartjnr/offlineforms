package br.unifesp.maritaca.ws.api.resp;

import java.util.HashMap;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.unifesp.maritaca.ws.api.adapter.Adapter;

@XmlRootElement(name="reportItem")
public class HashReportItemResponse extends ReportItemResponse {

	private HashMap<String, String> result;
	
	public HashReportItemResponse() {
		super(Status.OK);
	}

	@XmlJavaTypeAdapter(value=Adapter.class)
	@XmlElement(name="result")
	public HashMap<String, String> getResult() {
		return result;
	}

	public void setResult(HashMap<String, String> result) {
		this.result = result;
	}
	
}
