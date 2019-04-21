package br.unifesp.maritaca.business.oauth.dto;

import java.io.Serializable;

public class OAuthClientDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	
	private String clientId;
	
	private String secret;
	
	public OAuthClientDTO(String key, String clientId, String secret) {
		setKey(key);
		setClientId(clientId);
		setSecret(secret);
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
