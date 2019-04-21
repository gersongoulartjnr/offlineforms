package br.unifesp.maritaca.persistence.dao.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.OAuthDAO;
import br.unifesp.maritaca.persistence.entity.OAuthClient;
import br.unifesp.maritaca.persistence.entity.OAuthCode;
import br.unifesp.maritaca.persistence.entity.OAuthToken;

@Service("oauthDAO")
public class OAuthDAOImpl extends AbstractDAO implements OAuthDAO {

	@Override
	public OAuthClient findAuthClient(String clientId) {
		List<OAuthClient> listClients = emHector.
				cQuery(OAuthClient.class, "clientId", clientId, true);
		for (OAuthClient client : listClients) {
			return client; // always returns the first
		}
		return null;
	}

	@Override
	public OAuthToken findOAuthToken(String accessToken) {
		List<OAuthToken> listTokens = emHector.cQuery(OAuthToken.class,
				"accessToken", accessToken);
		for (OAuthToken token : listTokens) {
			return token;// always returns the first
		}
		return null;
	}

	@Override
	public <T> void persist(T obj) {
		emHector.persist(obj);
	}

	@Override
	public OAuthCode findOAuthCode(String code) {
		List<OAuthCode> listCodes = emHector.cQuery(OAuthCode.class,
				"code", code);
		for(OAuthCode oauthCode: listCodes){
			return oauthCode; //return first
		}
		return null;
	}

}
