package br.unifesp.maritaca.business.form.dto;

import br.unifesp.maritaca.business.base.dto.BaseDTO;

public class FormAccessRequestDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	private String formUrl;	
	private String formTitle;	
	private String userFullName;	
	private String userEmail;	
	private String creationDate;
	
	public FormAccessRequestDTO(String formUrl, String formTitle,
			String userFullName, String userEmail, String creationDate) {
		this.formUrl = formUrl;
		this.formTitle = formTitle;
		this.userFullName = userFullName;
		this.userEmail = userEmail;
		this.creationDate = creationDate;
	}
	
	public String getFormTitle() {
		return formTitle;
	}
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	public String getFormUrl() {
		return formUrl;
	}
	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}	
}