package br.unifesp.maritaca.business.group.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class GroupDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UUID key;
	private String name;
	private String description;
	private UUID owner;
	private String groupsList;
	private String currentGroups;
	
	public UUID getKey() {
		return key;
	}
	public void setKey(UUID key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UUID getOwner() {
		return owner;
	}
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	public String getGroupsList() {
		return groupsList;
	}
	public void setGroupsList(String groupsList) {
		this.groupsList = groupsList;
	}
	public String getCurrentGroups() {
		return currentGroups;
	}
	public void setCurrentGroups(String currentGroups) {
		this.currentGroups = currentGroups;
	}
}