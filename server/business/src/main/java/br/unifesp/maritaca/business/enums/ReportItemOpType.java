package br.unifesp.maritaca.business.enums;

public enum ReportItemOpType {

	// list
	LIST_ALL("list_all", "List all"),
	LIST_LAST_N("list_last_n", "List last n"),
	// basic operations
	MIN("min", "Minimum"),
	MAX("max", "Maximum"),
	AVG("avg", "Average"),
	SUM("sum", "Summation"),
	// hash operation
	TOTALBYVALUE("totByVal", "Total by values"), 
	NEAREST_POINTS("nearest_points", "nearest points");
	
	private String stringValue;
	private String description;
	
	private ReportItemOpType(String operation, String description){
		this.stringValue = operation;
		this.description = description;
	}
	
	public String toString() {
		return stringValue;
	}
	
	public String getDescription() {
		return description;
	}

	public static ReportItemOpType getOpType(String operation) {
		for (ReportItemOpType type : ReportItemOpType.values()) {
			if (type.stringValue.equals(operation)) {
				return type;
			}
		}
		throw new RuntimeException("Undefined AnswerOperatorType");
	}
	
	public static String getOpDescription(String operation) {
		for (ReportItemOpType type : ReportItemOpType.values()) {
			if (type.stringValue.equals(operation)) {
				return type.getDescription();
			}
		}
		throw new RuntimeException("Undefined AnswerOperatorType");
	}	
}
