package br.unifesp.maritaca.business.form;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.form.dto.AccessRequestDTO;
import br.unifesp.maritaca.business.form.dto.FormAccessRequestDTO;
import br.unifesp.maritaca.business.messaging.dto.EmailDTO;
import br.unifesp.maritaca.persistence.dao.FormAccessRequestDAO;
import br.unifesp.maritaca.persistence.dao.FormAccessibleByListDAO;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.FormAccessRequest;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.permission.RequestStatusType;

@Service("managFormAccessRequest")
public class ManagementFormAccessRequestImpl extends AbstractBusiness implements ManagementFormAccessRequest {

	private static Logger logger = Logger.getLogger(ManagementFormAccessRequestImpl.class);
	
	@Autowired private UserDAO 	userDAO;
	@Autowired private FormDAO 	formDAO;
	@Autowired private FormAccessRequestDAO formAccessRequestDAO;
	@Autowired private FormAccessibleByListDAO 	formByListDAO;
	
	private static final short ID_SEND_REQUEST = 1;
	private static final short ID_REQUEST_ACCEPTED = 2;
	
	@Override
	public void askForPermission(UserDTO userDTO, String formKey) {
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		Form form = formDAO.getFormByKey(UUID.fromString(formKey), true);
		if(user == null || form == null) {
			throw new MaritacaException("Invalid parameters");
		}
		User owner = userDAO.findUserByKey(form.getUser());
		formAccessRequestDAO.saveFormAccessRequest(new FormAccessRequest(form.getUrl(), user.getKey(), owner.getKey()));
		if(!isRunningTestEnvironment()) {
			getEmailData(ID_SEND_REQUEST, user.getFirstname(), user.getFullName(), owner.getFullName(), form.getTitle());
		}
	}

	private EmailDTO getEmailData(short actionType, String userEmail, String userFullName, String owner, String formTitle) {
		EmailDTO emailDTO = new EmailDTO();
		List<String> emails = new ArrayList<String>(1);
		emails.add(owner);		
		String[] data = new String[3];
		switch(actionType){
			case ID_SEND_REQUEST:
				data[0] = userEmail;
				data[1] = userFullName;
				data[2] = formTitle;
				emailDTO.setSubject(getText("email_request_form_access_subject"));		
				emailDTO.setContent(getText("email_request_form_access_content", data));
				break;
			case ID_REQUEST_ACCEPTED:
				emailDTO.setSubject(getText("email_reset_pass_subject"));
				emailDTO.setContent(getText("email_reset_pass_content", data));
				break;
			default:
				throw new MaritacaException("Invalid actionType");
		}		
		logger.info(">> " + emailDTO.getContent());
		emailDTO.setEmails(emails);
		return emailDTO;
	}

	@Override
	public List<FormAccessRequestDTO> getFormAccessRequestByUser(UserDTO userDTO) {
		List<FormAccessRequestDTO> farsDTO = new ArrayList<FormAccessRequestDTO>();
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if (user == null) {
			throw new MaritacaException("Invalid user");
		}
		List<FormAccessRequest> fars = formAccessRequestDAO.getFormAccessRequestsByOwner(user.getKey());	
		Form form;
		for (FormAccessRequest far : fars) {
			if (far.getStatus().equals(RequestStatusType.ACCEPTED) ||
					far.getStatus().equals(RequestStatusType.REJECTED) ) {
				continue;
			}
			form = formDAO.getFormByUrl(far.getFormUrl(), true);
			user = userDAO.findUserByKey(far.getUser());
			farsDTO.add(new FormAccessRequestDTO(form.getUrl(), form.getTitle(), user.getFullName(), user.getEmail(), far.getDate().toString()));
		}
		return farsDTO;
	}

	@Override
	public int getTotalRequestByUser(UserDTO userDTO) {
		return 	this.getFormAccessRequestByUser(userDTO).size();
	}

	@Override
	public void sendNotificationOfAccepting(UserDTO userDTO, List<String> formUrls) {
		User owner = userDAO.findUserByEmail(userDTO.getUsername());
		if(owner == null)
			throw new MaritacaException("Invalid user");
		List<FormAccessRequest> fars = formAccessRequestDAO.getFormAccessRequestsByOwner(owner.getKey());	
		Form form;
		User user;
		for (FormAccessRequest far : fars) {
			if (formUrls.contains(far.getFormUrl()) && !far.getStatus().equals(RequestStatusType.ACCEPTED)) {
				far.setStatus(RequestStatusType.ACCEPTED);
				formAccessRequestDAO.saveFormAccessRequest(far);
				form = formDAO.getFormByUrl(far.getFormUrl(), true);
				user = userDAO.findUserByKey(far.getUser());
				if(!isRunningTestEnvironment()) {
					getEmailData(ID_REQUEST_ACCEPTED, user.getFirstname(), user.getFullName(), owner.getFullName(), form.getTitle());
				}
			}
		}
	}

	@Override
	public Message rejectRequest(UserDTO userDTO, List<AccessRequestDTO> acceptList) {
		User owner = userDAO.findUserByEmail(userDTO.getUsername());
		if(owner == null)
			throw new MaritacaException("Invalid user");
		List<FormAccessRequest> fars = formAccessRequestDAO.getFormAccessRequestsByOwner(owner.getKey());	
		Form form;
		User user;
		Message message = new Message();
		for (AccessRequestDTO acceptRequest : acceptList) {
			user = userDAO.findUserByEmail(acceptRequest.getUser());
			for (FormAccessRequest far : fars) {
				if (acceptRequest.getForm().equals(far.getFormUrl()) && 
						far.getStatus().equals(RequestStatusType.PENDING) &&
						far.getUser().equals(user.getKey())) {
					far.setStatus(RequestStatusType.REJECTED);
					formAccessRequestDAO.saveFormAccessRequest(far);
					form = formDAO.getFormByUrl(far.getFormUrl(), true);
					if(!isRunningTestEnvironment()) {
						// TODO message of rejection
						getEmailData(ID_REQUEST_ACCEPTED, user.getFirstname(), user.getFullName(), owner.getFullName(), form.getTitle());
					}
					break;
				}
			}
		}
		message.setMessage("Successful");
		message.setType(MessageType.SUCCESS);
		return message;
	}

	@Override
	public Message acceptRequest(UserDTO userDTO, List<AccessRequestDTO> acceptList) {
		User owner = userDAO.findUserByEmail(userDTO.getUsername());
		if(owner == null) {
			throw new MaritacaException("Invalid user");
		}
		List<FormAccessRequest> fars = formAccessRequestDAO.getFormAccessRequestsByOwner(owner.getKey());	
		Form form;
		User user;
		Message message = new Message();
		for (AccessRequestDTO acceptRequest : acceptList) {
			user = userDAO.findUserByEmail(acceptRequest.getUser());
			for (FormAccessRequest far : fars) {
				if (acceptRequest.getForm().equals(far.getFormUrl()) && 
						far.getStatus().equals(RequestStatusType.PENDING) &&
						far.getUser().equals(user.getKey())) {
					far.setStatus(RequestStatusType.ACCEPTED);
					formAccessRequestDAO.saveFormAccessRequest(far);
					form = formDAO.getFormByUrl(far.getFormUrl(), true);
					if(!isRunningTestEnvironment()) {
						getEmailData(ID_REQUEST_ACCEPTED, user.getFirstname(), user.getFullName(), owner.getFullName(), form.getTitle());
					}
					form.getLists().add(user.getKey());
					formByListDAO.createOrUpdateFormAccessible(form, owner, (new ArrayList<UUID>()));
					formDAO.saveForm(form);
					break;
				}
			}
		}	
		message.setMessage("Successful");
		message.setType(MessageType.SUCCESS);
		return message;
	}
}