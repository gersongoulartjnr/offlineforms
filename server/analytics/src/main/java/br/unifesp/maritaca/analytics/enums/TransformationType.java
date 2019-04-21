package br.unifesp.maritaca.analytics.enums;

public enum TransformationType {

	SORT			("SORT"),
	EQUAL  			("EQUAL"),
	NOTEQUAL		("NOTEQUAL"),
	GREATER			("GREATER"),
	LESS			("LESS"),
	GREATER_EQUAL	("GREATER_EQUAL"),
	LESS_EQUAL		("LESS_EQUAL"),
	NFIRSTS			("NFIRSTS"),
	NLASTS			("NLASTS"),
	
	MIN				("MIN"),
	MAX				("MAX"),
	NMIN			("NMIN"),
	NMAX			("NMAX"),
	COUNT			("COUNT"),
	SUM				("SUM"),
	AVERAGE			("AVERAGE"),	
	GROUP_BY		("GROUP_BY");
	
	private String description;	

	private TransformationType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}