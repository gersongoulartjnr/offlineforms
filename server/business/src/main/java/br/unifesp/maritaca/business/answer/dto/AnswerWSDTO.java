package br.unifesp.maritaca.business.answer.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AnswerWSDTO {

	private Long timestamp;
		
	private List<QuestionAnswerDTO> questions;
	
	public AnswerWSDTO() { }
	
	@XmlAttribute
	public Long getTimestamp() {
		return timestamp;
	}
	
	@XmlElement(name="question")
	public List<QuestionAnswerDTO> getQuestions() {
		return questions;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public void setQuestions(List<QuestionAnswerDTO> questions) {
		this.questions = questions; 
	}
}
