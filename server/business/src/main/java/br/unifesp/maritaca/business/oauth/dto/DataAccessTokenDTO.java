package br.unifesp.maritaca.business.oauth.dto;

import java.io.Serializable;

public class DataAccessTokenDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private OAuthCodeDTO oauthCodeDTO;
	
	private OAuthClientDTO oauthClientDTO;
	
	public OAuthCodeDTO getOauthCodeDTO() {
		return oauthCodeDTO;
	}

	public void setOauthCodeDTO(OAuthCodeDTO oauthCodeDTO) {
		this.oauthCodeDTO = oauthCodeDTO;
	}

	public OAuthClientDTO getOauthClientDTO() {
		return oauthClientDTO;
	}

	public void setOauthClientDTO(OAuthClientDTO oauthClientDTO) {
		this.oauthClientDTO = oauthClientDTO;
	}

}
