package br.unifesp.offlineforms.mobile.util;

public enum HttpMethod {
	GET("get"),
	POST("post"),
	PUT("put"),
	DELETE("delete");
	
	private String value;
	
	private HttpMethod(String value) {
		this.value = value;
	}
	
	public static HttpMethod getValue(String value) {
		for (HttpMethod method : HttpMethod.values()) {
			if (method.toString().equals(value)) {
				return method;
			}
		}
		throw new RuntimeException("Invalid HTTP Method.");
	}

	@Override
	public String toString() {
		return value;
	}
}
