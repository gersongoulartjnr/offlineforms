package br.unifesp.maritaca.persistence.entitymanager;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;

import br.unifesp.maritaca.persistence.annotation.Minimal;

@Entity
@Table(name="CFforTesting")
public class CFforTesting{
	
	@Id
	private UUID key;
	
	@Minimal(indexed=true)
	@Column(name="name")
	private String name;
	
	//@Minimal(indexed=true)
	//@Column(name="uuid")
	//private UUID uuid;
	
	@Column(name="bigData")
	private String bigData;

	@Column(name="list")
	private List<UUID> list;
	
	public CFforTesting() {	}
	
	public CFforTesting(UUID key, String name, String bigData, List<UUID> list) {
		this.key = key;
		this.name = name;
		this.bigData = bigData;
		this.list = list;
	}
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
	
	public String getBigData() {
		return bigData;
	}
	
	public void setBigData(String bigData) {
		this.bigData = bigData;
	}
	public List<UUID> getList() {
		return list;
	}
	public void setList(List<UUID> list) {
		this.list = list;
	}	
}