package br.unifesp.maritaca.ws;

import junit.framework.Assert;

import org.junit.Test;

import br.unifesp.maritaca.persistence.util.ConstantsTest;

public class WebServicesTest {
	
	protected void setProperties() {
		System.setProperty(ConstantsTest.SYS_PROP_KEY_TEST, ConstantsTest.SYS_PROP_VALUE_TEST);
	}
	protected void clearProperties() {
		System.clearProperty(ConstantsTest.SYS_PROP_KEY_TEST);
	}
	
	@Test
	public void setPropertiesTest() {
		setProperties();
		Assert.assertEquals(System.getProperty(ConstantsTest.SYS_PROP_KEY_TEST), ConstantsTest.SYS_PROP_VALUE_TEST);
	}
	
	@Test
	public void clearPropertiesTest() {
		clearProperties();
		Assert.assertEquals(System.getProperty(ConstantsTest.SYS_PROP_KEY_TEST), null);
	}
}