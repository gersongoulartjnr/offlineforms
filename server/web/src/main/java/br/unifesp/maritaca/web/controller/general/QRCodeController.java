package br.unifesp.maritaca.web.controller.general;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QRCodeController {

	@RequestMapping(value = "/qr-code", method = RequestMethod.GET)
    public String showQRCode(@RequestParam(value = "url", required = true) String url,
    		HttpServletRequest request){
		request.setAttribute("urlQR", url);
        return "qr_code";
    }	
}