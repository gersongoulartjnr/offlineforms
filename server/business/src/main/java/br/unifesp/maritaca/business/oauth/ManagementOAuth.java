package br.unifesp.maritaca.business.oauth;

import br.unifesp.maritaca.business.oauth.dto.DataAccessTokenDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthClientDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthCodeDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthTokenDTO;

public interface ManagementOAuth {

	OAuthClientDTO findOAuthClient(String clientId);
	
	OAuthTokenDTO findOAuthToken(String accessToken);

	void saveAuthorizationCode(OAuthCodeDTO oauthCodeDTO);

	DataAccessTokenDTO findOAuthCodeAndClient(String code, String clientId);

	void saveOAuthToken(OAuthTokenDTO tokenDTO);
	
}
