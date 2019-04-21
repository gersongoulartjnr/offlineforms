package br.unifesp.maritaca.business.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.account.ManagementAccount;
import br.unifesp.maritaca.business.exception.ParameterException;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.group.ManagementGroup;
import br.unifesp.maritaca.persistence.cassandra.BaseEmbededServerSetupTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/business-context-test.xml")
public class ParameterVerfierTest extends BaseEmbededServerSetupTest {
	
	@Autowired private ManagementForm managementForm;
	@Autowired private ManagementGroup managementGroup;
	@Autowired private ManagementAccount account;
	
	@Test
	public void testNullParamaterAccount() {						
		try {
			managementForm.saveForm(null, null);
			Assert.fail();
		} catch (ParameterException e) {
		}		
		try {
			account.emailExists(null);
			Assert.fail();
		} catch (ParameterException e) {			
		}
	}
	
	@Test
	public void testNullParamaterList() {		
		try {
			managementGroup.removeGroupByKey(null, null);
			Assert.fail();
		} catch (ParameterException e) {			
		}
	}
	
	@Test
	public void testNullParamaterForm(){				
		try {
			managementGroup.getGroupsUserByName(null, null);
			Assert.fail();
		} catch (ParameterException e){			
		}
	}
}
