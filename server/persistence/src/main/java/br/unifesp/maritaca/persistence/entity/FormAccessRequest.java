package br.unifesp.maritaca.persistence.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.RequestStatusConverter;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;
import br.unifesp.maritaca.persistence.permission.RequestStatusType;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hom.annotations.Column;

@Entity
@Table(name="FormAccessRequest")
public class FormAccessRequest {

	@Id
	private UUID key;
	
	@Column(name="formUrl")
	private String formUrl;
	
	@Minimal(indexed=true)
	@Column(name="user", converter=UUIDConverter.class)
	private UUID user;
	
	@Minimal(indexed=true)
	@Column(name="owner", converter=UUIDConverter.class)
	private UUID owner;
	
	private MaritacaDate date;
	
	@Column(name="status", converter=RequestStatusConverter.class)
	private RequestStatusType status;	
	
	public FormAccessRequest() { }
	
	public FormAccessRequest(String formUrl, UUID userKey, UUID ownerKey) {
		this.setFormUrl(formUrl);
		this.setUser(userKey);
		this.setOwner(ownerKey);
		this.setStatus(RequestStatusType.PENDING);
	}

	public FormAccessRequest(String formUrl, UUID userKey,
			RequestStatusType status) {
		this.setFormUrl(formUrl);
		this.setUser(userKey);
		this.status = status;
	}

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
		if(key!=null){
			long dl = TimeUUIDUtils.getTimeFromUUID(getKey());
			MaritacaDate date = new MaritacaDate();
			date.setTime(dl);
			this.date = date;
		}
	}

	public RequestStatusType getStatus() {
		return status;
	}

	public void setStatus(RequestStatusType status) {
		this.status = status;
	}

	public UUID getUser() {
		return user;
	}

	public void setUser(UUID user) {
		this.user = user;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public MaritacaDate getDate() {
		return date;
	}

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}
}