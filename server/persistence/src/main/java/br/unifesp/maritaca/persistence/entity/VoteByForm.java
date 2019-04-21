package br.unifesp.maritaca.persistence.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;
import br.unifesp.maritaca.persistence.converter.JSONUUIDConverter;

@Entity
@Table(name="VoteByForm")
public class VoteByForm {
	
	@Id
	private UUID key;
	
	@Column(name="like", converter=JSONUUIDConverter.class)
	private List<UUID> usersLike;
	
	@Column(name="dislike", converter=JSONUUIDConverter.class)
	private List<UUID> usersDislike;

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public List<UUID> getUsersLike() {
		return usersLike;
	}

	public void setUsersLike(List<UUID> usersLike) {
		this.usersLike = usersLike;
	}

	public List<UUID> getUsersDislike() {
		return usersDislike;
	}

	public void setUsersDislike(List<UUID> usersDislike) {
		this.usersDislike = usersDislike;
	}
}