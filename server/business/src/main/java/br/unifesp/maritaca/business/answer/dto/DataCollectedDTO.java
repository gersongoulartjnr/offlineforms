package br.unifesp.maritaca.business.answer.dto;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="collecteddata")
public class DataCollectedDTO {

	private String formId;
	
	private String userId;
	
	private List<AnswerWSDTO> answers;
	
	public DataCollectedDTO() {	}
	
	@XmlElement
	public String getFormId() {
		return formId;
	}
	
	public void setFormId(String formId) {
		this.formId = formId;
	}

	@XmlElement
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@XmlElementWrapper(name="answers")
	@XmlElement(name="answer")
	public List<AnswerWSDTO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerWSDTO> answers) {
		this.answers = answers;
	}
	
	@Override
	public String toString(){
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(DataCollectedDTO.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter(); 
			marshaller.marshal(this, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}				
	}
}
