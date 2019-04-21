package br.unifesp.maritaca.business.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.base.dto.WrapperGrid;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.enums.OrderGroupBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.group.dto.AutocompleteDTO;
import br.unifesp.maritaca.business.group.dto.GroupDTO;
import br.unifesp.maritaca.business.util.UtilsBusiness;
import br.unifesp.maritaca.persistence.dao.GroupDAO;
import br.unifesp.maritaca.persistence.dao.GroupsByUserDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.User;

@Service("managementGroup")
public class ManagementGroupImpl extends AbstractBusiness implements ManagementGroup {
	
	private static Logger logger = Logger.getLogger(ManagementGroupImpl.class);
	
	@Autowired private GroupDAO groupDAO;
	@Autowired private UserDAO userDAO;
	@Autowired private GroupsByUserDAO groupsByUserDAO;
	
	@Override
	public WrapperGrid<GroupDTO> getGroupsByOwner(UserDTO userDTO, OrderGroupBy orderBy,
			OrderType orderType, int page, int numRows) {		
		List<GroupDTO> listGroups = getListOwnGroups(userDTO);
		int groupsSize = (listGroups == null ? 0 : listGroups.size());
		listGroups = truncateGroupsList(listGroups, orderBy, orderType, page, numRows);
		return getWrapperGroups(listGroups, orderBy, orderType, page, numRows, groupsSize);
	}

	@Override
	public Message saveGroup(GroupDTO group, UserDTO currentUser) {
		logger.info("saving group " + group.getName());
		if(group.getKey() == null) {
			return createGroup(group, currentUser);
		} else {
			return updateGroup(group, currentUser);
		}
	}

	@Override
	public Message removeGroupByKey(UserDTO userDTO, String groupKey) {
		Message message = new Message();
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		MaritacaList group = groupDAO.getMaritacaListByKey(UUID.fromString(groupKey), true);
		if(group != null && group.getOwner().toString().equals(user.getKey().toString())) {
			if(groupDAO.removeGroup(group)) {
				groupsByUserDAO.delGroupsByUser(group.getKey(), group.getUsers());
				message.setType(MessageType.SUCCESS);
				message.setMessage(getText("list_delete_sucess"));
			} else {
				message.setType(MessageType.ERROR);
				message.setMessage(getText("list_delete_fail"));
			}
		}		
		return message;
	}

	@Override
	public GroupDTO searchGroupByKey(UserDTO userDTO, String groupKey) {
		GroupDTO groupDTO = null;		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			throw new MaritacaException(userDTO.getUsername() + " does not exist");
		}
		
		MaritacaList group = groupDAO.getMaritacaListByKey(UUID.fromString(groupKey), true);
		if(group != null && group.getOwner().toString().equals(user.getKey().toString())) {
			groupDTO = new GroupDTO();
			groupDTO.setKey(group.getKey());
			groupDTO.setName(group.getName());
			groupDTO.setDescription(group.getDescription());
			Map<String, String> groupsList = this.getUsersListByKeys(group.getUsers());
			groupDTO.setGroupsList(groupsList == null ? "" : (new Gson()).toJson(groupsList));
		}		
		return groupDTO;
	}
	
	@Override
	public List<AutocompleteDTO> getGroupsByName(UserDTO userDTO, String prefixName) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			throw new MaritacaException(userDTO.getUsername() + " does not exist");
		}
		
		Collection<MaritacaList> groups = groupDAO.groupsStartingWith(prefixName);
		List<AutocompleteDTO> groupsDTO = new ArrayList<AutocompleteDTO>();
		for(MaritacaList group : groups) {
			if (group.getUsers() != null
					|| (group.getUsers() == null && group.getOwner().equals(user.getKey()))) {
				continue;
			}
			groupsDTO.add(new AutocompleteDTO(group.getKey().toString(), group.getName()));
		}
		return groupsDTO;
	}
	
	@Override
	public List<AutocompleteDTO> getGroupsUserByName(UserDTO userDTO, String prefix) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			throw new MaritacaException(userDTO.getUsername() + " does not exist");
		}
		
		Collection<MaritacaList> groups = groupDAO.groupsStartingWith(prefix);
		List<AutocompleteDTO> groupsDTO = new ArrayList<AutocompleteDTO>();
		for(MaritacaList group : groups) {
			if ((group.getUsers() == null && group.getOwner().equals(user.getKey()))
					|| (group.getUsers() != null && !group.getOwner().equals(user.getKey()))) {
				continue;
			}
			groupsDTO.add(new AutocompleteDTO(group.getKey().toString(), group.getName()));
		}
		return groupsDTO;
	}

	@Override
	public String getListnamesInJSON(UserDTO userDTO) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			throw new MaritacaException(userDTO.getUsername() + " does not exist");
		}
		
		GroupsByUser gbu = groupsByUserDAO.getGroupsByUser(user.getKey());
		List<String> list = new ArrayList<String>();
		list.add(user.getEmail());
		if (gbu != null && gbu.getGroups() != null) {
			for (UUID key : gbu.getGroups()) {
				list.add(key.toString());
			}
		}		
		return (new Gson()).toJson(list);
	}
	
	@Override
	public List<UUID> getGroupsFromString(String groupsList) {
		if(groupsList == null || "".equals(groupsList))
			return null;
		List<UUID> groups = new ArrayList<UUID>();
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> strGroups = (new Gson()).fromJson(groupsList, HashMap.class);
		for(Map.Entry<String, String> entry: strGroups.entrySet()) {
			MaritacaList mList = groupDAO.getMaritacaListByKey(UUID.fromString(entry.getKey()), false);
			if(mList != null) {
				groups.add(mList.getKey());
			}
		}		
		return groups;
	}
	
	@Override
	public Map<String, String> getUsersListByKeys(List<UUID> keys) {
		if(keys == null || keys.isEmpty()) {
			return null;
		}
		HashMap<String, String> groups = new HashMap<String, String>();
		for(UUID uuid : keys) {
			MaritacaList group = groupDAO.getMaritacaListByKey(uuid, true);
			if(group != null) {
				groups.put(group.getKey().toString(), group.getName());
			}
		}
		return groups;
	}
	
	private Message createGroup(GroupDTO groupDTO, UserDTO currentUser) {
		Message message = new Message();
		User user = userDAO.findUserByEmail(currentUser.getUsername());
		if(user != null) {
			MaritacaList group = new MaritacaList();
			group.setName(groupDTO.getName());
			group.setDescription(groupDTO.getDescription());
			group.setOwner(user.getKey());
			List<UUID> userGroupKeys = getGroupsFromString(groupDTO.getGroupsList());
			group.setUsers(userGroupKeys);
			groupDAO.saveGroup(group);
			
			groupsByUserDAO.addGroupsByUser(group.getKey(), userGroupKeys);
			if(group.getKey() != null) {
				message.setType(MessageType.SUCCESS);
				message.setMessage(getText("list_add_sucess"));
				message.setData(group.getKey());
			} else {
				message.setType(MessageType.ERROR);
				message.setMessage(getText("list_add_fail"));
			}
		} else {
			message.setType(MessageType.ERROR);
			message.setMessage(getText("exception_internal_server_error"));
		}
		return message;
	}
	
	private Message updateGroup(GroupDTO groupDTO, UserDTO currentUser) {
		Message message = new Message();
		User user = userDAO.findUserByEmail(currentUser.getUsername());
		MaritacaList group = groupDAO.getMaritacaListByKey(groupDTO.getKey(), true);//TODO: usersList?
		if(user != null && group != null && user.getKey().toString().equals(group.getOwner().toString())) {
			List<UUID> oldGroupList = group.getUsers();
			
			group.setName(groupDTO.getName());
			group.setDescription(groupDTO.getDescription());
			List<UUID> newGroupList = getGroupsFromString(groupDTO.getGroupsList());
			group.setUsers(newGroupList);			
			groupDAO.saveGroup(group);
			
			groupsByUserDAO.updateGroupByUserList(group.getKey(), oldGroupList, newGroupList);
			if(group.getKey() != null) {
				message.setType(MessageType.SUCCESS);
				message.setMessage(getText("list_update_sucess"));
				message.setData(group.getKey());
			}
			else {
				message.setType(MessageType.ERROR);
				message.setMessage(getText("list_update_fail"));
			}
		}		
		else {
			message.setType(MessageType.ERROR);
			message.setMessage(getText("exception_internal_server_error"));
		}
		return message;
	}
	
	private List<GroupDTO> truncateGroupsList(List<GroupDTO> groupDTO, OrderGroupBy orderBy, OrderType orderType, int page, int numRows){
		if(groupDTO == null)
			return null;
		else {
			UtilsBusiness.sortGrid(groupDTO, orderBy, orderType);
			return UtilsBusiness.pagingGrid(groupDTO, page, numRows);
		}
	}
	
	private WrapperGrid<GroupDTO> getWrapperGroups(List<GroupDTO> groups, OrderGroupBy orderBy,
			OrderType orderType, int page, int numRows, int totalRows) {
						
		WrapperGrid<GroupDTO> wrapper = new WrapperGrid<GroupDTO>();
		wrapper.setRows(groups);
		wrapper.setNumRows(numRows);
		wrapper.setCurrentNumRows((groups == null ? 0 : groups.size()));
		wrapper.setCurrentPage(getPage(page));
		wrapper.setOrderBy(orderBy.getDescription());
		wrapper.setOrderType(orderType.getDescription());
		wrapper.setNumPages(getTotalNumPages(totalRows, numRows));
		wrapper.setTotal(totalRows);
		
		return wrapper;	
	}	
	
	private List<GroupDTO> getListOwnGroups(UserDTO userDTO) {		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user != null) {
			List<MaritacaList> groups = groupDAO.getMaritacaListsByOwner(user.getKey());						
			List<GroupDTO> groupsDTO = new ArrayList<GroupDTO>();			
			for(MaritacaList group : groups) {
				if(!userDTO.getUsername().equals(group.getName())) {
					GroupDTO groupDTO = new GroupDTO();
					groupDTO.setKey(group.getKey());
					groupDTO.setName(group.getName());
					groupDTO.setDescription(group.getDescription()!= null?group.getDescription():"");
					groupsDTO.add(groupDTO);
				}
			}		
			return groupsDTO;
		}
		return null;
	}

	@Override
	public String getGroupsUser(UserDTO userDTO) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		HashMap<String, String> groups = new HashMap<String, String>();
		if(user != null) {
			List<MaritacaList> _groups = groupDAO.getMaritacaListsByOwner(user.getKey());
			for(MaritacaList group : _groups) {
				if (group.getKey().equals(user.getKey())) {
					continue;
				}
				groups.put(group.getKey().toString(), group.getName());
			}
		}
		return (new Gson()).toJson(groups);
	}

	@Override
	public String loadUsersGroup(String key, UserDTO userDTO) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		HashMap<String, String> groups = new HashMap<String, String>();
		if(user != null) {
			MaritacaList group = groupDAO.getMaritacaListByKey(UUID.fromString(key), false);
			MaritacaList userGroup;
			for(UUID _user : group.getUsers()) {
				userGroup = groupDAO.getMaritacaListByKey(_user, false);
				if (userGroup != null && userGroup.getUsers() == null) {
					groups.put(userGroup.getKey().toString(), userGroup.getName());
				}
			}
		}
		return (new Gson()).toJson(groups);
	}

	@Override
	public List<String> getUsernamesByGroupKey(UUID groupKey) {
		MaritacaList group = groupDAO.getMaritacaListByKey(groupKey, false);
		if(group == null)
			return null;
		
		if(group.getUsers() == null || group.getUsers().isEmpty()){
			User user = userDAO.findUserByKey(group.getOwner());
			if(user == null){
				return null;
			} else {
				List<String> users = new ArrayList<String>(1);
				users.add(user.getEmail());
				return users;
			}
		} else {
			List<String> users = new ArrayList<String>(group.getUsers().size());
			for(UUID key : group.getUsers()){
				User user = userDAO.findUserByKey(key);
				if(user != null){
					users.add(user.getEmail());
				}
			}
			return users;
		}
	}	
}