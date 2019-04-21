package br.unifesp.maritaca.mobile.util;

public enum RequestStatus {
	
	SUCCESS(0),
	ERROR_SENDING(1),
	ERROR_NO_DATA(2);
	
	private Integer id;

	private RequestStatus(Integer id) {
		this.id = id;
	}
}