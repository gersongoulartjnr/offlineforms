package br.unifesp.maritaca.analytics.json;

public class AGeolocation {
	
	private int field;
	private String transformation;
	private double value;
	private double latitude;
	private double longitude;
	
	public int getField() {
		return field;
	}
	public void setField(int field) {
		this.field = field;
	}
	
	public String getTransformation() {
		return transformation;
	}
	public void setTransformation(String transformation) {
		this.transformation = transformation;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}		
}