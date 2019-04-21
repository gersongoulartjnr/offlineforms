package br.unifesp.maritaca.ws.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;
import br.unifesp.maritaca.ws.util.UtilsWS;

public class UtilsWSTest {

	private static final String USER_EMAIL = "test@maritaca.com";
	
	@Test
	public void createUserTest(){
		HttpServletRequest request = mock(HttpServletRequest.class);
		try{
			UtilsWS.createUserDTO(request);
			fail();
		}catch (MaritacaWSException e){ }		
		
		when(request.getAttribute(ConstantsBusiness.WS_USER_KEY)).thenReturn(USER_EMAIL);		
		UserDTO userDTO = UtilsWS.createUserDTO(request);
		
		assertTrue(userDTO.getUsername().equals(USER_EMAIL));
	}

}
