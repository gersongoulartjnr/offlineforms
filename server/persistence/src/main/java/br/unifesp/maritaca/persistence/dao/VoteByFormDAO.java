package br.unifesp.maritaca.persistence.dao;

import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.VoteByForm;

public interface VoteByFormDAO {		

	public VoteByForm findVoteFormByKey(UUID formKey);
	
	public VoteByForm persist(VoteByForm voteByForm);
}