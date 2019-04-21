package br.unifesp.maritaca.business.enums;

public enum OrderFormBy {
	TITLE("title"),
	OWNER("owner"),
	DATE("creationDate"),
	POLICY("policy");
	
	private String description;
	
	private OrderFormBy(String description) {
		this.description = description;
	}
	
	public static OrderFormBy getOrderBy(String text){
		OrderFormBy orderBy = OrderFormBy.DATE;
		for(OrderFormBy ob : OrderFormBy.values()){
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