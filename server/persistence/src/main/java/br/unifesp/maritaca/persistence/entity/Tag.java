package br.unifesp.maritaca.persistence.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;
import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.IntegerConverter;

@Entity
@Table(name="Tag")
public class Tag {

	@Id
	private UUID key;
	
	@Minimal(indexed=true)
	@Column(name="text")
	private String text;
	
	@Minimal(indexed=true)
	@Column(name="weight", converter=IntegerConverter.class)
	private Integer weight;

	public Tag() {	}
	
	public Tag(UUID key, String text) {
		setKey(key);
		setText(text);
	}

	public Tag(String tagText) {
		setText(tagText);
		setWeight(0);
	}

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
}
