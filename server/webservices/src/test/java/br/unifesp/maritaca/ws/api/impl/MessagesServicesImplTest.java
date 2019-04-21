package br.unifesp.maritaca.ws.api.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.ws.WebServicesTest;
import br.unifesp.maritaca.ws.api.MessagesService;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;
import br.unifesp.maritaca.ws.resource.messsages.ErrorMobile;
import br.unifesp.maritaca.ws.resource.messsages.ErrorMobileReport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:webservices-context-test.xml"})
public class MessagesServicesImplTest extends WebServicesTest {

	private static final String USER_EMAIL = "test@maritaca.com";	
	
	private MessagesService messagesService;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		super.setProperties();
		messagesService = new MessagesServiceImpl();
	}
	
	@After
	public void cleanUp() {
		super.clearProperties();
	}
	
	@Test
	public void testAddErrorFailNull() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		exception.expect(MaritacaWSException.class);
		@SuppressWarnings("unused")
		MaritacaResponse response = messagesService.addError(request, null);
	}
	
	@Test
	public void testAddErrorSuccess() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);
		
		List<ErrorMobile> errors = new ArrayList<ErrorMobile>();
		ErrorMobile error = new ErrorMobile();
		error.setAndroidVersion("Gingerbread");
		error.setValue("error");
		errors.add(error);
		
		ErrorMobileReport errorMobile = new ErrorMobileReport();
		errorMobile.setErrors(errors);
		errorMobile.setUserName(USER_EMAIL);
		
		MaritacaResponse response = messagesService.addError(request, errorMobile);
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getCode());
	}	
}