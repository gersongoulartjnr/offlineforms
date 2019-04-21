package br.unifesp.maritaca.persistence.dao;

import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessibleByList;
import br.unifesp.maritaca.persistence.entity.User;

public interface FormAccessibleByListDAO {
	
	public FormAccessibleByList findFormAccesibleByKey(UUID listKey);
	
	public void createOrUpdateFormAccessible(Form form, User owner, List<UUID> deletedLists);
	
	public void deleteFormAccessible(Form form, User owner);
}