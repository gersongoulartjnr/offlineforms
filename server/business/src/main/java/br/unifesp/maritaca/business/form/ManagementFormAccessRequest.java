package br.unifesp.maritaca.business.form;

import java.util.List;

import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.form.dto.AccessRequestDTO;
import br.unifesp.maritaca.business.form.dto.FormAccessRequestDTO;

public interface ManagementFormAccessRequest {

	void askForPermission(UserDTO userDTO, String formKey);
	
	List<FormAccessRequestDTO> getFormAccessRequestByUser(UserDTO userDTO);
	
	int getTotalRequestByUser(UserDTO userDTO);
	
	void sendNotificationOfAccepting(UserDTO userDTO, List<String> formUrls);

	Message rejectRequest(UserDTO currentUser, List<AccessRequestDTO> acceptList);

	Message acceptRequest(UserDTO userDTO, List<AccessRequestDTO> acceptList);
}