package br.unifesp.maritaca.mobile.dataaccess.model;

import java.io.Serializable;
import java.util.List;

public class MaritacaGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer groupId;
	private Long timestamp;
	private List<MaritacaAnswer> answers;
	
	public MaritacaGroup() {
	}	
	public MaritacaGroup(Integer groupId, Long timestamp) {
		this.groupId = groupId;
		this.timestamp = timestamp;
	}
	public MaritacaGroup(Integer groupId, Long timestamp,
			List<MaritacaAnswer> answers) {
		this.groupId = groupId;
		this.timestamp = timestamp;
		this.answers = answers;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public List<MaritacaAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<MaritacaAnswer> answers) {
		this.answers = answers;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
		MaritacaGroup other = (MaritacaGroup) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		return true;
	}
}