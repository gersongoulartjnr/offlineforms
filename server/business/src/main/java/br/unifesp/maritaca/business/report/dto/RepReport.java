package br.unifesp.maritaca.business.report.dto;

import java.util.List;

public class RepReport {
	
	private String type;
	private Integer id;
	private String title;
	private String operation;
	private String simple;
	private List<RepMap> map;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getSimple() {
		return simple;
	}
	public void setSimple(String simple) {
		this.simple = simple;
	}
	public List<RepMap> getMap() {
		return map;
	}
	public void setMap(List<RepMap> map) {
		this.map = map;
	}
}