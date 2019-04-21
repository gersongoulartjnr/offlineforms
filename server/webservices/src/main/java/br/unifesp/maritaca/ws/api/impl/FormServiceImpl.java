package br.unifesp.maritaca.ws.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.context.SpringApplicationContext;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.form.ManagementFormAccessRequest;
import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.ws.api.FormService;
import br.unifesp.maritaca.ws.api.resp.ErrorResponse;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.api.resp.XmlSavedResponse;
import br.unifesp.maritaca.ws.util.SpringApplicationContextTest;
import br.unifesp.maritaca.ws.util.UtilsWS;

/**
 * Implemention of FormService interface.
 * 
 * @author alvarohenry and Jimmy Valverde
 */
@Path("/form")
public class FormServiceImpl implements FormService {

	private static final Log log = LogFactory.getLog(FormServiceImpl.class);

	private ManagementFormAccessRequest managFormAccessRequest;
	
	public FormServiceImpl() { 
		log.info("FormServiceImpl - Constructor");
		if(System.getProperty(ConstantsTest.SYS_PROP_KEY_TEST) == null) {
			managFormAccessRequest = (ManagementFormAccessRequest) SpringApplicationContext.getBean("managFormAccessRequest");
		}
		else {
			managFormAccessRequest = (ManagementFormAccessRequest) SpringApplicationContextTest.getBean("managFormAccessRequest");
		}
	}
	
	@Override
	public MaritacaResponse askForPermission(HttpServletRequest request,
			String formKey) {		
		try{
			UserDTO userDTO = UtilsWS.createUserDTO(request);			
			log.info("askForPermission : " + userDTO.getUsername());
			managFormAccessRequest.askForPermission(userDTO, formKey);
			XmlSavedResponse resp = new XmlSavedResponse();
			resp.setType(MaritacaResponse.RESPONSE_TYPE);
			return resp;
		}
		catch(Exception e){
			ErrorResponse error = new ErrorResponse(Status.INTERNAL_SERVER_ERROR);
			error.setMessage(e.getMessage());
			throw new MaritacaException();
		}
	}
}