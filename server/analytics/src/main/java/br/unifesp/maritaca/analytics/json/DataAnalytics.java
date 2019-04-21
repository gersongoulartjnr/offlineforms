package br.unifesp.maritaca.analytics.json;

import java.util.List;

public class DataAnalytics {

	private String name;
	private List<AElement> elements;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public List<AElement> getElements() {
		return elements;
	}
	public void setElements(List<AElement> elements) {
		this.elements = elements;
	}
}