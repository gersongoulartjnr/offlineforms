package br.unifesp.maritaca.persistence.dao;

import br.unifesp.maritaca.persistence.entity.OAuthClient;
import br.unifesp.maritaca.persistence.entity.OAuthCode;
import br.unifesp.maritaca.persistence.entity.OAuthToken;

public interface OAuthDAO {

	OAuthClient findAuthClient(String clientId);

	OAuthToken findOAuthToken(String accessToken);

	<T> void persist(T obj);

	OAuthCode findOAuthCode(String code);

}
