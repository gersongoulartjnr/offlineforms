package br.unifesp.maritaca.business.test.maritacalist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.BusinessTest;
import br.unifesp.maritaca.business.account.ManagementAccount;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.group.ManagementGroup;
import br.unifesp.maritaca.business.group.dto.AutocompleteDTO;
import br.unifesp.maritaca.business.group.dto.GroupDTO;
import br.unifesp.maritaca.persistence.dao.GroupDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.User;

import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class MaritacaListTests extends BusinessTest {
	
	protected static final String UUID_1 = "111dea60-146e-11e1-a7c0-d2b70b6d4d67";
	
	private User user1;
	private User user2;
	private UUID groupKey;
	private UserDTO userDTO1;
	private UserDTO userDTO2;
	
	@Autowired private ManagementGroup managementGroup;
	@Autowired private ManagementAccount account;
	@Autowired private UserDAO userDAO;
	@Autowired private GroupDAO groupDAO;
	
	private GroupDTO getGroupDTO() {
		GroupDTO groupDTO 	= new GroupDTO();		
		groupDTO.setName(LIST_ONE_NAME);
		groupDTO.setOwner(user1.getKey());
		HashMap<String, String> groups = new HashMap<String, String>();
		MaritacaList mList = groupDAO.getMaritacaListByName(USER1_EMAIL);
		groups.put(mList.getKey().toString(), USER1_EMAIL);		
		mList = groupDAO.getMaritacaListByName(USER2_EMAIL);
		groups.put(mList.getKey().toString(), USER2_EMAIL);
		groupDTO.setGroupsList((new Gson()).toJson(groups));
		return groupDTO;
	}
	
	private void saveAccount() {
		account.saveNewAccount(super.getAccountDTO(COD_USER1));
		user1 = userDAO.findUserByEmail(USER1_EMAIL);
		account.saveNewAccount(super.getAccountDTO(COD_USER2));
		user2 = userDAO.findUserByEmail(USER2_EMAIL);
	}
	
	private void createGroup() {
		saveAccount();		
		GroupDTO groupDTO = getGroupDTO();		
		Message message = managementGroup.saveGroup(groupDTO, userDTO1);
		if(message != null && message.getData() != null) {
			groupKey = UUID.fromString(message.getData().toString());
		}
	}
	
	@Before
	public void setUp() {
		super.setProperties();
		emHector.createColumnFamily(User.class);
		emHector.createColumnFamily(MaritacaList.class);
		emHector.createColumnFamily(GroupsByUser.class);
	 	userDTO1 = new UserDTO(USER1_EMAIL);
	 	userDTO2 = new UserDTO(USER2_EMAIL);
	}
	
	@After
	public void cleanUp(){
		super.clearProperties();
		emHector.dropColumnFamily(User.class);
		emHector.dropColumnFamily(MaritacaList.class);
	}
	
	@Test
	public void testCreateGroup(){
		saveAccount();		
		GroupDTO groupDTO = getGroupDTO();		
		Message message = managementGroup.saveGroup(groupDTO, userDTO1);
		Assert.assertNotNull(message);
		Assert.assertEquals(message.getType(), MessageType.SUCCESS);
	}
	
	@Test
	public void testFindGroup(){
		createGroup();
		GroupDTO groupDTO = null;
		groupDTO = managementGroup.searchGroupByKey(userDTO1, UUID_1);
		Assert.assertNull(groupDTO);
		
		groupDTO = managementGroup.searchGroupByKey(userDTO1, groupKey.toString());
		
		Assert.assertNotNull(groupDTO);
		Assert.assertEquals(groupDTO.getKey(), groupKey);
	}
	
	@Test
	public void testRemoveGroup(){
		createGroup();
		GroupDTO groupDTO = null;		
		groupDTO = managementGroup.searchGroupByKey(userDTO1, groupKey.toString());		
		Assert.assertNotNull(groupDTO);
		Assert.assertEquals(groupDTO.getKey(), groupKey);
		
		managementGroup.removeGroupByKey(userDTO1, groupKey.toString());
		GroupDTO removeGroupDTO = managementGroup.searchGroupByKey(userDTO1, groupKey.toString());
		Assert.assertNull(removeGroupDTO);
	}
	
	@Test
	public void testListByUser() {
		createGroup();
		MaritacaList group = groupDAO.getMaritacaListByKey(groupKey, false);		
		Assert.assertTrue(group.getUsers().size() == 2);
	}
	
	@Test
	public void testSearchUserList() {
		GroupDTO groupDTO = null;
		groupDTO = managementGroup.searchGroupByKey(userDTO1, UUID_1);
		Assert.assertNull(groupDTO);
		
		createGroup();
		groupDTO = managementGroup.searchGroupByKey(userDTO1, groupKey.toString());
		
		Assert.assertNotNull(groupDTO);
		Assert.assertEquals(groupDTO.getKey(), groupKey);
		Assert.assertEquals(groupDTO.getName(), LIST_ONE_NAME);
	}
	
	@Test
	public void testGetGroupsByNameEmptyList() {
		createGroup();
		String prefix = "nada";
		List<AutocompleteDTO> groups = managementGroup.getGroupsUserByName(userDTO1, prefix);
		Assert.assertTrue(groups.size() == 0);
	}
	
	@Test
	public void testGetGroupsByName() {
		createGroup();
		String prefix = LIST_ONE_NAME.substring(0,4);
		List<AutocompleteDTO> groups = managementGroup.getGroupsUserByName(userDTO1, prefix);
		List<String> values = new ArrayList<String>();
		for (AutocompleteDTO group : groups) {
			values.add(group.getLabel());
		}
		Assert.assertFalse(values.contains(USER1_EMAIL));
		Assert.assertTrue(values.contains(USER2_EMAIL));
		Assert.assertTrue(values.contains(LIST_ONE_NAME));
	}
	
	@Test
	public void testGetGroupsUserByNameOwnerGroup () {
		createGroup();
		String prefix = LIST_ONE_NAME.substring(0,4);
		List<AutocompleteDTO> groups = managementGroup.getGroupsUserByName(userDTO1, prefix);
		List<String> values = new ArrayList<String>();
		for (AutocompleteDTO group : groups) {
			values.add(group.getLabel());
		}
		Assert.assertTrue(values.contains(LIST_ONE_NAME));
		Assert.assertFalse(values.contains(USER1_EMAIL));
		Assert.assertTrue(values.contains(USER2_EMAIL));
	}
	
	@Test
	public void testGetGroupsUserByNameNoOwnerGroup () {
		createGroup();
		String prefix = LIST_ONE_NAME.substring(0,4);
		List<AutocompleteDTO> groups = managementGroup.getGroupsUserByName(userDTO2, prefix);
		List<String> values = new ArrayList<String>();
		for (AutocompleteDTO group : groups) {
			values.add(group.getLabel());
		}
		Assert.assertFalse(values.contains(LIST_ONE_NAME));
		Assert.assertTrue(values.contains(USER1_EMAIL));
		Assert.assertFalse(values.contains(USER2_EMAIL));
	}

	@Test
	public void testGetListnamesInJSON() {
		createGroup();	
		String json = managementGroup.getListnamesInJSON(new UserDTO(user1.getEmail()));
		@SuppressWarnings("rawtypes")
		List list = (new Gson()).fromJson(json, List.class);		
		Assert.assertTrue(list.contains(user1.getKey().toString()));
		Assert.assertTrue(list.contains(groupKey.toString()));
	}


	@Test
	public void testGetListnamesInJSONByNoOwner() {
		createGroup();	
		String json = managementGroup.getListnamesInJSON(new UserDTO(USER2_EMAIL));
		@SuppressWarnings("rawtypes")
		List list = (new Gson()).fromJson(json, List.class);
		Assert.assertFalse(list.contains(user1.getKey().toString()));
		Assert.assertTrue(list.contains(user2.getKey().toString()));
		Assert.assertTrue(list.contains(groupKey.toString()));
	}
	
}
