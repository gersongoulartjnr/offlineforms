package br.unifesp.maritaca.persistence.entity;

public class QuestionAnswerSubType extends QuestionAnswer {

	private static final long serialVersionUID = 1L;
	
	private String subtype;
	
	public QuestionAnswerSubType(String id, String value, String type, String subtype) {
		super(id, value, type);
		setSubtype(subtype);
	}
	
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	
	public String getSubtype() {
		return subtype;
	}
}