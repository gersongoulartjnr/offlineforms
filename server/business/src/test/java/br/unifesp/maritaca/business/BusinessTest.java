package br.unifesp.maritaca.business;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import br.unifesp.maritaca.business.account.ManagementAccount;
import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.group.ManagementGroup;
import br.unifesp.maritaca.business.group.dto.GroupDTO;
import br.unifesp.maritaca.persistence.EntityManagerHector;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;
import br.unifesp.maritaca.persistence.dao.GroupDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.util.ConstantsTest;

public abstract class BusinessTest extends BaseEmbededServerSetupTest {

	@Autowired protected EntityManagerHector emHector;
	@Autowired protected ManagementAccount account;
	@Autowired protected ManagementGroup managementGroup;
	@Autowired protected UserDAO userDAO;
	@Autowired protected GroupDAO groupDAO;

	protected static final int page = 1;
	protected static final int numRows = 10;

	protected static final String LIST_ONE_NAME = "userListOne";

	protected static final String COD_USER1 = "COD_USER1";
	protected static final String COD_USER2 = "COD_USER2";
	protected static final String COD_GROUP1 = "COD_GROUP1";
	protected static final String COD_GROUP2 = "COD_GROUP2";

	protected static final String USER1_FIRSTNAME = "user1";
	protected static final String USER1_LASTNAME = "user1lastname";
	protected static final String USER1_EMAIL = "user1@domain.com";
	protected static final String USER1_PASSWORD = "user1";

	protected static final String USER2_FIRSTNAME = "user2";
	protected static final String USER2_LASTNAME = "user2lastname";
	protected static final String USER2_EMAIL = "user2@domain.com";
	protected static final String USER2_PASSWORD = "user2";

	protected void setProperties() {
		System.setProperty(ConstantsTest.SYS_PROP_KEY_TEST, ConstantsTest.SYS_PROP_VALUE_TEST);
	}

	protected void clearProperties() {
		System.clearProperty(ConstantsTest.SYS_PROP_KEY_TEST);
	}

	protected AccountDTO getAccountDTO(String codUser) {
		if (codUser == COD_USER2) {
			return getAccountDTOWithParams(USER2_FIRSTNAME, 
										   USER2_LASTNAME,
										   USER2_EMAIL, 
										   USER2_PASSWORD);
		}
		return getAccountDTOWithParams(USER1_FIRSTNAME, 
									   USER1_LASTNAME, 
									   USER1_EMAIL, 
									   USER1_PASSWORD);
	}

	private AccountDTO getAccountDTOWithParams(String firstname, String lastname, 
			String email, String password) {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setFirstName(firstname);
		accountDTO.setLastName(lastname);
		accountDTO.setEmail(email);
		accountDTO.setPassword(password);
		return accountDTO;
	}

	protected UUID createGroup(GroupDTO groupDTO, UserDTO userDTO) {
		Message message = managementGroup.saveGroup(groupDTO, userDTO);
		if (message != null && message.getData() != null) {
			return UUID.fromString(message.getData().toString());
		}
		return null;
	}

	protected String getGroupsList(String codGroup) {
		if (codGroup == COD_GROUP2) {
			return null;
		}
		HashMap<String, String> groups = new HashMap<String, String>();
		MaritacaList mList = groupDAO.getMaritacaListByName(USER1_EMAIL);
		groups.put(mList.getKey().toString(), USER1_EMAIL);

		return (new Gson()).toJson(groups);
	}

	protected GroupDTO getGroupDTO(String codUser) {
		HashMap<String, String> groups = new HashMap<String, String>();

		MaritacaList mList;
		User user;
		if (codUser == COD_USER2) {
			mList = groupDAO.getMaritacaListByName(USER1_EMAIL);
			groups.put(mList.getKey().toString(), USER1_EMAIL);
			user = userDAO.findUserByEmail(USER2_EMAIL);
			return getGroupDTOWithParams(LIST_ONE_NAME, user, groups);
		}
		mList = groupDAO.getMaritacaListByName(USER2_EMAIL);
		groups.put(mList.getKey().toString(), USER2_EMAIL);
		user = userDAO.findUserByEmail(USER1_EMAIL);
		return getGroupDTOWithParams(LIST_ONE_NAME, user, groups);
	}

	private GroupDTO getGroupDTOWithParams(String listName, User user,
			Map<String, String> groups) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName(listName);
		groupDTO.setOwner(user.getKey());
		groupDTO.setGroupsList((new Gson()).toJson(groups));
		return groupDTO;
	}

	protected void createUserAndList() {
		account.saveNewAccount(getAccountDTO(COD_USER1));
		account.saveNewAccount(getAccountDTO(COD_USER2));
		createGroup(getGroupDTO(COD_USER1), new UserDTO(USER1_EMAIL));
		createGroup(getGroupDTO(COD_USER2), new UserDTO(USER2_EMAIL));
	}
}