package br.unifesp.maritaca.business.oauth.dto;

public class OAuthCodeDTO {
	
	private String key;
	
	private String code;
	
	private String clientId;
	
	private String userEmail;

	public OAuthCodeDTO() {	}
	
	public OAuthCodeDTO(String key, String code, String clientId, String userEmail) {
		this.setKey(key);
		this.code = code;
		this.clientId = clientId;
		this.setUserEmail(userEmail);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
}
