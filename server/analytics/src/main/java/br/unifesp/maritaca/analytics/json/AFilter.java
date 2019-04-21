package br.unifesp.maritaca.analytics.json;

import java.util.List;

public class AFilter {
	
	private AGeolocation geolocation;
	private ADate date;
	private List<String> users;
	private List<Integer> fields;

	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}	
	public AGeolocation getGeolocation() {
		return geolocation;
	}
	public void setGeolocation(AGeolocation geolocation) {
		this.geolocation = geolocation;
	}
	public ADate getDate() {
		return date;
	}
	public void setDate(ADate date) {
		this.date = date;
	}
	public List<Integer> getFields() {
		return fields;
	}
	public void setFields(List<Integer> fields) {
		this.fields = fields;
	}	
}