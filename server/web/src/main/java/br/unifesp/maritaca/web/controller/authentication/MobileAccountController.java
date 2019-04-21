package br.unifesp.maritaca.web.controller.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.unifesp.maritaca.business.account.ManagementAccount;
import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.oauth.dto.OAuthParamsDTO;
import br.unifesp.maritaca.business.validator.AccountValidator;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;
import br.unifesp.maritaca.web.util.UtilsWeb;

@Controller
public class MobileAccountController extends AbstractController {

	private static final long serialVersionUID = 1L;
	private static final String USER = "user";
	private static final String USER_REGISTERED = "userRegistered";
	private static final String MOB_LOGIN_URL = "mobLoginUrl";
	private static final String ACT_MESSAGE = "actMessage";
	
	@Autowired
	private ManagementAccount account;
	
	@Autowired
	private AccountValidator accountValidator;
	
	@ModelAttribute(USER)
	public AccountDTO init() { 
		return new AccountDTO();
	}
	
	@RequestMapping(value = "/mobile-create-account", method = RequestMethod.GET)
    public String showView(HttpServletRequest request) {
        return "mob_create_account";
    }
	
	@RequestMapping(value = "/mobile-create-account", method = RequestMethod.POST)
    public String saveAccount(@Valid @ModelAttribute(USER) AccountDTO user, BindingResult result, 
    		HttpServletRequest request, Model model) {
		String captchaValue = (String) request.getSession().getAttribute(ConstantsWeb.CAPTCHA_SESSION_KEY);	
		user.setCaptchaValue(captchaValue);
			
		accountValidator.validate(user, result);
		if(result.hasErrors()) {
			return "mob_create_account";
		}
		user.setContextPath(UtilsWeb.buildContextUrl(request));
		Message message = account.saveNewAccount(user);
		if(message.getType().equals(MessageType.SUCCESS)) {
			String formId = (String)request.getSession().getAttribute(ConstantsWeb.FORM_ID_NAME);
			String mobLoginUrl = MOBILE_LOGIN_HTML_VIEW+"?"+buildUrl(new OAuthParamsDTO(
					ConstantsWeb.ATTR_RESPONSE_TYPE, 
					ConstantsWeb.ATTR_CLIENT_ID, 
					ConstantsWeb.ATTR_REDIRECT_URI,
					formId));
			
			message.setMessage("Account was created");
			model.addAttribute(USER_REGISTERED, message.getMessage());
			model.addAttribute(MOB_LOGIN_URL, mobLoginUrl);			
			return "mob_create_account";
		}
		else {
			model.addAttribute(MESSAGE, message);
			return "mob_create_account";
		}
	}
	
	@RequestMapping(value = "/mobile-activate", method = RequestMethod.GET)
    public String activateAccount(@RequestParam(value = "code", required = true) String code, 
    		Model model) {
		Message message = account.activateAccount(code);
		model.addAttribute(ACT_MESSAGE, message.getMessage());
        return "mob_activate_account";
    }
}