package br.unifesp.maritaca.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.permission.Policy;

@Service("formDAO")
public class FormDAOImpl extends AbstractDAO implements FormDAO {

	private static Logger logger = Logger.getLogger(FormDAOImpl.class);
	
	@Override
	public List<Form> getListOwnFormsByUserKey(String userKey) {
		List<Form> forms = emHector.cQuery(Form.class, "user", userKey, false);
		return forms;
	}
	
	@Override
	public List<Form> getAllSharedFormsByUserKey(String userKey) {
		List<Form> forms = new ArrayList<Form>();
		List<Form> publicForms = getAllPublicLists(false);
		if(publicForms != null && !publicForms.isEmpty())
			forms.addAll(publicForms);
		forms.addAll(getListSharedFormsByUserKey(UUID.fromString(userKey)));
		return forms;		
	}
	
	@Override
	public List<Form> getAllPublicLists(boolean minimal) {
		return emHector.cQuery(Form.class, "policy", String.valueOf(Policy.PUBLIC.getId()), minimal);
	}
	
	private List<Form> getListSharedFormsByUserKey(UUID userKey) {	
		List<Form> forms = new ArrayList<Form>();
		
		GroupsByUser groupsByUser = getGroupsByUser(userKey);
		if (groupsByUser != null) {
			for(UUID groupKey : groupsByUser.getGroups()) {
				FormAccessibleByList formAccessByList = getFormAccessibleByUser(groupKey);
				if (formAccessByList == null) {
					continue;
				}
				for(UUID formKey : formAccessByList.getForms()) {
					Form form = emHector.find(Form.class, formKey);
					if(!forms.contains(form)) {
						forms.add(form);
					}
				}
			}
		}
		return forms;
	}
	
	private GroupsByUser getGroupsByUser(UUID userKey) {
		return emHector.find(GroupsByUser.class, userKey);
	}

	private FormAccessibleByList getFormAccessibleByUser(UUID groupKey) {		
		return emHector.find(FormAccessibleByList.class, groupKey);
	}
	
	@Override
	public void saveForm(Form form) {
		emHector.persist(form);
	}
	
	public boolean verifyIfUrlExist(String url) {
		// TODO: improve this
		// look for url in the Form columnFamily
		List<Form> fsList = emHector.cQuery(Form.class, "url", url, true);
		return fsList.size() > 0;
	}
	
	public Form getFormByKey(UUID formKey, boolean minimal) {
		if (formKey == null ) {
			throw new IllegalArgumentException("Incomplete parameters");
		}
		return emHector.find(Form.class, formKey);
	}

	@Override
	public Form getFormByUrl(String url, boolean minimal) {
		List<Form> forms = emHector.cQuery(Form.class, "url", url);
		if (forms == null || forms.size() == 0) {
			return null;
		} else if (forms.size() == 1) {
			return forms.get(0);
		} else {
			//TODO:
			logger.error(url + " " + forms.size());
			return null;
		}
	}
	
	@Override
	public void delete(Form form) {
		emHector.delete(Form.class, form.getKey());
	}

	@Override
	public byte[] getFileFromFilesystem(String pathFileName) {
		return emFileSystem.readFile(pathFileName);
	}

	@Override
	public List<Form> getAll() {
		return emHector.listAll(Form.class, 5000, null, false);
	}
}