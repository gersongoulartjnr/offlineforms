package br.unifesp.maritaca.business.exception;

import java.util.UUID;

import br.unifesp.maritaca.persistence.permission.Document;
import br.unifesp.maritaca.persistence.permission.Operation;

public class AuthorizationDenied extends MaritacaException{

	private static final long serialVersionUID = 1L;
	
	private static final String LOG_ERROR = "Authorization denied to user %s trying to %s " +
											" the %s of form with id %s";
	
	private Operation  operation;
	private UUID       formId;
	private UUID       userId;
	private String		reportId;
	private Document   doc;
	
	
	public AuthorizationDenied(Document doc, UUID formId, UUID userId, Operation operation){
		setDoc(doc);
		setFormId(formId);
		setUserId(userId);
		setOperation(operation);
	}

	public AuthorizationDenied(Document doc, UUID formId, UUID userId, String reportId, Operation operation) {
		this.operation = operation;
		this.formId = formId;
		this.userId = userId;
		this.reportId = reportId;
		this.doc = doc;
	}

	@Override
	public String getMessage(){
		return String.format(LOG_ERROR, getUserId().toString(),
				doc.toString(), operation.toString(), getFormId().toString(), 
				reportId);
	}
	
	public Operation getOperation() {
		return operation;
	}
	
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public UUID getFormId() {
		return formId;
	}

	public void setFormId(UUID formId) {
		this.formId = formId;
	}
}
