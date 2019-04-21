package br.unifesp.maritaca.persistence.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;
import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;

@Entity
@Table(name="UserFormTag")
public class UserFormTag {

	@Id
	private UUID key;
	
	@Minimal(indexed=true)
	@Column(name="tag", converter = UUIDConverter.class)
	private UUID tag;
	
	@Minimal(indexed=true)
	@Column(name="user", converter = UUIDConverter.class)
	private UUID user;
	
	@Minimal(indexed=true)
	@Column(name="form", converter = UUIDConverter.class)
	private UUID form;

	public UserFormTag() {  }
	
	public UserFormTag(UUID tagKey, UUID userKey, UUID formKey) {
		setTag(tagKey);
		setUser(userKey);
		setForm(formKey);
	}

	public UUID getTag() {
		return tag;
	}

	public void setTag(UUID tag) {
		this.tag = tag;
	}

	public UUID getUser() {
		return user;
	}

	public void setUser(UUID user) {
		this.user = user;
	}

	public UUID getForm() {
		return form;
	}

	public void setForm(UUID form) {
		this.form = form;
	}

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}
	
}
