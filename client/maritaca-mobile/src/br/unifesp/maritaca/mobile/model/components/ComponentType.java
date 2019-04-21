package br.unifesp.maritaca.mobile.model.components;

public enum ComponentType {
	TEXT		("text"),
	COMBOBOX	("combobox"),
	CHECKBOX	("checkbox"),
	RADIOBOX	("radio"),
	DATE		("date"),
	NUMBER		("number"),
	DECIMAL		("decimal"),
	MONEY		("money"),
	PICTURE		("picture"),
	AUDIO		("audio"),
	VIDEO		("video"),
	GEOLOCATION	("geolocation"),
	SLIDER		("slider"),
	BARCODE		("barcode"),
	DRAW        ("draw"),
	TIME		("time"),
	TIMESTAMP	("timestamp"),
	TEMPERATURE	("temperature"),
	MOVEMENT	("movement"),
	SIGNAL		("signal");
	
	private String description;
	
	private ComponentType(String description){
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public static ComponentType getComponentTypeByDescription(String description){
		for (ComponentType type : ComponentType.values()) {
			if(type.description.equals(description))
				return type;
		}
		return null;
	}
}