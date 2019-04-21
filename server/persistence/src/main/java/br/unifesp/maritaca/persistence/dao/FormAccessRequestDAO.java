package br.unifesp.maritaca.persistence.dao;

import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.FormAccessRequest;

public interface FormAccessRequestDAO {
	
	void saveFormAccessRequest(FormAccessRequest formAccessRequest);

	List<FormAccessRequest> getFormAccessRequestsByOwner(UUID ownerKey);

	int getTotalRequestByUser(UUID ownerKey);

	List<FormAccessRequest> getFormAccessRequestsByUser(UUID userKey);
}