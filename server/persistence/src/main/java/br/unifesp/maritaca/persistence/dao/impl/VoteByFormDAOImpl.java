package br.unifesp.maritaca.persistence.dao.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.VoteByFormDAO;
import br.unifesp.maritaca.persistence.entity.VoteByForm;

@Service("voteByFormDAO")
public class VoteByFormDAOImpl extends AbstractDAO implements VoteByFormDAO {
	
	public VoteByForm findVoteFormByKey(UUID formKey) {
		return emHector.find(VoteByForm.class, formKey);		
	}
	
	public VoteByForm persist(VoteByForm voteByForm) {
		try {
			emHector.persist(voteByForm);
			return voteByForm;
		}
		catch(Exception ex) {
			return null;
		}		
	}
}