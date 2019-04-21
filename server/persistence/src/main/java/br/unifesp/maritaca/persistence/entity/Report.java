package br.unifesp.maritaca.persistence.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hom.annotations.Column;
import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.LongConverter;
import br.unifesp.maritaca.persistence.converter.UUIDConverter;

@Entity
@Table(name="Report")
public class Report {
	
	@Id
	private UUID key;
	
	@Minimal(indexed=true)
	@Column(name = "user", converter = UUIDConverter.class)
	private UUID user;
	
	@Minimal(indexed=true)
	@Column(name = "form", converter = UUIDConverter.class)
	private UUID form;
	
	@Minimal(indexed=true)
	@Column(name="name")
	private String name;
	
	@Minimal(indexed=true)
	@Column(name="id")
	private String id;
	
	@Column(name="start", converter=LongConverter.class)
	private Long start;
	
	@Column(name="finish", converter=LongConverter.class)
	private Long finish;
	
	@Column(name="xml")
	private String xml;
	
	private MaritacaDate creationDate;

	public Report() {	}

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
		if(key!=null){
			long dl = TimeUUIDUtils.getTimeFromUUID(getKey());
			MaritacaDate date = new MaritacaDate();
			date.setTime(dl);
			creationDate = date;
		}
	}

	public void setKey(String ks) {
		setKey(UUID.fromString(ks));		
	}

	public UUID getUser() {
		return user;
	}

	public void setUser(UUID user) {
		this.user = user;
	}
	
	public UUID getForm() {
		return form;
	}

	public void setForm(UUID form) {
		this.form = form;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public MaritacaDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(MaritacaDate creationDate) {
		this.creationDate = creationDate;
	}
	
	@Override
	public String toString() {
		if (getKey() != null) {
			return getKey().toString();
		}
		return super.toString();
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getFinish() {
		return finish;
	}

	public void setFinish(Long finish) {
		this.finish = finish;
	}

}