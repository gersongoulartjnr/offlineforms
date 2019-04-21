package br.unifesp.maritaca.business.report.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="reportItem")
public class ReportItemWParams extends ReportItemDTO {

	private List<String> parameters;

	public ReportItemWParams() { }
	
	@XmlElementWrapper(name="params")
	@XmlElement(name="param")
	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
}
