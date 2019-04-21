package br.unifesp.maritaca.business.form.dto;

import br.unifesp.maritaca.business.base.dto.BaseDTO;

public class VoteDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private boolean like;
	private boolean dislike;
	private int numLikes;
	private int numDislikes;
	
	public VoteDTO() {}

	public VoteDTO(boolean like, boolean dislike, int numLikes, int numDislikes) {
		this.like = like;
		this.dislike = dislike;
		this.numLikes = numLikes;
		this.numDislikes = numDislikes;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public boolean isDislike() {
		return dislike;
	}

	public void setDislike(boolean dislike) {
		this.dislike = dislike;
	}

	public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}

	public int getNumDislikes() {
		return numDislikes;
	}

	public void setNumDislikes(int numDislikes) {
		this.numDislikes = numDislikes;
	}
}