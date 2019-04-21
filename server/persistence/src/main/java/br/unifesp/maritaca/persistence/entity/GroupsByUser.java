package br.unifesp.maritaca.persistence.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.converter.JSONUUIDConverter;

@Entity
@Table(name="GroupsByUser")
public class GroupsByUser {
	
	@Id
	private UUID key;
	
	@Column(name="groups", converter=JSONUUIDConverter.class)
	private List<UUID> groups;

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public List<UUID> getGroups() {
		return groups;
	}

	public void setGroups(List<UUID> groups) {
		this.groups = groups;
	}
		
}
