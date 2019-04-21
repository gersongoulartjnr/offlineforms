package br.unifesp.maritaca.ws.api.resp;

import java.util.Collection;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.unifesp.maritaca.business.answer.dto.AnswerDTO;

@XmlRootElement(name="response")
public class AnswerListResponse extends MaritacaResponse {
	
	private int size;
	
	private Collection<AnswerDTO> answers;
	
	private Collection<String> questions;
	
	public AnswerListResponse() {
		super(Status.OK);
	}
	
	@XmlAttribute
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	@XmlElementWrapper(name="questions")
	@XmlElement(name="question")
	public Collection<String> getQuestions() {
		return questions;
	}
	
	public void setQuestions(Collection<String> questions) {
		this.questions = questions;
	}
	
	@XmlElementWrapper(name="answers")
	@XmlElement(name="answer")
	public Collection<AnswerDTO> getAnswers() {
		return answers;
	}
	
	public void setAnswers(Collection<AnswerDTO> answers) {
		if (answers == null) {
			return;
		}		
		this.answers = answers;
		setSize(answers.size());
	}

}
