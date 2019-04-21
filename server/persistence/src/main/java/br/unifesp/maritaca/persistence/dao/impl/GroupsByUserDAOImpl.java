package br.unifesp.maritaca.persistence.dao.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.GroupsByUserDAO;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;

@Service("groupByUserDAO")
public class GroupsByUserDAOImpl extends AbstractDAO implements GroupsByUserDAO {

	@Override
	public void addGroupsByUser(UUID groupKey, List<UUID> userByGroupKeys) {
		for(UUID key: userByGroupKeys) {
			MaritacaList mList = emHector.find(MaritacaList.class, key);
			if (mList.getUsers() == null) {
				GroupsByUser gbu = emHector.find(GroupsByUser.class, mList.getKey());
				if (gbu.getGroups().contains(groupKey)) 
					continue;
				gbu.getGroups().add(groupKey);
				emHector.persist(gbu);
			} else {
				this.addGroupsByUser(groupKey, mList.getUsers());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateGroupByUserList(UUID groupKey, List<UUID> oldGroupList,
			List<UUID> newGroupList) {		
		List<UUID> addList = ListUtils.subtract(newGroupList, oldGroupList);
		List<UUID> delList = ListUtils.subtract(oldGroupList, newGroupList);
		if (addList.isEmpty() && delList.isEmpty()) {
			return;
		}
		this.addGroupsByUser(groupKey, addList);
		this.delGroupsByUser(groupKey, delList);
	}

	@Override
	public void delGroupsByUser(UUID groupKey, List<UUID> userByGroupKeys) {
		for(UUID key: userByGroupKeys) {
			MaritacaList mList = emHector.find(MaritacaList.class, key);
			if (mList.getUsers() == null) {
				GroupsByUser gbu = emHector.find(GroupsByUser.class, mList.getKey());
				gbu.getGroups().remove(groupKey);
				emHector.persist(gbu);
			} else {
				this.delGroupsByUser(groupKey, mList.getUsers());
			}
		}
	}

	@Override
	public GroupsByUser getGroupsByUser(UUID key) {
		return emHector.find(GroupsByUser.class, key);
	}
	
	@Override
	public void save(GroupsByUser gbu) {
		emHector.persist(gbu);
	}

}
