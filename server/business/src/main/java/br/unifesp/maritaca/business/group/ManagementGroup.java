package br.unifesp.maritaca.business.group;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.base.dto.WrapperGrid;
import br.unifesp.maritaca.business.enums.OrderGroupBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.group.dto.AutocompleteDTO;
import br.unifesp.maritaca.business.group.dto.GroupDTO;

public interface ManagementGroup {
	
	WrapperGrid<GroupDTO> getGroupsByOwner(UserDTO userDTO, 
			OrderGroupBy orderBy,
			OrderType orderType,
			int page, 
			int numRows);
	
	Message saveGroup(GroupDTO group, UserDTO currentUser);
	
	Message removeGroupByKey(UserDTO userDTO, String groupKey);
	
	GroupDTO searchGroupByKey(UserDTO userDTO, String groupKey);
	
	List<AutocompleteDTO> getGroupsByName(UserDTO userDTO, String prefix);
	
	List<AutocompleteDTO> getGroupsUserByName(UserDTO userDTO, String prefix);

	String getListnamesInJSON(UserDTO currentUser);

	List<UUID> getGroupsFromString(String groupsList);

	Map<String, String> getUsersListByKeys(List<UUID> keys);

	String getGroupsUser(UserDTO userDTO);

	String loadUsersGroup(String key, UserDTO currentUser);

	/**
	 * Get a list with the usernames(emails) by group key in MaritacaList entity,
	 * if group key is of an user it will return just an email, but that group key is 
	 * of another MaritacaList it will return the emails of users field for that group.
	 * 
	 * @param groupKey
	 * @return A list of usernames(emails) 
	 */
	public List<String> getUsernamesByGroupKey(UUID groupKey);	
}