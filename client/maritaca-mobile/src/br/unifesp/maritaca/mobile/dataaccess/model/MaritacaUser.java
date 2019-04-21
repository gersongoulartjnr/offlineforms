package br.unifesp.maritaca.mobile.dataaccess.model;

import java.io.Serializable;
import java.util.List;

public class MaritacaUser implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String email;
	private String accessToken;
	private String refreshToken;
	private long initDate;
	private long expirationDate;
	private String formId;
	private List<MaritacaGroup> answersGroup;
	
	public MaritacaUser() {
	}
	
	public MaritacaUser(Integer id, String email, String accessToken, String refreshToken,
			long expirationDate, long initDate) {
		this.setId(id);
		this.email = email;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expirationDate = expirationDate;
		this.initDate = initDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public long getInitDate() {
		return initDate;
	}
	
	public void setInitDate(long initDate) {
		this.initDate = initDate;
	}

	public long getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(long expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public List<MaritacaGroup> getAnswersGroup() {
		return answersGroup;
	}

	public void setAnswersGroup(List<MaritacaGroup> answersGroup) {
		this.answersGroup = answersGroup;
	}
}