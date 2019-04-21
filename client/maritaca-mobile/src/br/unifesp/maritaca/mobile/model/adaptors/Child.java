package br.unifesp.maritaca.mobile.model.adaptors;

public class Child {
	
	private String name;
	private String url;	
	
	public Child(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}