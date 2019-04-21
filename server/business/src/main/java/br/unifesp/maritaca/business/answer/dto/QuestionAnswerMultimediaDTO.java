package br.unifesp.maritaca.business.answer.dto;

import javax.xml.bind.annotation.XmlAttribute;

import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.persistence.entity.QuestionAnswerMultimedia;

public class QuestionAnswerMultimediaDTO extends QuestionAnswerDTO {

	private String thumbnail; // base64

	public QuestionAnswerMultimediaDTO() { }
	
	public QuestionAnswerMultimediaDTO(String id, ComponentType type, String value, String thumbnail) {
		super(id, type, value);
		setThumbnail(thumbnail);
	}
	
	public QuestionAnswerMultimediaDTO(QuestionAnswerMultimedia qam) {
		super(qam);
		setThumbnail(qam.getThumbnail());
	}
	
	@XmlAttribute
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}
