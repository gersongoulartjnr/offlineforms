package br.unifesp.maritaca.business.analytics.dto;

public class AEditorDTO extends AnalyticsDTO {

	private static final long serialVersionUID = 1L;
	
	private String formUrl;
	private String formName;
	private String formDescription;
	private String formXml;
	private String formCreationDate;
	private int formNumOfCollects;
	private String formCollectors;
	
	public String getFormUrl() {
		return formUrl;
	}
	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getFormDescription() {
		return formDescription;
	}
	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}
	public String getFormXml() {
		return formXml;
	}
	public void setFormXml(String formXml) {
		this.formXml = formXml;
	}
	public String getFormCreationDate() {
		return formCreationDate;
	}
	public void setFormCreationDate(String formCreationDate) {
		this.formCreationDate = formCreationDate;
	}
	public int getFormNumOfCollects() {
		return formNumOfCollects;
	}
	public void setFormNumOfCollects(int formNumOfCollects) {
		this.formNumOfCollects = formNumOfCollects;
	}
	public String getFormCollectors() {
		return formCollectors;
	}
	public void setFormCollectors(String formCollectors) {
		this.formCollectors = formCollectors;
	}
}