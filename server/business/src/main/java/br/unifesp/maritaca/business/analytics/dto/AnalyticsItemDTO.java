package br.unifesp.maritaca.business.analytics.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="analyticsItem")
public class AnalyticsItemDTO {

	private String formId;
	private String analyticsId;
	private int itemId;
	
	@XmlElement(name="form")
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	@XmlElement(name="analytics")
	public String getAnalyticsId() {
		return analyticsId;
	}
	public void setAnalyticsId(String analyticsId) {
		this.analyticsId = analyticsId;
	}
	
	@XmlElement(name="item")
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}