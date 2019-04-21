package br.unifesp.maritaca.analytics.json;

public class ATransformation {

	private int id;
	private int field;
	private String questionType;
	private String type;
	private ASpecificData specificData;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getField() {
		return field;
	}
	public void setField(int field) {
		this.field = field;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ASpecificData getSpecificData() {
		return specificData;
	}
	public void setSpecificData(ASpecificData specificData) {
		this.specificData = specificData;
	}	
}