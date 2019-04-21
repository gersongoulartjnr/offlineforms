package br.unifesp.maritaca.web.controller.authentication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.web.util.ConstantsWeb;

/**
 * 
 * @author Tiago Barabasz
 * @author Jimmy Valverde S&aacute;nchez
 *
 */
@Controller
public class LoginController extends BaseAuthenticationController {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LoginController.class);

	@ModelAttribute(USER)
	public AccountDTO init() {
		return new AccountDTO();
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
    public String showLogin(HttpServletRequest request, Model model) {
		int maxInactiveInterval = request.getSession().getMaxInactiveInterval();
		request.getSession().setAttribute(ConstantsWeb.SESSION_TIMEOUT, maxInactiveInterval);
		if(getCurrentUser(request) == null) {
			return "index";			
		}
		else {
			return REDIRECT_FORMS_HTML_VIEW;
		}
    }
	
	@RequestMapping(value = "/getinactivetime", method = RequestMethod.POST)
	public @ResponseBody Long checkTimeoutSession(HttpServletRequest request) {
		Object object = request.getSession().getAttribute(ConstantsWeb.LAST_SESSION_ACCESS);
		long currentTime = (new Date()).getTime();
		if (object == null) {
			return currentTime;
		}
		long value = (Long) object;
		logger.info("session value : " + (currentTime - value));
		return (currentTime - value);
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.POST)
    public String doLogin(@Valid @ModelAttribute(USER) AccountDTO user, BindingResult result, Errors errors, 
    		Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
				
		//TODO: result.hasErrors() ...?
		Message loginMessage = login.doLogin(user);
		if(loginMessage != null && loginMessage.getType().equals(MessageType.SUCCESS)) {
			AccountDTO accountDTO = (AccountDTO)loginMessage.getData();
			UserDTO maritacaUser = new UserDTO(user.getEmail());
			maritacaUser.setFullName(accountDTO.retrieveFullName());			
			maritacaUser.setLoggedBefore(user.getLoggedBefore());
			setCurrentUser(request, maritacaUser);
			logger.info("Server-Login: " + maritacaUser.getUsername());
			logger.info("name: " + maritacaUser.getFullName());
			//
			String url = (String)request.getSession().getAttribute(ConstantsWeb.DOWNLOAD_APK);
			if(url != null || "".equals(url)) {
				try {					
					Message message = login.getApkByUrl(url, getCurrentUser(request));
					if(message != null && message.getType().equals(MessageType.SUCCESS)) {
						if(message.getData() != null) {
							byte[] apkBytes = (byte[])message.getData();
							response.setContentType(ConstantsBusiness.MOB_MIMETYPE);
							response.setHeader("Content-Disposition", "attachment;Filename=\"" + url+".apk"+ "\"");
							response.setContentLength((int) apkBytes.length);					
							OutputStream os = response.getOutputStream();
							os.write(apkBytes);           
							os.flush(); 
							os.close();
						}
						else {//
							message.setMessage("You need to login first.");
							redirectAttributes.addFlashAttribute(MESSAGE, message);
							request.getSession().setAttribute(ConstantsWeb.DOWNLOAD_APK, url);
							return REDIRECT_LOGIN_HTML_VIEW;
						}				
					} else {
						message.setMessage("You cannot download this APK.");
						redirectAttributes.addFlashAttribute(MESSAGE, message);
						return "redirect:/forms.html";				
					}
				}
				catch(Exception e) {
					redirectAttributes.addFlashAttribute(MESSAGE, new Message("Error downloading Mobile App", MessageType.ERROR));
					logger.error("Error downloading Mobile App: " + url);
					return "redirect:/form.html?id="+url;
				}
			}
			//
			return REDIRECT_FORMS_HTML_VIEW;
		}
		else {
			if(loginMessage != null) {
				model.addAttribute(ERROR_MESSAGE, loginMessage.getMessage());
			}
			return "index";
		}
	}

	/* OpenId */
	@RequestMapping(value = "/login-openid", method = RequestMethod.GET)
	public String showLoginOpenId(@RequestParam(ConstantsWeb.ATTR_OP) String op, 
			HttpServletRequest request, HttpServletResponse response) 
					throws IOException {
		return loginOpenId("index", "/home-openid.html", op, request, response);
	}
	
	@RequestMapping(value="/home-openid", method = RequestMethod.GET)
	public String showHomeOpenId(/*@RequestParam("openid.response_nonce") String nonce, */Model model, 
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) 
					throws IOException{
		return homeOpenId(REDIRECT_LOGIN_HTML_VIEW, REDIRECT_FORMS_HTML_VIEW, request, response, redirectAttributes);
	}
	
	/* Public Forms */
	@RequestMapping(value = "/public-forms", method = RequestMethod.GET)
	public @ResponseBody List<FormDTO> getTopPublicForms(HttpServletRequest request) {
		return login.getTopPublicForms();
	}
	@RequestMapping(value = "/search-public-forms", method = RequestMethod.GET)
	public @ResponseBody List<FormDTO> searchPublicForms(HttpServletRequest request, 
			@RequestParam(value = "search", required = true) String search) {
		return login.searchPublicForms(search);
	}
	
	/* Gets APK of public forms */
	@RequestMapping(value = "/get-app", params = "android-app", method = RequestMethod.GET)
    public String getAPK(@RequestParam(value = "id", required = true) String url, 
    		ModelMap model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		
		try {	
			Message message = login.getAPK(url);
			if(message != null && message.getType().equals(MessageType.SUCCESS)) {
				byte[] apkBytes = (byte[])message.getData();
				response.setContentType(ConstantsBusiness.MOB_MIMETYPE);
	            response.setHeader("Content-Disposition", "attachment;Filename=\"" + url+".apk"+ "\"");
	            response.setContentLength((int) apkBytes.length);	            			
				OutputStream os = response.getOutputStream();
				os.write(apkBytes);
	            os.flush(); 
	            os.close();
			}
			else{
				redirectAttributes.addFlashAttribute(MESSAGE, message);
				return "redirect:/index.html";
			}
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute(MESSAGE, new Message("Error downloading Mobile App", MessageType.ERROR));
			logger.error("Error downloading Mobile App: " + url);
			return "redirect:/index.html";
		}
		return null;
    }
	
	@RequestMapping(value = "/form/valid", method = RequestMethod.POST)
	public @ResponseBody String isAPKBuilding(@RequestParam(value = "id", required = true) String url, HttpServletRequest request) {
		if (!"".equals(url) && login.isAPKBuilding(url)){
			return "true";
		}
		return "false";
	}
}