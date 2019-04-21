package br.unifesp.maritaca.web.controller.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * developers.html
 * @author jimmy
 *
 */
@Controller
public class DevelopersController{

	@RequestMapping(value = "/developers", method = RequestMethod.GET)
    public String showView(){
        return "developers";
    }	
}