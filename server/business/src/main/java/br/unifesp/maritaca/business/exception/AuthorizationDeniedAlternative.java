package br.unifesp.maritaca.business.exception;

import java.util.UUID;

import br.unifesp.maritaca.persistence.permission.Document;
import br.unifesp.maritaca.persistence.permission.Operation;
import br.unifesp.maritaca.persistence.permission.RequestStatusType;

public class AuthorizationDeniedAlternative extends AuthorizationDenied {

private static final long serialVersionUID = 1L;
	
	private String extraMessage;
	
	private RequestStatusType status;
	
	public AuthorizationDeniedAlternative(Document doc, UUID formId, UUID userId, Operation operation){
		super(doc, formId, userId, operation);
	}

	public String getExtraMessage() {
		return extraMessage;
	}

	public void setExtraMessage(String extraMessage) {
		this.extraMessage = extraMessage;
	}

	public RequestStatusType getStatus() {
		return status;
	}

	public void setStatus(RequestStatusType status) {
		this.status = status;
	}
}
