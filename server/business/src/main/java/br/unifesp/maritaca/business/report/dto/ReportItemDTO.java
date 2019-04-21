package br.unifesp.maritaca.business.report.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="reportItem")
@XmlSeeAlso({ReportItemWParams.class, ReportItemList.class})
public class ReportItemDTO {

	private String formId;
	
	private String reportId;
	
	private String op;
	
	private String questionType;
		
	private int itemId;
	
	private int questionId;

	@XmlElement(name="form")
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@XmlElement(name="report")
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@XmlElement(name="op")
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	@XmlElement(name="type")
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	@XmlElement(name="questionId")
	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	@XmlElement(name="itemId")
	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}
