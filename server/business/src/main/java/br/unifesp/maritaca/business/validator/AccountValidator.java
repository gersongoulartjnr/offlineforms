package br.unifesp.maritaca.business.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.unifesp.maritaca.business.account.ManagementAccount;
import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;

@Component
public class AccountValidator implements Validator {
	
	@Autowired
	private ManagementAccount account;

	@Override
	public boolean supports(Class<?> clazz) {
		return AccountDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AccountDTO accountDTO = (AccountDTO) target;
		
		String firstname	= accountDTO.getFirstName();
		String lastname 	= accountDTO.getLastName();
		String email 		= accountDTO.getEmail();
		String password 	= accountDTO.getPassword();
		String passwordConf	= accountDTO.getPasswordConfirmation();
		
		if("".equals(firstname.trim()) || firstname.length() <= ConstantsBusiness.MIN_FIRSTNAME_SIZE || 
				firstname.length() >= ConstantsBusiness.MAX_FIRSTNAME_SIZE) {
			String[] errFirstNameArgs	= {	String.valueOf(ConstantsBusiness.MIN_FIRSTNAME_SIZE), 
					String.valueOf(ConstantsBusiness.MAX_FIRSTNAME_SIZE)};
			errors.rejectValue("firstName", "error_user_firstname_size", errFirstNameArgs, null);
		}
		
		if(lastname != null && !"".equals(lastname.trim())){
			if(lastname.length() <= ConstantsBusiness.MIN_LASTNAME_SIZE || 
					lastname.length() >= ConstantsBusiness.MAX_LASTNAME_SIZE) {
				String[] errLastNameArgs	= {	String.valueOf(ConstantsBusiness.MIN_LASTNAME_SIZE), 
						String.valueOf(ConstantsBusiness.MAX_LASTNAME_SIZE)};
				errors.rejectValue("lastName", "error_user_lastname_size", errLastNameArgs, null);
			}
		}
		
		if(account.emailExists(email)) {
			errors.rejectValue("email", "account_create_used_email");
		}
		
		Pattern pattern = Pattern.compile(ConstantsBusiness.EMAIL_REG_EXP);
		Matcher matcher = pattern.matcher(email);
		boolean matches = matcher.matches();
		if(!matches) {
			errors.rejectValue("email", "error_user_email_invalid");
		}
		
		boolean testVersion = true;
		if(!testVersion  && !accountDTO.getCaptchaValue().equals(accountDTO.getCaptchaCode())) {
			errors.rejectValue("captchaCode", "account_captcha_code_invalid");
		}
		
		if("".equals(password.trim()) || password.length() <= ConstantsBusiness.MIN_PASSWORD_SIZE ||
				password.length() >= ConstantsBusiness.MAX_PASSWORD_SIZE) {
			String[] errPasswordArgs	= {	String.valueOf(ConstantsBusiness.MIN_PASSWORD_SIZE), 
					String.valueOf(ConstantsBusiness.MAX_PASSWORD_SIZE)};
			errors.rejectValue("password", "error_user_password_size", errPasswordArgs, null);
		}		
		else if(!password.equals(passwordConf)) {
			errors.rejectValue("passwordConfirmation", "error_user_password_dont_match");
		}
	}
}