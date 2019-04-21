package br.unifesp.maritaca.business.enums;

public enum ComponentType {
	TEXT		("text"),
	COMBOBOX	("combobox"),
	RADIOBOX	("radio"),
	CHECKBOX	("checkbox"),
	DATE		("date"),
	NUMBER		("number"),
	DECIMAL		("decimal"),
	MONEY		("money"),
	PICTURE		("picture"),
	AUDIO		("audio"),
	VIDEO		("video"),
	GEOLOCATION	("geolocation"),
	SLIDER		("slider"),  
	BARCODE		("barcode"),// implemented until here.
	TIME		("time"),
	TIMESTAMP	("timestamp"),
	TEMPERATURE	("temperature"),
	MOVEMENT	("movement"),
	SIGNAL		("signal");
	
	private String value;
	
	private ComponentType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static ComponentType getComponentType(String value){
		for (ComponentType type : ComponentType.values()) {
			if(type.value.equals(value))
				return type;
		}
		throw new RuntimeException("Undefined ComponentType");
	}
}	
