package br.unifesp.maritaca.business.enums;

public enum OrderAnswerBy {
	AUTHOR("name"),
	DATE("strCreationDate");
	
	private String description;
	
	private OrderAnswerBy(String description) {
		this.description = description;
	}
	
	public static OrderAnswerBy getOrderBy(String text){
		OrderAnswerBy orderBy = OrderAnswerBy.DATE;
		for(OrderAnswerBy ob : OrderAnswerBy.values()){
			if(ob.getDescription().equals(text)){
				orderBy = ob;
				break;
			}
		}
		return orderBy;
	}

	public String getDescription() {
		return description;
	}	
}