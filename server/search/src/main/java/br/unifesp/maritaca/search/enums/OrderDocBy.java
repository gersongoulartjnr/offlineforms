package br.unifesp.maritaca.search.enums;

public enum OrderDocBy {

	TITLE("form_title", "title"),
	OWNER("formn_owner", "owner"),
	DATE("form_creation_date", "creationDate"),
	POLICY("form_policy", "policy");
	
	private String docDescription;
	private String formDescription;
	
	private OrderDocBy(String docDescription, String formDescription) {
		this.docDescription = docDescription;
		this.formDescription = formDescription;
	}
	
	public static OrderDocBy getOrderBy(String text){
		OrderDocBy orderBy = OrderDocBy.DATE;
		for(OrderDocBy ob : OrderDocBy.values()){
			if(ob.getFormDescription().equals(text)){
				orderBy = ob;
				break;
			}
		}
		return orderBy;
	}

	public String getDocDescription() {
		return docDescription;
	}
	public String getFormDescription() {
		return formDescription;
	}
}