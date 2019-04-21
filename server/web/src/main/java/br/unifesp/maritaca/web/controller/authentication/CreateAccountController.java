package br.unifesp.maritaca.web.controller.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unifesp.maritaca.business.account.ManagementAccount;
import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;
import br.unifesp.maritaca.web.util.UtilsWeb;

/**
 * 
 * @author Jimmy Valverde Sanchez
 *
 */
@Controller
public class CreateAccountController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	private static final String USER = "user";
	@Autowired private ManagementAccount account;
	
	private static final String ACT_MESSAGE = "actMessage";	
	
	@ModelAttribute(USER)
	public AccountDTO init() {
		return new AccountDTO();
	}

	@RequestMapping(value = "/create-account", method = RequestMethod.GET)
    public String showViewNewAccount() {
        return "create_account";
    }
	
	@RequestMapping(value = "/create-account", method = RequestMethod.POST, 
			headers = {"content-type=application/json"})
    public @ResponseBody Message createAccount(HttpServletRequest request, @RequestBody AccountDTO user){		
		String captchaValue = (String) request.getSession().getAttribute(ConstantsWeb.CAPTCHA_SESSION_KEY);	
		user.setCaptchaValue(captchaValue);
		user.setContextPath(UtilsWeb.buildContextUrl(request));		
		return account.createAccount(user);
	}
	
	@RequestMapping(value = "/activate", method = RequestMethod.GET)
    public String activateAccount(@RequestParam(value = "code", required = true) String code, 
    		Model model) {
		Message message = account.activateAccount(code);
		model.addAttribute(ACT_MESSAGE, message.getMessage());
        return "activate_account";
    }
}