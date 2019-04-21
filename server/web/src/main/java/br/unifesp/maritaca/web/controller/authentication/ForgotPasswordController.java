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
import br.unifesp.maritaca.web.util.UtilsWeb;

@Controller
public class ForgotPasswordController extends AbstractController{
	
	private static final long serialVersionUID = 1L;
	protected static final String USER = "user";	
	@Autowired private ManagementAccount account;
	
	@ModelAttribute(USER)
	public AccountDTO init() {
		return new AccountDTO();
	}
	
	/**
	 * User types his email
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/forgot-pass", method = RequestMethod.GET)
    public String showViewForgotPass(HttpServletRequest request) {
		if(getCurrentUser(request) == null) {		
			return "forgot_pass";
		}
		else
			return REDIRECT_FORMS_HTML_VIEW;
    }
	
	@RequestMapping(value = "/forgot-pass", method = RequestMethod.POST, 
			headers = {"content-type=application/json"})
	public @ResponseBody Message recoverPassword(HttpServletRequest request, @RequestBody AccountDTO user){
		user.setContextPath(UtilsWeb.buildContextUrl(request));
		return account.recoverPasswordStepOne(user);
	}
	
	/**
	 * User types his new password
	 * @param request
	 * @param model
	 * @param email
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/update-pass", method = RequestMethod.GET)
    public String showViewChangePass(HttpServletRequest request, Model model, 
    		@RequestParam(value = "user", required = true) String email, 
    		@RequestParam(value = "code", required = true) String code) {
		if(getCurrentUser(request) == null) {
			AccountDTO acc = new AccountDTO();
			acc.setEmail(email);
			acc.setResetPassCode(code);
			model.addAttribute(USER, acc);
			return "update_pass";
		}
		else
			return REDIRECT_FORMS_HTML_VIEW;        
    }
	
	@RequestMapping(value = "/update-pass", method = RequestMethod.POST, 
			headers = {"content-type=application/json"})
	public @ResponseBody Message changePassword(HttpServletRequest request, @RequestBody AccountDTO user){
		user.setContextPath(UtilsWeb.buildContextUrl(request));
		return account.recoverPasswordStepTwo(user);
	}
}