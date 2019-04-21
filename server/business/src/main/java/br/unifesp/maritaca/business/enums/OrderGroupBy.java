package br.unifesp.maritaca.business.enums;

public enum OrderGroupBy {	
	GROUPNAME("name");
	
	private String description;
	
	private OrderGroupBy(String description) {
		this.description = description;
	}
	
	public static OrderGroupBy getOrderBy(String text){
		OrderGroupBy orderBy = OrderGroupBy.GROUPNAME;
		for(OrderGroupBy ob : OrderGroupBy.values()){
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