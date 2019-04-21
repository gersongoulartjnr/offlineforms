package br.unifesp.maritaca.business.form.dto;

import br.unifesp.maritaca.business.base.dto.BaseDTO;

public class AccessRequestDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	
	private String user;
	
	private String form;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}
	
}
