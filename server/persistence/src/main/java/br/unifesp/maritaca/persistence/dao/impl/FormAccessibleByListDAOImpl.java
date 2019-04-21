package br.unifesp.maritaca.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.FormAccessibleByListDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.User;

@Service("formByListDAO")
public class FormAccessibleByListDAOImpl extends AbstractDAO implements FormAccessibleByListDAO {
	
	@Override
	public FormAccessibleByList findFormAccesibleByKey(UUID listKey) {
		return emHector.find(FormAccessibleByList.class, listKey);
	}
	
	@Override
	public void createOrUpdateFormAccessible(Form form, User owner, List<UUID> deletedLists) {
		if(form.getKey() != null && owner != null) {
			//form.getLists().remove(owner.getMaritacaList().getKey());//
			for(UUID uuid : form.getLists()) {
				FormAccessibleByList formsByList = findFormAccesibleByKey(uuid);
				if(formsByList == null) {//TODO:
					formsByList = new FormAccessibleByList();
					formsByList.setKey(uuid);					
						List<UUID> uuidsForm = new ArrayList<UUID>(1);
						uuidsForm.add(form.getKey());
					formsByList.setForms(uuidsForm);
					emHector.persist(formsByList);
				}
				else {
					if(!formsByList.getForms().isEmpty()) {
						if(!formsByList.getForms().contains(form.getKey())) {
							formsByList.getForms().add(form.getKey());
							emHector.persist(formsByList);
						}
					}
				}
			}
			//Delete Form from the List
            if(!deletedLists.isEmpty()) {
                removeLists(deletedLists, form.getKey());
            }
        }
    }
    
    private void removeLists(List<UUID> deletedLists, UUID formKey) {
        for(UUID uuid : deletedLists) {
            FormAccessibleByList formsByList = findFormAccesibleByKey(uuid);            
            if(formsByList != null) {
                if(formsByList.getForms().contains(formKey)) {
                    formsByList.getForms().remove(formKey);
                }
                emHector.persist(formsByList);
            }
        }
    }
	
    @Override
	public void deleteFormAccessible(Form form, User owner) {
		if(form.getKey() != null ) {
			return;
		}
		for(UUID uuid : form.getLists()) {
			FormAccessibleByList formsByList = findFormAccesibleByKey(uuid);
			if(formsByList != null) {
				if(formsByList.getForms().contains(uuid)) {
					List<UUID> temp = new ArrayList<UUID>(1);
					temp.add(uuid);
					formsByList.getForms().removeAll(temp);
					emHector.persist(formsByList);
				}
			}
		}
	}
}