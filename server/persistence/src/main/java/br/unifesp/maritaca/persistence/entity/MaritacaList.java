package br.unifesp.maritaca.persistence.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.JSONUUIDConverter;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;

@Entity
@Table(name="MaritacaList")
public class MaritacaList {
	
	@Id
	private UUID key;
	
	@Minimal(indexed = true)
	@Column(name="owner", converter=UUIDConverter.class)
	private UUID owner;

	@Minimal(indexed = true)
	@Column(name="name")
	private String name;

	@Minimal
	@Column(name="description")
	private String description;
	
	@Column(name="users", converter=JSONUUIDConverter.class)
	private List<UUID> users;

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public void setKey(String ks) {
		if (ks == "")
			this.key = null;
		this.key = UUID.fromString(ks);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		if (getKey() != null) {
			return getKey().toString();
		}
		return super.toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MaritacaList){
			MaritacaList grp = (MaritacaList)obj;
			return grp.getKey().equals(getKey());
		} else {
			return false;
		}
	}

	public List<UUID> getUsers() {
		return users;
	}

	public void setUsers(List<UUID> users) {
		this.users = users;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}
}
