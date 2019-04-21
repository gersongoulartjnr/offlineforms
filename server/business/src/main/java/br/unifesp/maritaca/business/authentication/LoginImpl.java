package br.unifesp.maritaca.business.authentication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.form.ManagementForm;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.util.MaritacaEncode;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;

@Service("login")
public class LoginImpl extends AbstractBusiness implements Login {
	
	@Autowired private UserDAO userDAO;
	@Autowired private ManagementForm managementForm;
	
	@Override
	public Message doLogin(AccountDTO loginDTO) {
		Message message = new Message();
		User user = userDAO.findUserByEmail(loginDTO.getEmail());
		if(user != null && user.getEnabled().equals(ConstantsPersistence.USER_ENABLED)) {
			if(loginSuccessful(loginDTO, user)) {
				loginDTO.setFirstName(user.getFirstname());
				loginDTO.setLastName(user.getLastname());
				loginDTO.setLoggedBefore(user.getLoggedBefore());
				userDAO.updateUserLoggedBefore(user);
				message.setType(MessageType.SUCCESS);
				message.setData(loginDTO);				
				if (loginDTO.getFormId() != null) {
					managementForm.verifyPermissions(loginDTO.getFormId(), user.getKey().toString());
				}
			} else {
				message.setType(MessageType.ERROR);
				message.setMessage(getText("login_failed"));
			}
		} else {
			message.setType(MessageType.ERROR);
			message.setMessage(getText("login_account_not_verified"));
		}
		return message;
	}

	private boolean loginSuccessful(AccountDTO loginDTO, User user) {
		String passEncrypted = MaritacaEncode.getStringMessageDigest(loginDTO.getPassword(), MaritacaEncode.SHA1);
		return (passEncrypted.equals(user.getPassword()));
	}

	@Override
	public AccountDTO doLoginSocial(AccountDTO accountDTO) {
		User user = userDAO.findUserByEmail(accountDTO.getEmail());
		if(user != null) {
			accountDTO.setKey(user.getKey().toString());
			userDAO.updateUserLoggedBefore(user);
			accountDTO.setLoggedBefore(user.getLoggedBefore());			
		} else {
			user = new User();
			user.setFirstname(accountDTO.getFirstName());
			user.setLastname(accountDTO.getLastName());
			user.setEmail(accountDTO.getEmail());
			user.setPassword(accountDTO.getPassword());
			user.setEnabled(ConstantsPersistence.USER_ENABLED);
			user.setLoggedBefore(ConstantsPersistence.NEVER_LOGGED);
			user.setActivationCode(" ");
			userDAO.saveUser(user);
			if(user.getKey() != null) {
				accountDTO.setKey(user.getKey().toString());
				accountDTO.setLoggedBefore(user.getLoggedBefore());
			}
		}
		if(accountDTO.getFormId() != null) {
			managementForm.verifyPermissions(accountDTO.getFormId(), user.getKey().toString());
		}
		return accountDTO;
	}

	@Override
	public Message getApkByUrl(String url, UserDTO userDTO) {
		return managementForm.getApkByUrl(url, userDTO);
	}

	@Override
	public List<FormDTO> getTopPublicForms() {
		return managementForm.getTopPublicForms();
	}

	@Override
	public Message getAPK(String url) {
		return managementForm.getAndroidAppByUrl(url);
	}

	@Override
	public List<FormDTO> searchPublicForms(String search) {
		return managementForm.searchPublicForms(search);
	}
	
	@Override
	public boolean isAPKBuilding(String formUrl) {
		return managementForm.isAPKBuilding(formUrl);
	}
}