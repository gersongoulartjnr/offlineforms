package br.unifesp.maritaca.web.test.login;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.web.test.IntegrationTest;

public class LoginOpenIdTest extends IntegrationTest {

	private static final String GOOGLE_SIGN_IN_URL = "^https://accounts\\.google\\.com/ServiceLogin.*";
	private static final String GOOGLE_OPEN_ID_ALLOW_URL = "^https://accounts\\.google\\.com/o/openid2/.*";

	/**
	 * This test performs an open id authentication with an user that is not
	 * registered in Maritaca. After the open id authentication the user is
	 * logged in and performs a logout. Finally he tries to log in again with
	 * the same openid. This time no authentication should be performed.
	 */
	//@Test
	public void loginOpenIdTest() {
		goToGoogleOpenId();

		performGoogleLogin(ConstantsTest.USER_OPENID,ConstantsTest.PASSWORD_OPENID);
		allowMaritacaApplication();

		super.closeTutorial();
		super.doLogout();
		clickGoogleOpenIdAndCheckLogin();
		allowMaritacaApplication();
	}

	/**
	 * In this test the user starts the open id authentication but refuses to
	 * allow the application to use his information. He is then redirected to
	 * hte login page.
	 */
	@Test
	public void cancelOpenIdTest() {
		goToGoogleOpenId();

		performGoogleLogin(ConstantsTest.USER_OPENID, ConstantsTest.PASSWORD_OPENID);
		//refuseMaritacaApplication();
	}

	private void refuseMaritacaApplication() {
		WebElement googleReject = getDriver().findElement(By.id("reject_button"));
		WebElement googleRemember = getDriver().findElement(By.id("remember_choices_checkbox"));
		if (googleRemember.isSelected()) {
			googleRemember.click();
		}
		googleReject.click();

		super.waitUrl(ConstantsTest.URL_LOGIN);
	}

	private void clickGoogleOpenIdAndCheckLogin() {
		WebElement googleLogin = getDriver().findElement(By.cssSelector("a#google_openid"));
		googleLogin.click();

		super.matchesUrl(GOOGLE_OPEN_ID_ALLOW_URL);
	}

	private void allowMaritacaApplication() {
		WebElement googleAllow = getDriver().findElement(By.id("approve_button"));
		WebElement googleRemember = getDriver().findElement(By.id("remember_choices_checkbox"));
		if (googleRemember.isSelected()) {
			googleRemember.click();
		}
		googleAllow.click();

		super.waitUrl(ConstantsTest.URL_HOME);
	}

	private void performGoogleLogin(String openIdEmail, String openIdPassword) {
		WebElement googleEmail = getDriver().findElement(By.id("Email"));
		WebElement googlePasswd = getDriver().findElement(By.id("Passwd"));
		WebElement googleSignin = getDriver().findElement(By.id("signIn"));
		WebElement googleRemember = getDriver().findElement(By.id("PersistentCookie"));

		if (googleRemember.isSelected()) {
			googleRemember.click();
		}

		googleEmail.sendKeys(openIdEmail);
		googlePasswd.sendKeys(openIdPassword);
		googleSignin.click();

		super.matchesUrl(GOOGLE_OPEN_ID_ALLOW_URL);
	}

	private void goToGoogleOpenId() {
		WebElement googleLogin = getDriver().findElement(By.cssSelector("a#google_openid"));
		googleLogin.click();

		super.matchesUrl(GOOGLE_SIGN_IN_URL);
	}
}
