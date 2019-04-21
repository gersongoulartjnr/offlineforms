package br.unifesp.maritaca.web.controller.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * about.html
 * @author jimmy
 *
 */
@Controller
public class AboutController{

	@RequestMapping(value = "/about", method = RequestMethod.GET)
    public String showView(){
        return "about";
    }
}