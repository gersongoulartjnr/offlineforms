package br.unifesp.maritaca.business.answer.dto;

import java.io.Serializable;

public class ZipFileDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String filename;
	private Object data;
	private String message;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}