package br.unifesp.maritaca.business.test.account;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.BusinessTest;
import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class AccountTests extends BusinessTest {
	
	private AccountDTO accounDTO;
	
	@Before
	public void setUp() {
		super.setProperties();	
		emHector.createColumnFamily(User.class);
		emHector.createColumnFamily(MaritacaList.class);
	}
	
	@After
	public void cleanUp() {
		super.clearProperties();
		emHector.dropColumnFamily(User.class);
		emHector.dropColumnFamily(MaritacaList.class);
	}
	
	private AccountDTO getAccountDTO() {
		accounDTO = new AccountDTO();
		accounDTO.setFirstName(USER1_FIRSTNAME);
		accounDTO.setLastName(USER1_LASTNAME);
		accounDTO.setEmail(USER1_EMAIL);
		accounDTO.setPassword(USER1_PASSWORD);
		return accounDTO;
	}
	
	@Test
	public void saveNewAccountTest() {
		Message message = account.saveNewAccount(getAccountDTO());
		Assert.assertNotNull(message);
		Assert.assertEquals(message.getType(), MessageType.SUCCESS);
	}
	
	@Test
	public void emailExistsTest() {
		Assert.assertFalse(account.emailExists(USER1_EMAIL));
		account.saveNewAccount(getAccountDTO());
		Assert.assertTrue(account.emailExists(USER1_EMAIL));
	}
}