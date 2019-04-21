package br.unifesp.maritaca.business.report.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="reportItem")
public class ReportItemList extends ReportItemDTO {

	private int numAnswers;

	public ReportItemList() { }
	
	@XmlAttribute
	public int getNumAnswers() {
		return numAnswers;
	}

	public void setNumAnswers(int numAnswers) {
		this.numAnswers = numAnswers;
	}
}
