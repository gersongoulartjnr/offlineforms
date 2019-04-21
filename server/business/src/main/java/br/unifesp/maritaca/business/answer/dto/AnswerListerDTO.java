package br.unifesp.maritaca.business.answer.dto;

import java.util.List;

/**
 * DTO used to retrieve all the answers for one form.
 * It is used mainly in the "List Answers" view.
 * @author tiagobarabasz
 */
public class AnswerListerDTO {
	
	private List<AnswerDTO> answers;	
	private List<String> questions;	
	private String formTitle;
	
	public List<AnswerDTO> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers;
	}
	public List<String> getQuestions() {
		return questions;
	}
	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}
	public String getFormTitle() {
		return formTitle;
	}
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}		
}