package br.unifesp.maritaca.web.test;

import org.openqa.selenium.By;

public abstract class ItConstants {
	public static final By ACCOUNT_EDITOR_FIRST_NAME 	= By.cssSelector("input[id=firstName]");
	public static final By ACCOUNT_EDITOR_LAST_NAME  	= By.cssSelector("input[id=lastName]");
	public static final By ACCOUNT_EDITOR_EMAIL      	= By.cssSelector("input[id=email]");
	public static final By ACCOUNT_EDITOR_CUR_PASSWD 	= By.cssSelector("input[id=currentPassword]");
	public static final By ACCOUNT_EDITOR_PASSWD     	= By.cssSelector("input[id=password]");
	public static final By ACCOUNT_EDITOR_RE_PASSWD  	= By.cssSelector("input[id=passwordConfirmation]");
	public static final By UPDATE_ACCOUNT_BT		   	= By.cssSelector("div[id=updateAccount]");
	public static final By ACCOUNT_EDITOR_CREATE_BT  	= By.cssSelector("button[class=form-button]");
	
	public static final By LOGIN_CREATE_ACCOUNT_BT   	= By.cssSelector("a[id=create_account]");
	public static final By LOGIN_EMAIL               	= By.cssSelector("input[id=email]");
	public static final By LOGIN_PASSWD              	= By.cssSelector("input[id=password]");	
	public static final By LOGIN_BT                  	= By.cssSelector("button[class=form-button]");
	
	public static final By CLOSE_TUTORIAL_LNK        	= By.cssSelector("div.ui-dialog > div > a");
	
	public static final By FORM_LIST_NEW_FORM_BT     	= By.cssSelector("a#btn_new_form");
	
	public static final By INFO_MSG                  	= By.cssSelector("div.maritacaMessages > div.infoMsg");
}
