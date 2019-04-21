package br.unifesp.maritaca.search.enums;

public enum Policy {
	PRIVATE(0),
	SHARED_HIERARCHICAL(1),
	SHARED_SOCIAL(2),
	PUBLIC(3);

	private Integer idPolicy;
	
	private Policy(Integer idPolicy) {
		this.idPolicy = idPolicy;
	}

	public Integer getId() {
		return idPolicy;
	}
}