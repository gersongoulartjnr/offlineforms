package br.unifesp.maritaca.business.base.dto;

import br.unifesp.maritaca.business.enums.MessageType;

public class Message {
	
	private String message;
	
	private MessageType type;
	
	private Object data;
	
	private Object extra;
	
	public Message() {}
	
	public Message(String message, MessageType type) {
		this.message = message;
		this.type = type;
		this.data = null;
	}	
	public Message(String message, MessageType type, Object data) {
		this.message = message;
		this.type = type;
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

}