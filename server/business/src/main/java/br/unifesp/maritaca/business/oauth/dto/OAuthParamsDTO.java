package br.unifesp.maritaca.business.oauth.dto;

import java.io.Serializable;

public class OAuthParamsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String responseType;
	private String clientId;
	private String redirectUri;
	private String formId;
	
	public OAuthParamsDTO() {}
	
	public OAuthParamsDTO(String responseType, String clientId, String redirectUri) {
		this.responseType = responseType;
		this.clientId = clientId;
		this.redirectUri = redirectUri;
	}
	
	public OAuthParamsDTO(String responseType, String clientId, String redirectUri, String formId) {
		this.responseType = responseType;
		this.clientId = clientId;
		this.redirectUri = redirectUri;
		this.formId = formId;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
}