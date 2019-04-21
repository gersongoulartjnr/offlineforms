package br.unifesp.maritaca.business.answer.dto;

import java.io.Serializable;

public class LastTimeDataDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long lastAnswerCreationDate;

	private int totalAnswersCollected;
	
	public LastTimeDataDTO(long maxCreationDate, Integer totalAnswers) {
		this.lastAnswerCreationDate = maxCreationDate;
		this.totalAnswersCollected = totalAnswers;
	}

	public long getLastAnswerCreationDate() {
		return lastAnswerCreationDate;
	}
	
	public void setLastAnswerCreationDate(long lastAnswerCreationDate) {
		this.lastAnswerCreationDate = lastAnswerCreationDate;
	}
	
	public int getTotalAnswersCollected() {
		return totalAnswersCollected;
	}
	
	public void setTotalAnswersCollected(int totalAnswersCollected) {
		this.totalAnswersCollected = totalAnswersCollected;
	}
	
}
