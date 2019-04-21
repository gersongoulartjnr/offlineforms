package br.unifesp.maritaca.persistence.entity;

public class QuestionAnswerMultimedia extends QuestionAnswer {
	
	private static final long serialVersionUID = 1L;
	
	private String thumbnail;

	public QuestionAnswerMultimedia(String id, String value, String type, String thumbnail) {
		super(id, value, type);
		setThumbnail(thumbnail);
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}	
}
