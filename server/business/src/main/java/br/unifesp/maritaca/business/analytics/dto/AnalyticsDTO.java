package br.unifesp.maritaca.business.analytics.dto;

import br.unifesp.maritaca.business.base.dto.BaseDTO;
import br.unifesp.maritaca.persistence.permission.Permission;

public class AnalyticsDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String doc;
	private String creationDate;
	private Permission reportPermission;

	public AnalyticsDTO() {
	}

	public AnalyticsDTO(String id, String name, String creationDate) {
		this.id = id;
		this.name = name;
		this.creationDate = creationDate;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public Permission getReportPermission() {
		return reportPermission;
	}
	public void setReportPermission(Permission reportPermission) {
		this.reportPermission = reportPermission;
	}

	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
}