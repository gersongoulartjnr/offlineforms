package br.unifesp.maritaca.mobile.dataaccess.model;

import java.io.Serializable;

public class MaritacaAnswer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String type;
	private String subtype;
	private String value;
	
	public MaritacaAnswer() {
	}
	
	public MaritacaAnswer(Integer id) {
		this.id = id;
	}

	public MaritacaAnswer(Integer id, String type, String subtype, String value) {
		this.id = id;
		this.type = type;
		this.subtype = subtype;
		this.value = value;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaritacaAnswer other = (MaritacaAnswer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}