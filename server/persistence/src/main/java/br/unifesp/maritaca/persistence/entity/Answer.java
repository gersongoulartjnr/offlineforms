package br.unifesp.maritaca.persistence.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;

@Entity
@Table(name="Answer")
public class Answer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private UUID key;

	@Minimal(indexed=true)
	@Column(name = "form", converter = UUIDConverter.class)
	private UUID form;

	@Minimal(indexed=true)
	@Column(name = "user", converter = UUIDConverter.class)
	private UUID user;
	
	@Column(name = "userData")
	private String userData;
	
	@Column(name = "userEmail")
	@Deprecated
	private String userEmail;

	@Minimal(indexed=true)
	@Column(name = "url")
	private String url;
	
	@Column(name="questions")
	private List<QuestionAnswer> questions;
	
	@Column(name = "creationDate")
	private Long creationDate;
	
	public UUID getKey() {
		return key;
	}
	public void setKey(UUID key) {
		this.key = key;
	}
	public void setKey(String ks) {
		if (ks == "")
			this.key = null;
		this.key = UUID.fromString(ks);
	}
	public UUID getForm() {
		return form;
	}
	public void setForm(UUID form) {
		this.form = form;
	}
	public UUID getUser() {
		return user;
	}
	public void setUser(UUID user) {
		this.user = user;
	}
	public List<QuestionAnswer> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionAnswer> questions) {
		this.questions = questions;
	}
	public Long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	@Override
	public String toString() {
		if (getKey() != null) {
			return getKey().toString();
		}
		return super.toString();
	}
}