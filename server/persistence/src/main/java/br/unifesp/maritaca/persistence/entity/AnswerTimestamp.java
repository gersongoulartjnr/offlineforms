package br.unifesp.maritaca.persistence.entity;

import java.io.Serializable;
import java.util.UUID;

public class AnswerTimestamp implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long creationDate;

	private UUID answer;
	
	public AnswerTimestamp() { }
	
	public AnswerTimestamp(UUID answerKey, Long creationDate) {
		this.answer = answerKey;
		this.creationDate = creationDate;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}
	
	public UUID getAnswer() {
		return answer;
	}
	
	public void setAnswer(UUID answer) {
		this.answer = answer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnswerTimestamp other = (AnswerTimestamp) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		return true;
	}

}
