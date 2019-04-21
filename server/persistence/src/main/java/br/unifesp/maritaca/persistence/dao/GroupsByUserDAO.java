package br.unifesp.maritaca.persistence.dao;

import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.GroupsByUser;

public interface GroupsByUserDAO {

	void addGroupsByUser(UUID groupKey, List<UUID> userGroupKeys);

	void updateGroupByUserList(UUID key, List<UUID> oldGroupList,
			List<UUID> newGroupList);

	void delGroupsByUser(UUID key, List<UUID> userByGroupKeys);

	GroupsByUser getGroupsByUser(UUID key);

	void save(GroupsByUser gbu);

}
