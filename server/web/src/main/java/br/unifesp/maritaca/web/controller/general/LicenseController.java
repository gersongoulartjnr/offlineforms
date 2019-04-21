package br.unifesp.maritaca.web.controller.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * license.html
 * @author jimmy
 *
 */
@Controller
public class LicenseController{
	
	@RequestMapping(value = "/license", method = RequestMethod.GET)
    public String showView(){
        return "license";
    }
}