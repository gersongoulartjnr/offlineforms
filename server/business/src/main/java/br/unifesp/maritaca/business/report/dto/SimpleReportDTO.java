package br.unifesp.maritaca.business.report.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.unifesp.maritaca.business.base.dto.BaseDTO;

@XmlRootElement(name="report")
public class SimpleReportDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String reportId;
	private String reportName;
	private String creationDate;
	
	public SimpleReportDTO() {}

	public SimpleReportDTO(String reportId, String reportName, String creationDate) {
		this.reportId = reportId;
		this.reportName = reportName;
		this.creationDate = creationDate;
	}

	@XmlAttribute
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@XmlAttribute
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	@XmlAttribute
	public String getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
}