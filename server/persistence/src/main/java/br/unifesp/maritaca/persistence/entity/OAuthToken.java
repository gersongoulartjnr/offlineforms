package br.unifesp.maritaca.persistence.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;

@Entity
@Table(name="OAuthToken")
public class OAuthToken {
	
	@Id
	private UUID key;
	
	@Minimal(indexed=true)
	@Column(name="refreshToken")
	private String refreshToken;
	
	@Minimal(indexed=true, ttl=true)
	@Column(name="accessToken")
	private String accessToken;
	
	@Minimal(indexed=true)
	@Column(name="user", converter=UUIDConverter.class)
	private UUID user;
	
	@Minimal
	@Column(name="clientId")
	private UUID clientId;
	
	private Integer accessTokenTTL = ConstantsPersistence.OAUTH_EXPIRATION_DATE;

	public Integer getAccessTokenTTL() {
		return accessTokenTTL;
	}

	public void setAccessTokenTTL(Integer accessTokenTTL) {
		this.accessTokenTTL = accessTokenTTL;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public UUID getKey() {
		return key;
	}
	
	public void setKey(UUID key) {
		this.key = key;
	}
	
	public UUID getUser() {
		return user;
	}
	
	public void setUser(UUID user) {
		this.user = user;
	}
	
	public UUID getClientId() {
		return clientId;
	}
	
	public void setClientId(UUID clientId) {
		this.clientId = clientId;
	}
	
}
