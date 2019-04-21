package br.unifesp.maritaca.web.controller.general;

import java.io.Serializable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class IndexController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
    public String showIndex(){
		return "index";
    }
}