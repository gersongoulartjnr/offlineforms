package br.unifesp.maritaca.web.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.unifesp.maritaca.persistence.util.ConstantsTest;

public abstract class IntegrationTest {

	
	private static final String TEMPORARY_MESSAGES = "div.maritacaMessages div";
	private static final String AVAILABLE_TABS     = "return $('ul#%s > li#%s > a')[0]";
	private static final String CURRENT_TAB        = "ul#%s > li.selected > a";
	private static final String MOD_TAB_ID         = "ul-main-menu";
	private static final String SUB_MOD_TAB_ID     = "ul-form-menu";

	private WebDriver driver;
	
	@Before
	public void setupTest() {
		this.setProperties();
		setDriver(new FirefoxDriver());
		getDriver().manage().deleteAllCookies();
		getDriver().get(ConstantsTest.URL_SERVER);
	}

	@After
	public void finishTest() {
		this.clearProperties();
		getDriver().quit();
	}
	
	protected void setProperties() {
		System.setProperty(ConstantsTest.SYS_PROP_KEY_TEST, ConstantsTest.SYS_PROP_VALUE_TEST);
	}
	protected void clearProperties() {
		System.clearProperty(ConstantsTest.SYS_PROP_KEY_TEST);
	}
	public void goToUrl(String url){
		getDriver().get(url);
		waitUrl(url);
	}
	
	public void fillInput(By selector, String text){
		WebElement inputElement = getDriver().findElement(selector);
		inputElement.clear();
		inputElement.sendKeys(text);
	}
	
	public void clickButton(By selector){
		WebElement buttonElement = getDriver().findElement(selector);
		buttonElement.click();		
	}
	
	public void waitUrl(String url){
		final String urlLocal = url; 
        (new WebDriverWait(driver, ConstantsTest.WAIT_URL_TIME)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().equals(urlLocal);
            }
        });
        waitRender();
	}
	
	public void matchesUrl(String urlRegExp){
		final String urlRegExpLocal = urlRegExp; 
        (new WebDriverWait(driver, ConstantsTest.WAIT_URL_TIME)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().matches(urlRegExpLocal);
            }
        });
	}
	
	public void doLogin(String user, String password){
        WebElement inputEmail  = getDriver().findElement(ItConstants.LOGIN_EMAIL);
        WebElement inputPasswd = getDriver().findElement(ItConstants.LOGIN_PASSWD);
        WebElement inputLogin  = getDriver().findElement(ItConstants.LOGIN_BT);
        
        
        inputEmail.sendKeys(user);
        inputPasswd.sendKeys(password);
        inputLogin.click();
        
        waitUrl(ConstantsTest.URL_HOME);
	}
	
	public void closeTutorial() {
		WebElement btnLogout = getDriver().findElement(ItConstants.CLOSE_TUTORIAL_LNK);
		btnLogout.click();
		waitUrl(ConstantsTest.URL_HOME);
	}
	
	public void doLogout() {
		WebElement btnLogout = getDriver().findElement(By.cssSelector("a#logout_button"));
		btnLogout.click();
		waitUrl(ConstantsTest.URL_LOGIN);
	}
	
	public void changeTab(final String tabName){
		changeTabOrSubTab(tabName, MOD_TAB_ID);
	}
	
	
	private void changeTabOrSubTab(final String tabName, final String divSelector){		
		final String tabSelector = String.format(AVAILABLE_TABS, divSelector,tabName);
		
		WebElement tab = (WebElement) ((JavascriptExecutor) getDriver()).executeScript(tabSelector);		
		tab.click();
		waitRender();
		
		return;
	}
	
	//TODO This is not good but I couldn't get the selenium wait to work
	private void waitRender() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}		
	}

	public String selectedTabName(){
		return selectedTabOrSubTabName(MOD_TAB_ID);
	}
	
	public String selectedSubTabName(){
		return selectedTabOrSubTabName(SUB_MOD_TAB_ID);
	}
	
	private String selectedTabOrSubTabName(final String divSelector){
		final String currentTabSelector = String.format(CURRENT_TAB,divSelector);   			
	     
		WebElement selectedTabElmt = safeFind(By.cssSelector(currentTabSelector));
		
		return selectedTabElmt.getText();
	}
	
	public WebElement safeFind(final By locator){
		WebDriverWait wait    = new WebDriverWait(getDriver(), 10);
		WebElement    element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		
		return element;
	}

	public void changeSubTab(String subTabName) {
		changeTabOrSubTab(subTabName, SUB_MOD_TAB_ID);
	}
	
	public List<String> temporaryMessages(){
		List<WebElement> tmpTags     = getDriver().findElements(By.cssSelector(TEMPORARY_MESSAGES));				
		List<String>     messages = new ArrayList<String>();
		
		for(WebElement tmpTag : tmpTags){
			messages.add(tmpTag.getText());			
		}
		return messages;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void checkInfoMsg() {
        try{
        	safeFind(ItConstants.INFO_MSG);
        }catch(NoSuchElementException e){
        	fail();
        }
	}
}
