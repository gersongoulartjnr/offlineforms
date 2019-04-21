package br.unifesp.maritaca.web.controller.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * contact-us.html
 * @author jimmy
 *
 */
@Controller
public class ContactUsController{

	@RequestMapping(value = "/contact-us", method = RequestMethod.GET)
	public String showView(){
		return "contact_us";
	}
}