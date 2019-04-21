package br.unifesp.maritaca.persistence.util;

public class ConstantsTest {
	
	public static final String SYS_PROP_KEY_TEST		= "MARITACA_TEST";
	public static final String SYS_PROP_VALUE_TEST	= "	TEST";
	
	public static final String URL_SERVER         = "http://127.0.0.1:8480/maritaca/";
	public static final String URL_HOME           = URL_SERVER+"forms.html";
	public static final String URL_CREATE_ACCOUNT = URL_SERVER+"create-account.html";
	public static final String URL_LOGIN          = URL_SERVER+"index.html";
	public static final String URL_MY_ACCOUNT     = URL_SERVER+"my-account.html";
	public static final String URL_CHANGE_PASSWD  = URL_SERVER+"change-pass.html";
	
	public static final String USER_OPENID        = "test.maritaca";
	public static final String PASSWORD_OPENID    = "123412abc";
		
	public static final String USERS_PASSWORD = "qwerty"; //sha1 for 'qwerty'
	public static final String USER_PASSWORD_ENC = "b1b3773a05c0ed0176787a4f1574ff0075f7521e"; //sha1 for 'qwerty'
	
	public static final String USER1_EMAIL = "user1@maritaca.com";
	public static final String USER1_FIRSTNAME = "user1";
	public static final String USER1_LASTNAME = "test1";
	
	public static final String USER2_EMAIL = "user2@maritaca.com";
	public static final String USER2_FIRSTNAME = "user2";
	public static final String USER2_LASTNAME = "test2";
	
	public static final String USER3_EMAIL = "user3@maritaca.com";
	public static final String USER3_FIRSTNAME = "user3";
	public static final String USER3_LASTNAME = "test3";
	
	public static final String MARITACALIST1_TITLE = "user1list1";
	
	public static final String MARITACALIST2_TITLE = "user2list2";
	
	public static final String FORM1_TITLE = "form1";
	public static final String FORM1_XML = "<form><title>form1</title><questions><text id=\"0\" next=\"-1\" required=\"false\" ><label>TextBox title</label><help></help><size>100</size><default></default></text></questions></form>";
	
	public static final String FORM2_TITLE = "form2";
	public static final String FORM2_XML = "<form><title>form2</title><questions><number id=\"0\" next=\"-1\" required=\"false\" min=\"\" max=\"\" ><label>Number title</label><help></help><default></default></number></questions></form>";
	
	public static final String FORM3_TITLE = "form3";
	public static final String FORM3_XML = "<form><title>form3</title><questions><date id=\"0\" next=\"-1\" required=\"false\" min=\"\" max=\"\" ><label>Date title</label><help></help><default></default><format>mm/dd/yy</format></date></questions></form>";
	

	public static final Integer WAIT_URL_TIME = 60;
}
