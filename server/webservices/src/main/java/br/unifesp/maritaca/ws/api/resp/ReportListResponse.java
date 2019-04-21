package br.unifesp.maritaca.ws.api.resp;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.unifesp.maritaca.business.report.dto.SimpleReportDTO;

@XmlRootElement(name="response")
public class ReportListResponse extends MaritacaResponse {

	private int size;
	
	private Collection<SimpleReportDTO> reports;

	@XmlElementWrapper(name="reports")
	@XmlElement(name="report")
	public Collection<SimpleReportDTO> getReports() {
		return reports;
	}

	public void setReports(Collection<SimpleReportDTO> reports) {
		this.reports = reports;
	}

	@XmlAttribute
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
