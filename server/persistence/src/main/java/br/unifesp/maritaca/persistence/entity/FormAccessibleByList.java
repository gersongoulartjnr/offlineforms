package br.unifesp.maritaca.persistence.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.converter.JSONUUIDConverter;

@Entity
@Table(name="FormAccessibleByList")
public class FormAccessibleByList {
	
	@Id
	private UUID key;
		
	@Column(name="forms", converter=JSONUUIDConverter.class)
	private List<UUID> forms;
	
	public List<UUID> getForms() {
		return forms;
	}

	public void setForms(List<UUID> forms) {
		this.forms = forms;
	}

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}
}