package br.unifesp.maritaca.persistence.permission;

public enum RequestStatusType {	
	PENDING(0),
	ACCEPTED(1),
	REJECTED(2),
	IGNORED(3);
	
	private int id;

	private RequestStatusType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static RequestStatusType getInstance(String name){
		for (RequestStatusType p : RequestStatusType.values()) {
			if(p.getId() == Integer.parseInt(name)) {
				return p;
			}
		}
		throw new IllegalArgumentException("No RequestStatus with name, " + name);
	}
}