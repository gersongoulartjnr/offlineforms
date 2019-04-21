package br.unifesp.maritaca.business.answer.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.persistence.entity.QuestionAnswer;

@XmlRootElement
@XmlSeeAlso({QuestionAnswerMultimediaDTO.class})
public class QuestionAnswerDTO {

	private String id;
	
	private String type;
	
	private String value;
	
	private String subtype;
	
	public QuestionAnswerDTO() { }
	
	public QuestionAnswerDTO(String id, ComponentType type, String value) {
		setId(id);
		setType(type.getValue());
		setValue(value);
	}
	
	public QuestionAnswerDTO(String id, ComponentType type, String value,
			String subtype) {
		this.id = id;
		this.type = type.getValue();
		this.value = value;
		this.subtype = subtype;
	}

	public QuestionAnswerDTO(QuestionAnswer qa){
		setId(qa.getId());
		setValue(qa.getValue());
		setType(qa.getType());
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public QuestionAnswer toBaseInstance(){
		QuestionAnswer qa = new QuestionAnswer();
		qa.setId(getId());
		qa.setType(getType());
		qa.setValue(getValue());
		return qa;
	}

	@XmlAttribute
	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
}