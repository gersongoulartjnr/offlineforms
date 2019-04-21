package br.unifesp.maritaca.web.test.form.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.web.test.IntegrationTest;
import br.unifesp.maritaca.web.test.ItConstants;

public class FormListTest extends IntegrationTest{

	@Test
	@Ignore
	public void user1FormList() {
		doLogin(ConstantsTest.USER1_EMAIL,ConstantsTest.USERS_PASSWORD);
		
		List<WebElement> formLinks;
		formLinks = getDriver().findElements(By.cssSelector("div#myForms > table > tbody > tr > td > a"));
		
		assertTrue(formLinks.size()==3);
		
		List<String> formNames = new ArrayList<String>();
		for(WebElement formLink : formLinks){
			formNames.add(formLink.getText());
		}
		
		assertTrue(formNames.contains(ConstantsTest.FORM1_TITLE));
		assertTrue(formNames.contains(ConstantsTest.FORM2_TITLE));
		assertTrue(formNames.contains(ConstantsTest.FORM3_TITLE));
	}

	@Test
	@Ignore //TODO Saving the form state weren't implemented in this version yet.
	public void formNavigationTest(){
		doLogin(ConstantsTest.USER1_EMAIL,ConstantsTest.USERS_PASSWORD);

		//Open 4 forms
		for(int i=0; i<4; i++){
			WebElement newFormBt  = safeFind(ItConstants.FORM_LIST_NEW_FORM_BT);
			newFormBt.click();
			changeTab("Forms");
		}		
		
		//Leave each one in one different open sub tab
		changeTab("New Form");		
		changeSubTab("Editor");
				
		changeTab("New Form 1");
		changeSubTab("Details");
				
		changeTab("New Form 2");
		changeSubTab("Share");
		
		changeTab("New Form 3");
		changeSubTab("View Answers");		
		
		//Check if they are still on the sub tab we left them
		changeTab("New Form");
		assertEquals(selectedSubTabName(),"Editor");

		changeTab("New Form 1");
		assertEquals(selectedSubTabName(),"Details");

		changeTab("New Form 2");
		assertEquals(selectedSubTabName(),"Share");
		
		changeTab("New Form 3");
		assertEquals(selectedSubTabName(),"View Answers");		
	}
}