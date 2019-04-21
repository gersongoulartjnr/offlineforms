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
@Table(name="OAuthCode")
public class OAuthCode {
	
	@Id
	private UUID key;
	
	@Minimal(indexed = true, ttl=true)
	@Column(name="code")
	private String code;
	
	@Minimal
	@Column(name="clientId", converter=UUIDConverter.class)
	private UUID clientId;
	
	@Minimal
	@Column(name="userEmail")
	private String userEmail;

	private Integer codeTTL = ConstantsPersistence.OAUTH_CODE_TIME_TO_LIVE;
	
	public Integer getCodeTTL() {
		return codeTTL;
	}
	
	public void setCodeTTL(Integer codeTTL) {
		this.codeTTL = codeTTL;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public UUID getClientId() {
		return clientId;
	}

	public void setClientId(UUID clientId) {
		this.clientId = clientId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}	
}