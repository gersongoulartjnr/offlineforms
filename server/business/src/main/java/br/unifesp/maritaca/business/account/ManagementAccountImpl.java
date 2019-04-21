package br.unifesp.maritaca.business.account;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.messaging.ManagementMessage;
import br.unifesp.maritaca.business.messaging.dto.EmailDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.business.util.MaritacaEncode;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@Service("account")
public class ManagementAccountImpl extends AbstractBusiness implements ManagementAccount {
	
	private static Logger logger = Logger.getLogger(ManagementAccountImpl.class);
	
	@Autowired private UserDAO userDAO;	
	@Autowired private ManagementMessage managementMessage;
	
	private static final short ID_CREATE_ACCOUNT	= 1;
	private static final short ID_UPDATE_PASS	= 2;
	private static final String SHA1_EMPTY_STRING	= "da39a3ee5e6b4b0d3255bfef95601890afd80709";
	
	@Override
	public Message createAccount(AccountDTO accDTO){
		String firstName = accDTO.getFirstName().trim();
		String lastName = accDTO.getLastName().trim();
		String email = accDTO.getEmail().trim();
		String passEncoded = accDTO.getPassEncoded();
		String captchaCode = accDTO.getCaptchaCode().trim();
		if(!isFirstNameValid(firstName)){
			return firstNameIsInvalid();
		}
		if(!isLastNameValid(lastName)){
			return lastNameIsInvalid();
		}
		if(isEmptyPassword(passEncoded)){
			return new Message(getText("err_pass_required"), MessageType.ERROR);
		}
		if(!isRunningTestEnvironment()){
			if("".equals(captchaCode) || !captchaCode.equals(accDTO.getCaptchaValue())){
				return new Message(getText("err_captcha_invalid"), MessageType.ERROR);
			}
		}
		if(!isEmailValid(email)){
			return new Message(getText("err_email_invalid"), MessageType.ERROR);
		}
		if(emailExists(email)){
			return new Message(getText("err_email_exists"), MessageType.ERROR);
		}
		
		User user = new User();
		user.setFirstname(firstName);
		user.setLastname(lastName);
		user.setEmail(email);
		user.setEnabled(ConstantsPersistence.USER_DISABLED);
		user.setActivationCode(createActivationCode());
		user.setPassword(passEncoded);
		userDAO.saveUser(user);
		if(user.getKey() != null) {
			if(!isRunningTestEnvironment()){
				managementMessage.sendMessage(getEmailData(ID_CREATE_ACCOUNT, user.getEmail(), user.getActivationCode(), accDTO.getContextPath()));
			}
			return new Message(getText("suc_account_created"), MessageType.SUCCESS);
		}
		else{
			return new Message(getText("err_create_account"), MessageType.ERROR);
		}
	}
	
	@Override
	public Message saveNewAccount(AccountDTO accountDTO) {
		Message message = new Message();
		User user = new User();
		user.setFirstname(accountDTO.getFirstName());
		user.setLastname(accountDTO.getLastName());
		user.setEmail(accountDTO.getEmail());
		user.setEnabled(ConstantsPersistence.USER_DISABLED);
		user.setActivationCode(createActivationCode());
		user.setPassword(MaritacaEncode.getStringMessageDigest(accountDTO.getPassword(), MaritacaEncode.SHA1));
		userDAO.saveUser(user);
		if(user.getKey() != null) {
			if(!isRunningTestEnvironment()) {
				managementMessage.sendMessage(getEmailData(ID_CREATE_ACCOUNT, user.getEmail(), user.getActivationCode(), accountDTO.getContextPath()));				
			}
			accountDTO.setKey(user.getKey().toString());
			message.setMessage(getText("suc_account_created"));
			message.setType(MessageType.SUCCESS);
		}
		else {
			message.setMessage(getText("err_create_account"));
			message.setType(MessageType.ERROR);
		}
		return message;
	}
	
	@Override
	public Message activateAccount(String activationCode){
		User user = userDAO.findUserByActivationCode(activationCode);
		if(user == null) {
			return new Message(getText("err_acc_cannot_active"), MessageType.ERROR);
		}
		else {
			if(user.getEnabled().equals(ConstantsPersistence.USER_ENABLED)) {
				return new Message(getText("err_acc_already_active"), MessageType.ERROR);
			}
			else {
				user.setEnabled(ConstantsPersistence.USER_ENABLED);
				userDAO.updateUser(user);
				return new Message(getText("suc_acc_recently_active"), MessageType.SUCCESS);
			}
		}
	}

	@Override
	public Message updateAccount(UserDTO userDTO, AccountDTO accDTO){
		String firstName = accDTO.getFirstName().trim();
		String lastName = accDTO.getLastName().trim();		
		if(!isFirstNameValid(firstName)){
			return firstNameIsInvalid();
		}
		if(!isLastNameValid(lastName)){
			return lastNameIsInvalid();
		}		
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			return new Message(getText("err_no_user"), MessageType.ERROR);
		}
		user.setFirstname(accDTO.getFirstName());
		user.setLastname(accDTO.getLastName());
		userDAO.updateUser(user);
		return new Message(getText("suc_account_updated"), MessageType.SUCCESS);
	}
	
	@Override
	public Message changePassword(UserDTO userDTO, AccountDTO accDTO){
		if(isEmptyPassword(accDTO.getCurPassEncoded())){
			return new Message(getText("err_current_pass_required"), MessageType.ERROR);
		}
		if(isEmptyPassword(accDTO.getPassEncoded())){
			return new Message(getText("err_pass_required"), MessageType.ERROR);
		}
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null){
			return new Message(getText("err_no_user"), MessageType.ERROR);
		}
		if(!accDTO.getCurPassEncoded().equals(user.getPassword())){
			return new Message(getText("err_current_pass_wrong"), MessageType.ERROR);
		}
		user.setPassword(accDTO.getPassEncoded());
		userDAO.updateUser(user);
		return new Message(getText("suc_account_updated"), MessageType.SUCCESS);
	}	

	@Override
	public Message recoverPasswordStepOne(AccountDTO accDTO) {
		if(accDTO == null || "".equals(accDTO.getEmail().trim())){
			return new Message(getText("err_email_required"), MessageType.ERROR);
		}		
		User user = userDAO.findUserByEmail(accDTO.getEmail());
		if(user == null){
			return new Message(getText("err_no_user"), MessageType.ERROR);
		}
		user.setResetPassCode(UtilsPersistence.randomString());
		userDAO.saveUser(user);
		managementMessage.sendMessage(getEmailData(ID_UPDATE_PASS, user.getEmail(), user.getResetPassCode(), accDTO.getContextPath()));
		return new Message(getText("suc_fp_code_sent"), MessageType.SUCCESS);
	}

	@Override
	public Message recoverPasswordStepTwo(AccountDTO accDTO) {
		if(accDTO == null || "".equals(accDTO.getEmail().trim())){
			return new Message(getText("err_email_required"), MessageType.ERROR);
		}
		if("".equals(accDTO.getResetPassCode().trim())){
			return new Message(getText("err_fp_invalid_code"), MessageType.ERROR);
		}
		if(isEmptyPassword(accDTO.getPassEncoded())){
			return new Message(getText("err_pass_required"), MessageType.ERROR);
		}		
		User user = userDAO.findUserByEmail(accDTO.getEmail());
		if(user == null){
			return new Message(getText("err_no_user"), MessageType.ERROR);
		}
		if(user.getResetPassCode() == null || "".equals(user.getResetPassCode()) || !accDTO.getResetPassCode().equals(user.getResetPassCode())){
			return new Message(getText("err_fp_invalid_code"), MessageType.ERROR);
		}
		user.setPassword(accDTO.getPassEncoded());
		user.setResetPassCode("");
		userDAO.saveUser(user);		
		return new Message(getText("suc_fp_pass_updated"), MessageType.SUCCESS);
	}
	
	@Override
	public boolean emailExists(String email) {
		return userDAO.findUserByEmail(email) != null;
	}	
	
	@Override
	public AccountDTO getUserByUserName(UserDTO userDTO){
		User user = userDAO.findUserByEmail(userDTO.getUsername());
		if(user == null)
			return null;
		
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setFirstName(user.getFirstname());
		accountDTO.setLastName(user.getLastname());
		accountDTO.setEmail(user.getEmail());		
		return accountDTO;
	}
	
	/*** ***/
	private EmailDTO getEmailData(short actionType, String email, String code, String contextPath) {
		EmailDTO emailDTO = new EmailDTO();
		List<String> emails = new ArrayList<String>(1);
		emails.add(email);
		switch(actionType){
			case ID_CREATE_ACCOUNT:
				String urlAct = contextPath+"/activate.html?code=" + code;
				emailDTO.setSubject(getText("email_activation_subject"));		
				emailDTO.setContent(getText("email_activation_content", urlAct));
				break;
			case ID_UPDATE_PASS:
				String urlRes = contextPath+"/update-pass.html?user="+email+"&code=" + code;
				emailDTO.setSubject(getText("email_reset_pass_subject"));
				emailDTO.setContent(getText("email_reset_pass_content", urlRes));
				break;
		}		
		logger.info(">> " + emailDTO.getContent());
		emailDTO.setEmails(emails);
		return emailDTO;
	}
	private String createActivationCode() {
		String activationCode = UtilsPersistence.randomString();
		if (!userDAO.verifyIfCodeExists(activationCode))
			return activationCode;
		else
			return createActivationCode();
	}
	private boolean isFirstNameValid(String firstName){
		if(firstName.length() < ConstantsBusiness.MIN_FIRSTNAME_SIZE || firstName.length() > ConstantsBusiness.MAX_FIRSTNAME_SIZE)
			return false;
		return true;
	}
	private boolean isLastNameValid(String lastName){
		if(lastName.length() < ConstantsBusiness.MIN_LASTNAME_SIZE || lastName.length() > ConstantsBusiness.MAX_LASTNAME_SIZE)
			return false;
		return true;
	}
	private boolean isEmailValid(String email){
		Pattern pattern = Pattern.compile(ConstantsBusiness.EMAIL_REG_EXP);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	private boolean isEmptyPassword(String passEncoded){
		if(SHA1_EMPTY_STRING.equals(passEncoded))
			return true;
		return false;
	}
	private Message firstNameIsInvalid(){
		String[] errFirstNameArgs = {String.valueOf(ConstantsBusiness.MIN_FIRSTNAME_SIZE), 
				String.valueOf(ConstantsBusiness.MAX_FIRSTNAME_SIZE)};
		return new Message(getText("err_firstname_size", errFirstNameArgs), MessageType.ERROR);
	}
	private Message lastNameIsInvalid(){
		String[] errLastNameArgs = {String.valueOf(ConstantsBusiness.MIN_LASTNAME_SIZE), 
				String.valueOf(ConstantsBusiness.MAX_LASTNAME_SIZE)};
		return new Message(getText("err_lastname_size", errLastNameArgs), MessageType.ERROR);
	}
}