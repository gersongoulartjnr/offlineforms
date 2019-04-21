package br.unifesp.maritaca.persistence.dao;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.MaritacaList;

public interface GroupDAO {
	
	public MaritacaList getMaritacaListByKey(UUID mListKey, boolean minimal);
	
	public MaritacaList getMaritacaListByName(String name);
	
	public void saveGroup(MaritacaList group);
	
	public boolean removeGroup(MaritacaList group);

	public Collection<MaritacaList> groupsStartingWith(String startingString);
	
	public List<MaritacaList> getMaritacaListsByOwner(UUID owner);

	// TODO delete
	public List<MaritacaList> getAllGroups();

	public void delete(UUID key);
}