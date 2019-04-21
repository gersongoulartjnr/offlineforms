package br.unifesp.maritaca.web.test.login;

import org.junit.Test;

import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.web.test.IntegrationTest;

public class LoginMaritacaTest extends IntegrationTest{

	/**
	 * The root user is essential to the system and it is tested alone.
	 */
	@Test
	public void rootLoginTest() {
		super.doLogin(ConstantsBusiness.ROOTEMAIL, ConstantsBusiness.ROOT);
	}
	
	/**
	 * Test if the users used on the other tests can perform the login. 
	 */
	@Test
	public void testUsersLoginTest() {
		super.doLogin(ConstantsTest.USER1_EMAIL, ConstantsTest.USERS_PASSWORD);
		super.doLogout();
		
		/*super.doLogin(ConstantsTest.USER2_EMAIL, ConstantsTest.USERS_PASSWORD);
		super.closeTutorial();
		super.doLogout();
		
		super.doLogin(ConstantsTest.USER3_EMAIL, ConstantsTest.USERS_PASSWORD);
		super.closeTutorial();
		super.doLogout();*/
	}
}
