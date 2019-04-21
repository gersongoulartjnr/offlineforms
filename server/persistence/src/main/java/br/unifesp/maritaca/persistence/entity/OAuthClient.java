package br.unifesp.maritaca.persistence.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.annotation.Minimal;

@Entity
@Table(name="OAuthClient")
public class OAuthClient {
	
	@Id
	private UUID key;

	@Minimal(indexed = true)
	@Column(name="clientId")
	private String clientId;
	
	@Minimal
	@Column(name="secret")
	private String secret;

	public UUID getKey() {
		return key;
	}
	
	public void setKey(UUID key) {
		this.key = key;
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
}
