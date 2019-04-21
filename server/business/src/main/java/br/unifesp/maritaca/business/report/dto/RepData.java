package br.unifesp.maritaca.business.report.dto;

import java.util.List;

public class RepData {
	
	private String title;
	private List<RepReport> lstReport;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<RepReport> getLstReport() {
		return lstReport;
	}
	public void setLstReport(List<RepReport> lstReport) {
		this.lstReport = lstReport;
	}
}