package br.unifesp.maritaca.mobile.model.adaptors;

import java.util.ArrayList;

public class Parent {
	
    private String title;
    private ArrayList<Child> children;
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<Child> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<Child> children) {
		this.children = children;
	}
}