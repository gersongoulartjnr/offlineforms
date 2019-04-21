package br.unifesp.maritaca.business.oauth.dto;

public class OAuthTokenDTO {
	
	private String accessToken;
	
	private String refreshToken;
	
	private String userEmail;
	
	private Integer expirationDate;
	
	private String clientId;

	public OAuthTokenDTO(String accessToken, String refreshToken, String userEmail,
			Integer expirationDate, String clientId) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.setUserEmail(userEmail);
		this.expirationDate = expirationDate;
		this.clientId = clientId;
	}

	public OAuthTokenDTO() { }

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Integer expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

}
