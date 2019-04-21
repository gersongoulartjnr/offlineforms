package br.unifesp.maritaca.business.tag;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class TagsWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<UUID> keys;
	private List<String> texts;

	public List<UUID> getKeys() {
		return keys;
	}
	public void setKeys(List<UUID> keys) {
		this.keys = keys;
	}

	public List<String> getTexts() {
		return texts;
	}
	public void setTexts(List<String> texts) {
		this.texts = texts;
	}
}