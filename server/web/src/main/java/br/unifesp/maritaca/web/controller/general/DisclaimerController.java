package br.unifesp.maritaca.web.controller.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * disclaimer.html
 * @author jimmy
 *
 */
@Controller
public class DisclaimerController{

	@RequestMapping(value = "/disclaimer", method = RequestMethod.GET)
    public String showView(){
        return "disclaimer";
    }
}