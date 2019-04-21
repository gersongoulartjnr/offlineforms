package br.unifesp.maritaca.business.answer.dto;

import javax.xml.bind.annotation.XmlAttribute;

import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.persistence.entity.QuestionAnswerSubType;

public class QuestionAnswerSubTypeDTO extends QuestionAnswerDTO {

	private String subtype;

	public QuestionAnswerSubTypeDTO() {	}
	
	public QuestionAnswerSubTypeDTO(String id, String value, String subtype) {
		super(id, ComponentType.BARCODE, value);
		setSubtype(subtype);
	}
	
	public QuestionAnswerSubTypeDTO(QuestionAnswerSubType qab) {
		super(qab);
		setSubtype(qab.getSubtype());
	}
	
	@XmlAttribute
	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
}
