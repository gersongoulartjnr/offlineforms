package br.unifesp.maritaca.persistence.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;
import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.JSONAnswerTimestampConverter;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;

@Entity
@Table(name="AnswerByUserForm")
public class AnswerByUserForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private UUID key;
	
	@Minimal(indexed=true)
	@Column(name="user", converter = UUIDConverter.class)
	private UUID user;
	
	@Minimal(indexed=true)
	@Column(name="form", converter = UUIDConverter.class)
	private UUID form;

	@Column(name="answers", converter=JSONAnswerTimestampConverter.class)
	private List<AnswerTimestamp> answers; // lists of answers

	public AnswerByUserForm() {	}
	
	public AnswerByUserForm(UUID user, UUID form, List<AnswerTimestamp> answers) {
		this.user = user;
		this.form = form;
		this.answers = answers;
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

	public UUID getForm() {
		return form;
	}

	public void setForm(UUID form) {
		this.form = form;
	}

	public void setAnswers(List<AnswerTimestamp> answers) {
		this.answers = answers;
	}
	
	public List<AnswerTimestamp> getAnswers() {
		return answers;
	}
}
