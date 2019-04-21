package br.unifesp.maritaca.analytics.json;

import java.util.List;

public class AElement {

	private int id;
	private String name;
	private AFilter filter;
	private List<ATransformation> transformations;
	private List<ATransformation> sectransformations;
	private AView view;	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AFilter getFilter() {
		return filter;
	}
	public void setFilter(AFilter filter) {
		this.filter = filter;
	}
	public List<ATransformation> getTransformations() {
		return transformations;
	}
	public void setTransformations(List<ATransformation> transformations) {
		this.transformations = transformations;
	}
	public List<ATransformation> getSectransformations() {
		return sectransformations;
	}
	public void setSectransformations(List<ATransformation> sectransformations) {
		this.sectransformations = sectransformations;
	}
	public AView getView() {
		return view;
	}
	public void setView(AView view) {
		this.view = view;
	}
}