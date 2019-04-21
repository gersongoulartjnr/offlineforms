package br.unifesp.maritaca.persistence.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.GroupDAO;
import br.unifesp.maritaca.persistence.entity.MaritacaList;

@Service("groupDAO")
public class GroupDAOImpl extends AbstractDAO implements GroupDAO {
	
	@Override
	public MaritacaList getMaritacaListByKey(UUID mListKey, boolean minimal) {		
		return emHector.find(MaritacaList.class, mListKey);
	}
	
	@Override
	public MaritacaList getMaritacaListByName(String name) {
		List<MaritacaList> groups = emHector.cQuery(MaritacaList.class, "name", name);
		if (groups == null || groups.size() == 0) {
			return null;
		} else if (groups.size() == 1) {
			return groups.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public void saveGroup(MaritacaList group) {
		emHector.persist(group);
	}
	
	@Override
	public boolean removeGroup(MaritacaList group) {
		return emHector.delete(MaritacaList.class, group.getKey());
	}

	@Override
	public Collection<MaritacaList> groupsStartingWith(String startingString) {
		return emHector.objectsStartingWith(MaritacaList.class, startingString, "getName");
	}
	
	@Override
	public List<MaritacaList> getMaritacaListsByOwner(UUID owner) {
		return emHector.cQuery(MaritacaList.class, "owner", owner.toString());
	}

	@Override
	public List<MaritacaList> getAllGroups() {
		return emHector.listAll(MaritacaList.class);
	}

	@Override
	public void delete(UUID key) {
		emHector.delete(MaritacaList.class, key);
	}	
}