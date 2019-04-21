package br.unifesp.maritaca.persistence.dao.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.FormAccessRequestDAO;
import br.unifesp.maritaca.persistence.entity.FormAccessRequest;

@Service("formAccessRequestDAO")
public class FormAccessRequestDAOImpl extends AbstractDAO implements FormAccessRequestDAO {

	@Override
	public void saveFormAccessRequest(FormAccessRequest formAccessRequest) {
		emHector.persist(formAccessRequest);
	}

	@Override
	public List<FormAccessRequest> getFormAccessRequestsByOwner(UUID ownerKey) {
		return emHector.cQuery(FormAccessRequest.class, "owner", ownerKey.toString());
	}

	@Override
	public List<FormAccessRequest> getFormAccessRequestsByUser(UUID userKey) {
		return emHector.cQuery(FormAccessRequest.class, "user", userKey.toString());
	}
	
	@Override
	public int getTotalRequestByUser(UUID ownerKey) {
		List<FormAccessRequest> reqs = getFormAccessRequestsByOwner(ownerKey);
		if(reqs == null || reqs.isEmpty())
			return 0;
		return reqs.size();
	}

}