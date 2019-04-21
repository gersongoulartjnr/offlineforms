package br.unifesp.maritaca.ws.api.adapter;

public class MapEntry {

	private String key;
	private String value;

	public MapEntry() {  }

    public MapEntry(String key, String value) {
        this.setKey(key);
        this.setValue(value);
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
