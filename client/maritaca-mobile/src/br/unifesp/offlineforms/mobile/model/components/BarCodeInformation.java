package br.unifesp.offlineforms.mobile.model.components;

public class BarCodeInformation {

	private String code;
	private String type;

	public BarCodeInformation(String type, String code) {
		this.code = code;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}