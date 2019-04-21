package br.unifesp.maritaca.ws.api.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import br.unifesp.maritaca.ws.api.InviteService;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;
import br.unifesp.maritaca.ws.resource.messsages.Invitation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:webservices-context-test.xml"})
public class InviteServiceImplTest extends WebServicesTest {

	private static final String USER_EMAIL = "test@maritaca.com";
	
	private InviteService inviteService;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		super.setProperties();
		inviteService = new InviteServiceImpl();
	}
	
	@After
	public void cleanUp() {
		super.clearProperties();
	}
	
	@Test
	public void testAddInvitationFail() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		exception.expect(MaritacaWSException.class);
		@SuppressWarnings("unused")
		MaritacaResponse response = inviteService.addInvitation(request, null);
	}
	
	@Test
	public void testAddInvitationSuccess() throws IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);
		
		Invitation invitation = new Invitation();
		invitation.setFormId(UUID.randomUUID().toString());
		List<String> friends = new ArrayList<String>();
		friends.add("test1.test.com");
		friends.add("test2.test.com");
		friends.add("test3.test.com");		
		invitation.setFriends(friends);
		
		MaritacaResponse response = inviteService.addInvitation(request, invitation);
		Assert.assertEquals(Status.OK.getStatusCode(), response.getCode());
		
	}
}
