package br.unifesp.maritaca.ws.api.impl;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.context.SpringApplicationContext;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.messaging.ManagementMessage;
import br.unifesp.maritaca.business.messaging.dto.EmailDTO;
import br.unifesp.maritaca.persistence.util.ConstantsTest;
import br.unifesp.maritaca.ws.api.InviteService;
import br.unifesp.maritaca.ws.api.resp.ErrorResponse;
import br.unifesp.maritaca.ws.api.resp.MaritacaResponse;
import br.unifesp.maritaca.ws.api.resp.XmlSavedResponse;
import br.unifesp.maritaca.ws.exceptions.MaritacaWSException;
import br.unifesp.maritaca.ws.resource.messsages.Invitation;
import br.unifesp.maritaca.ws.util.SpringApplicationContextTest;
import br.unifesp.maritaca.ws.util.UtilsWS;

@Path("/invite")
public class InviteServiceImpl implements InviteService {

	private static final String INVITATION_SUBJECT 	= "Take part of Maritaca community :)";
	private static final String URL_PROJECT			= "maritaca.unifesp.br";

	private static final Log log = LogFactory.getLog(InviteServiceImpl.class);

	private ManagementForm managementForm;
	private ManagementMessage managementMessage;

	public InviteServiceImpl() {	
		log.info("InviteServiceImpl - Constructor");
		if(System.getProperty(ConstantsTest.SYS_PROP_KEY_TEST) == null) {
			managementForm = (ManagementForm) SpringApplicationContext.getBean("managementForm");
			managementMessage = (ManagementMessage) SpringApplicationContext.getBean("managementMessage");
		}
		else {
			managementForm = (ManagementForm) SpringApplicationContextTest.getBean("managementForm");
			managementMessage = (ManagementMessage) SpringApplicationContextTest.getBean("managementMessage");
		}
	}

	@Override
	public MaritacaResponse addInvitation(HttpServletRequest request, Invitation invitation) {
		try{
			UserDTO userDTO = UtilsWS.createUserDTO(request);
			
			FormDTO formDTO = new FormDTO();
			formDTO.setKey(UUID.fromString(invitation.getFormId()));
			formDTO = managementForm.getFormDTOById(invitation.getFormId(), userDTO);
			
			EmailDTO emailDTO = new EmailDTO();
			emailDTO.setContent( createContentInvite(formDTO) );
			emailDTO.setEmails(invitation.getFriends());
			emailDTO.setSubject(INVITATION_SUBJECT);

			managementMessage.sendMessage(emailDTO);
			
			XmlSavedResponse resp = new XmlSavedResponse();
			resp.setType(MaritacaResponse.MESSAGE_TYPE);
			return resp;
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse(Status.INTERNAL_SERVER_ERROR);
			error.setMessage(e.getMessage());
			throw new MaritacaWSException(error);
		}
	}

	private String createContentInvite(FormDTO formDTO) {
		String message = "Hello, " + 
					formDTO.getUser().toUpperCase() + " invites you to use Maritaca. \n" +
					formDTO.getUser().toUpperCase() + " creates a form (" + formDTO.getTitle(); 
		if(formDTO.getDescription() != null)			
					message += ", " + formDTO.getDescription();
		message += ") and wants you collaborate. Please register in " + URL_PROJECT;
		return message;
	}
}