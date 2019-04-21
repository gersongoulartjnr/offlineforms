package br.unifesp.maritaca.business.base.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	private String fullName;
	
	private String loggedBefore;

	public UserDTO() {
	}

	public UserDTO(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLoggedBefore() {
		return loggedBefore;
	}

	public void setLoggedBefore(String loggedBefore) {
		this.loggedBefore = loggedBefore;
	}	
	
	@Override
	public String toString() {		
		return getUsername();
	}
}