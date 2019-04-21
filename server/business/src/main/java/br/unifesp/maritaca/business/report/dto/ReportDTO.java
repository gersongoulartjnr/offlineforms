package br.unifesp.maritaca.business.report.dto;

import java.util.Date;
import java.util.UUID;

import br.unifesp.maritaca.persistence.permission.Permission;

public class ReportDTO extends SimpleReportDTO {
	
	private static final long serialVersionUID = 1L;

	private UUID key;	
	private String user;
	private String reportXml;
	private String formUrl;
	private String formName;
	private String formDescription;
	private String formIcon;
	private String formXml;
	private String token;
	private Date start;
	private Date finish;
	private String strStart;
	private String strFinish;
	private Permission reportPermission;
	private String reportData;
	
	public UUID getKey() {
		return key;
	}
	public void setKey(UUID key) {
		this.key = key;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getReportXml() {
		return reportXml;
	}
	public void setReportXml(String reportXml) {
		this.reportXml = reportXml;
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
	public String getFormIcon() {
		return formIcon;
	}
	public void setFormIcon(String formIcon) {
		this.formIcon = formIcon;
	}
	public String getFormXml() {
		return formXml;
	}
	public void setFormXml(String formXml) {
		this.formXml = formXml;
	}
	public String getFormUrl() {
		return formUrl;
	}
	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getFinish() {
		return finish;
	}
	public void setFinish(Date finish) {
		this.finish = finish;
	}
	public String getStrStart() {
		return strStart;
	}
	public void setStrStart(String strStart) {
		this.strStart = strStart;
	}
	public String getStrFinish() {
		return strFinish;
	}
	public void setStrFinish(String strFinish) {
		this.strFinish = strFinish;
	}
	public Permission getReportPermission() {
		return reportPermission;
	}
	public void setReportPermission(Permission reportPermission) {
		this.reportPermission = reportPermission;
	}
	public String getReportData() {
		return reportData;
	}
	public void setReportData(String reportData) {
		this.reportData = reportData;
	}	
}