package br.unifesp.maritaca.web.controller.form;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.unifesp.maritaca.web.controller.AbstractController;

@Controller
public class ApkController extends AbstractController {

	private static final long serialVersionUID = 1L;

	@RequestMapping(value = "/send-apk-link", method = RequestMethod.GET)
	public String showView(HttpServletRequest request) {
		
        return "form_send_app_link";
    }
}