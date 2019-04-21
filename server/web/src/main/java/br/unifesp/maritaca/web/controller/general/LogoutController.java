package br.unifesp.maritaca.web.controller.general;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.unifesp.maritaca.web.util.ConstantsWeb;

/**
 * logout.html
 * @author jimmy
 *
 */
@Controller
public class LogoutController{

	private static Logger logger = Logger.getLogger(LogoutController.class);
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String doLogout(HttpServletRequest request){
		logger.info("logout was called");
		request.getSession().removeAttribute(ConstantsWeb.CURRENT_USER);
		request.getSession().invalidate();
		return "redirect:/index.html";
	}
}