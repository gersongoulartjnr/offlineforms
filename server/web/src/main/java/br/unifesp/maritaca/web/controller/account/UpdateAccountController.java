package br.unifesp.maritaca.web.controller.account;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unifesp.maritaca.business.account.ManagementAccount;
import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.web.controller.AbstractController;

@Controller
public class UpdateAccountController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	private static final String USER = "user";
	@Autowired private ManagementAccount account;	
	
	@ModelAttribute(USER)
	public AccountDTO init() {
		return new AccountDTO();
	}

	@RequestMapping(value = "/my-account", method = RequestMethod.GET)
    public String showViewUpdateAccount(ModelMap model, HttpServletRequest request) {		
		AccountDTO accountDTO = account.getUserByUserName(getCurrentUser(request));
		if(accountDTO != null) {
			model.addAttribute(USER, accountDTO);
		}
        return "my_account";
    }
	
	@RequestMapping(value = "/my-account", method = RequestMethod.POST, 
			headers = {"content-type=application/json"})
    public @ResponseBody Message updateAccount(HttpServletRequest request, @RequestBody AccountDTO user){
		return account.updateAccount(getCurrentUser(request), user);
	}
}