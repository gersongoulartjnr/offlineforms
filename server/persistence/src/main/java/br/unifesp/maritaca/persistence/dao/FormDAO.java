package br.unifesp.maritaca.persistence.dao;

import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.Form;

public interface FormDAO {
	
	List<Form> getListOwnFormsByUserKey(String userKey);
	
	List<Form> getAllSharedFormsByUserKey(String userKey);
	
	void saveForm(Form form);
	
	boolean verifyIfUrlExist(String url);
	
	Form getFormByKey(UUID formKey, boolean minimal);
	
	Form getFormByUrl(String url, boolean minimal);
	
	void delete(Form form);
	
	byte[] getFileFromFilesystem(String pathFileName);
	
	List<Form> getAllPublicLists(boolean minimal);
	
	//
	List<Form> getAll();
}