package br.unifesp.maritaca.web.test.account;


import org.junit.Test;

import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.web.test.IntegrationTest;
import br.unifesp.maritaca.web.test.ItConstants;

public class AccountEditorTest extends IntegrationTest{

	
	private static final String USER_FIRST_NAME = "User";
	private static final String USER_LOGIN      = "user33@maritaca.com";
	private static final String USER_PASSWD     = "12345678";
	
	private static final String USER1_NEW_LASTNAME = "user 1 lastname tmp";

	@Test
	public void changeLastNameTest(){        
        super.doLogin(ConstantsTest.USER1_EMAIL, ConstantsTest.USERS_PASSWORD);        
        super.goToUrl(ConstantsTest.URL_MY_ACCOUNT);        
        
        super.fillInput(ItConstants.ACCOUNT_EDITOR_LAST_NAME, USER1_NEW_LASTNAME);
        super.clickButton(ItConstants.UPDATE_ACCOUNT_BT);        

        super.checkInfoMsg();
	}
	
	/**
	 * This test changes the user3 password two times. In the first time his user
	 * name is used and in the second the password is reseted to its original value.
	 */
	//@Test
	public void changePasswordTest(){        
        super.doLogin(ConstantsTest.USER3_EMAIL, ConstantsTest.USERS_PASSWORD);        
        super.goToUrl(ConstantsTest.URL_CHANGE_PASSWD);        
        
        super.fillInput(ItConstants.ACCOUNT_EDITOR_CUR_PASSWD, ConstantsTest.USERS_PASSWORD);
        super.fillInput(ItConstants.ACCOUNT_EDITOR_PASSWD,     ConstantsTest.USER3_EMAIL);
        super.fillInput(ItConstants.ACCOUNT_EDITOR_RE_PASSWD,  ConstantsTest.USER3_EMAIL);
        super.clickButton(ItConstants.ACCOUNT_EDITOR_CREATE_BT);        
        super.checkInfoMsg();
        
        super.doLogout();        
        super.doLogin(ConstantsTest.USER3_EMAIL, ConstantsTest.USER3_EMAIL);
        super.goToUrl(ConstantsTest.URL_CHANGE_PASSWD);  
        
        super.fillInput(ItConstants.ACCOUNT_EDITOR_CUR_PASSWD, ConstantsTest.USER3_EMAIL);
        super.fillInput(ItConstants.ACCOUNT_EDITOR_PASSWD,     ConstantsTest.USERS_PASSWORD);
        super.fillInput(ItConstants.ACCOUNT_EDITOR_RE_PASSWD,  ConstantsTest.USERS_PASSWORD);
        super.clickButton(ItConstants.ACCOUNT_EDITOR_CREATE_BT);        
        super.checkInfoMsg();
        
        super.doLogout();   
        super.doLogin(ConstantsTest.USER3_EMAIL, ConstantsTest.USERS_PASSWORD);
	}
	
	//@Test
	public void createUserTest() {
		super.clickButton(ItConstants.LOGIN_CREATE_ACCOUNT_BT);
        super.waitUrl(ConstantsTest.URL_CREATE_ACCOUNT);
        
        super.fillInput(ItConstants.ACCOUNT_EDITOR_FIRST_NAME, USER_FIRST_NAME);        
        super.fillInput(ItConstants.ACCOUNT_EDITOR_EMAIL,      USER_LOGIN);
        super.fillInput(ItConstants.ACCOUNT_EDITOR_PASSWD,     USER_PASSWD);
        super.fillInput(ItConstants.ACCOUNT_EDITOR_RE_PASSWD,  USER_PASSWD);        
        
        super.clickButton(ItConstants.ACCOUNT_EDITOR_CREATE_BT);        
        super.waitUrl(ConstantsTest.URL_HOME);
	}

}
