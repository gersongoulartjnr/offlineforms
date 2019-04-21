package br.unifesp.maritaca.business.oauth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.oauth.dto.DataAccessTokenDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthClientDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthCodeDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthTokenDTO;
import br.unifesp.maritaca.persistence.dao.OAuthDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.OAuthClient;
import br.unifesp.maritaca.persistence.entity.OAuthCode;
import br.unifesp.maritaca.persistence.entity.OAuthToken;
import br.unifesp.maritaca.persistence.entity.User;

@Service
public class ManagementOAuthImpl implements ManagementOAuth {

	@Autowired private OAuthDAO oauthDAO;
	@Autowired private UserDAO userDAO; 
	
	@Override
	public OAuthClientDTO findOAuthClient(String clientId) {
		OAuthClient oauthClient = oauthDAO.findAuthClient(clientId);  
		if (oauthClient != null){
			OAuthClientDTO clientDTO = new OAuthClientDTO(oauthClient.getKey().toString(),
														  oauthClient.getClientId(), 
														  oauthClient.getSecret());
			return clientDTO;
		}
		return null;
	}

	@Override
	public OAuthTokenDTO findOAuthToken(String accessToken) {
		OAuthToken oauthToken = oauthDAO.findOAuthToken(accessToken);
		User user = userDAO.findUserByKey(oauthToken.getUser());
		String clientId = (oauthToken.getClientId() != null) ? oauthToken.getClientId().toString() : "";
		if (oauthToken != null && user != null) {
			return new OAuthTokenDTO(oauthToken.getAccessToken(),
									oauthToken.getRefreshToken(),
									user.getEmail(),
									oauthToken.getAccessTokenTTL(),
									clientId);
		}
		return null;
	}

	@Override
	public void saveAuthorizationCode(OAuthCodeDTO oauthCodeDTO) {
		OAuthCode oauthCode = new OAuthCode();
		OAuthClientDTO oauthClientDTO = this.findOAuthClient(oauthCodeDTO.getClientId());
		oauthCode.setClientId(UUID.fromString(oauthClientDTO.getKey()));
		oauthCode.setCode(oauthCodeDTO.getCode());
		oauthCode.setUserEmail(oauthCodeDTO.getUserEmail());
		
		oauthDAO.persist(oauthCode);		
	}

	@Override
	public DataAccessTokenDTO findOAuthCodeAndClient(String code,
			String clientId) {
		DataAccessTokenDTO dataDTO = new DataAccessTokenDTO();
		
		OAuthCode authCode = oauthDAO.findOAuthCode(code);

		if (authCode != null) {
			OAuthCodeDTO oauthCodeDTO = new OAuthCodeDTO(authCode.getKey().toString(),
														authCode.getCode(),
														authCode.getClientId().toString(),
														authCode.getUserEmail());
			dataDTO.setOauthCodeDTO(oauthCodeDTO);
		} else { 
			dataDTO.setOauthCodeDTO(null);	
		}
		dataDTO.setOauthClientDTO(this.findOAuthClient(clientId));
		return dataDTO;
	}

	@Override
	public void saveOAuthToken(OAuthTokenDTO tokenDTO) {
		OAuthToken oauthToken = new OAuthToken();
		oauthToken.setAccessToken(tokenDTO.getAccessToken());
		oauthToken.setRefreshToken(tokenDTO.getRefreshToken());
			OAuthClientDTO client = this.findOAuthClient(tokenDTO.getClientId());
		oauthToken.setClientId(UUID.fromString(client.getKey()));
		User user = userDAO.findUserByEmail(tokenDTO.getUserEmail());
		oauthToken.setUser(user.getKey());
		tokenDTO.setExpirationDate(oauthToken.getAccessTokenTTL());
		
		oauthDAO.persist(oauthToken);
	}
}