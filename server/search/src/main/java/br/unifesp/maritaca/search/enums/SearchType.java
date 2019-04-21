package br.unifesp.maritaca.search.enums;

public enum SearchType {
	PUBLIC_FORMS(0),
	MY_FORMS(1),
	SHARED_FORMS(2);
	
	private int id;

	private SearchType(int id) {
		this.id = id;
	}
	
	public static SearchType getSearchType(int idType){
		for(SearchType t : SearchType.values()){
			if(t.getId() == idType){
				return t;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}
}