package br.unifesp.maritaca.web.controller.authentication;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.smartam.leeloo.common.OAuth;

import org.apache.log4j.Logger;
import org.scribe.builder.api.FacebookApi;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.enums.MessageType;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.AuthorizationDeniedAlternative;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.form.ManagementFormAccessRequest;
import br.unifesp.maritaca.business.oauth.dto.OAuthParamsDTO;
import br.unifesp.maritaca.persistence.permission.RequestStatusType;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;
import br.unifesp.maritaca.web.util.ConstantsWeb;

@Controller
public class MobileLoginController extends BaseAuthenticationController {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MobileLoginController.class);
	private static final String CLIENT_ID = "clientId";
	private static final String CURRENT_YEAR = "currentYear";
	private static final String SUCCESS_RETURN = "redirect:/oauth/authorizationConfirm?";
	private static final String ACCESS_REQUEST_URL = "form-access-request/request-access.html";
	private static final String REQUEST_MESSAGE = "request_message";
	
	@Autowired
	private ManagementFormAccessRequest managFormAccessRequest;
	
	@ModelAttribute(USER)
	public AccountDTO init() {	
		return new AccountDTO();
	}
	
	@ModelAttribute(OAUTH_PARAMS)
	public OAuthParamsDTO initOAuthParams() {	
		return new OAuthParamsDTO();
	}
	
	/*** Login ***/
	@RequestMapping(value = "/mobile-login", method = RequestMethod.GET)
    public String showLogin(@RequestParam(value = OAuth.OAUTH_RESPONSE_TYPE, required = true) String responseType, 
    		@RequestParam(value = OAuth.OAUTH_CLIENT_ID, required = true) String clientId, 
    		@RequestParam(value = OAuth.OAUTH_REDIRECT_URI, required = true) String redirectUri,
    		@RequestParam(value = ConstantsWeb.FORM_ID_NAME, required = true) String formId,
    		HttpServletRequest request, Model model) {
		request.getSession().setAttribute(OAUTH_PARAMS, new OAuthParamsDTO(responseType, clientId, redirectUri, formId));
		request.getSession().setAttribute(ConstantsWeb.FORM_ID_NAME, formId);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		model.addAttribute(CURRENT_YEAR, year);
        return "mobile_login";
    }
	
	@RequestMapping(value = "/mobile-login", method = RequestMethod.POST)
    public String doLogin(@Valid @ModelAttribute(USER) AccountDTO user, BindingResult result, Errors errors, HttpServletRequest request, 
    		RedirectAttributes redirectAttributes) {
		
		OAuthParamsDTO oAuthParamsDTO = getOAuthParamsDTO(request);	
		if(oAuthParamsDTO != null) {
			String formId = (String)request.getSession().getAttribute(ConstantsWeb.FORM_ID_NAME);
			try {
				user.setFormId(formId);
				Message message = login.doLogin(user);
				
				if(message != null && message.getType().equals(MessageType.SUCCESS)) {
					UserDTO maritacaUser = new UserDTO(user.getEmail());
					setMobileCurrentUser(request, maritacaUser);
					logger.info("Mobile-Login: " + maritacaUser.getUsername());
					return "redirect:/mobile-confirmation.html";
				}
				else {	
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE, message.getMessage());
					return "redirect:/mobile-login.html?"+buildUrl(oAuthParamsDTO);
				}
			} catch (AuthorizationDeniedAlternative e) {
				if (e.getStatus() == null) {
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getUserMessage() + " " + e.getExtraMessage());
					redirectAttributes.addFlashAttribute(REQUEST_MESSAGE, 
							"<a href=\"" + buildRequestPermissionURI(formId, user.getEmail()) + "\">Click here</a>");
					return "redirect:/mobile-login.html?"+buildUrl(oAuthParamsDTO);
				} else if (e.getStatus().equals(RequestStatusType.PENDING)) { 
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getUserMessage() + " " + e.getExtraMessage());
					return "redirect:/mobile-login.html?"+buildUrl(oAuthParamsDTO);
				} else {
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getUserMessage());
					return "redirect:/mobile-login.html?"+buildUrl(oAuthParamsDTO);
				}
			} catch (AuthorizationDenied e) {
				redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getUserMessage());
				return "redirect:/mobile-login.html?"+buildUrl(oAuthParamsDTO);
			} catch (MaritacaException e) {
				redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
				return "redirect:/mobile-login.html?"+buildUrl(oAuthParamsDTO);
			}
		}
		else {
			return REDIRECT_LOGIN_HTML_VIEW;
		}
	}
	
	@RequestMapping(value = "/form-access-request/request-access", method = RequestMethod.GET)
    public String sendRequestForPermission(
    		@RequestParam(value = ConstantsWeb.FORM_ID_NAME, required = true) String formId,
    		@RequestParam(value = ConstantsWeb.USER_NAME, required = true) String userEmail,
    		HttpServletRequest request, Model model) {
		OAuthParamsDTO oAuthParamsDTO = getOAuthParamsDTO(request);
		managFormAccessRequest.askForPermission(new UserDTO(userEmail), formId);
		return "redirect:/mobile-login.html?"+buildUrl(oAuthParamsDTO);
    }
		
	private String buildRequestPermissionURI(String formkey, String username){
		StringBuilder str = new StringBuilder();
		str.append(ACCESS_REQUEST_URL);
		str.append("?");
		str.append(ConstantsWeb.FORM_ID_NAME);
		str.append("=");
		str.append(formkey);
		str.append("&");
		str.append(ConstantsWeb.USER_NAME);
		str.append("=");
		str.append(username);
		return str.toString();
	}
	
	/*** Confirmation ***/
	@RequestMapping(value = "/mobile-confirmation", method = RequestMethod.GET)
    public String showConfirmation(HttpServletRequest request, Model model) {
		OAuthParamsDTO oauthParams = getOAuthParamsDTO(request);		
		
		if(getMobileCurrentUser(request) == null || oauthParams == null) {
			return "redirect:/mobile-login.html";
		}
		model.addAttribute(CLIENT_ID, oauthParams.getClientId());
		int year = Calendar.getInstance().get(Calendar.YEAR);
		model.addAttribute(CURRENT_YEAR, year);
        return "mobile_conf";
    }
	
	@RequestMapping(value = "/mobile-confirmation", method = RequestMethod.POST)
    public String allowConfirmation(@Valid @ModelAttribute(OAUTH_PARAMS) OAuthParamsDTO oAuthParams, HttpServletRequest request) {
		OAuthParamsDTO oAuthParamsDTO = getOAuthParamsDTO(request);
		UserDTO userDTO = getMobileCurrentUser(request);
		if(userDTO != null)	{
			return "redirect:/oauth/authorizationConfirm?"+buildUrl(oAuthParamsDTO)+"&user_id="+userDTO.getUsername();
		}
		else
			return REDIRECT_LOGIN_HTML_VIEW;
	}
	
	@RequestMapping(value = "/mobile-confirmation", params="cancel", method = RequestMethod.POST)
    public String cancelConfirmation(@Valid @ModelAttribute(OAUTH_PARAMS) OAuthParamsDTO oAuthParams, HttpServletRequest request) {
		OAuthParamsDTO oAuthParamsDTO = getOAuthParamsDTO(request);
		UserDTO userDTO = getCurrentUser(request);
		if(userDTO != null)
			return "redirect:/oauth/authorizationConfirm?"+buildUrl(oAuthParamsDTO);
		else
			return REDIRECT_MOBILE_LOGIN_HTML_VIEW+"?"+buildUrl(new OAuthParamsDTO(
					ConstantsWeb.ATTR_RESPONSE_TYPE, 
					ConstantsWeb.ATTR_CLIENT_ID, 
					ConstantsWeb.ATTR_REDIRECT_URI));
	}	
	
	/*** OpenId ***/
	@RequestMapping(value = "/mobile-login-openid", method = RequestMethod.GET)
	public String showLoginOpenId(@RequestParam(ConstantsWeb.ATTR_OP) String op, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		return loginOpenId("mobile_login", "/mobile-home-openid.html", op, request, response);
	}
	
	@RequestMapping(value="/mobile-home-openid", method = RequestMethod.GET)
	public String showHomeOpenId(Model model, 
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) 
					throws IOException{
		String successReturn = SUCCESS_RETURN;
		logger.info("homeOpenId>> " + request.getSession().getAttribute(ConstantsWeb.FORM_ID_NAME));
		return homeOpenId(REDIRECT_MOBILE_LOGIN_HTML_VIEW, successReturn, request, response, redirectAttributes);
	}
	
	/*** Social Login***/
	@RequestMapping(value = "/facebook/mobile-login", method = RequestMethod.GET)
	public void signin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			OAuthService service = getOAuthService(FacebookApi.class, 
					UtilsPersistence.retrieveConfigFile().getFbMobApiKey(),
					UtilsPersistence.retrieveConfigFile().getFbMobAppSecret(),
					UtilsPersistence.retrieveConfigFile().getFbMobRedirect(),
					UtilsPersistence.retrieveConfigFile().getFbScope());
			response.sendRedirect(service.getAuthorizationUrl(EMPTY_TOKEN));
		} catch (Exception e) {
			logger.error("fb-mob-signin: " + e.getMessage()); 	
		}
	}
	
	@RequestMapping(value = "/facebook/mobile-callback", method = RequestMethod.GET)
	public String accessCode(@RequestParam(value = "code", required = true) String code, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String successReturn = SUCCESS_RETURN;
		logger.info("homeSocial>> " + request.getSession().getAttribute(ConstantsWeb.FORM_ID_NAME));
		return loginSocial(code, FacebookApi.class, 
				UtilsPersistence.retrieveConfigFile().getFbMobApiKey(),
				UtilsPersistence.retrieveConfigFile().getFbMobAppSecret(),
				UtilsPersistence.retrieveConfigFile().getFbMobRedirect(),
				UtilsPersistence.retrieveConfigFile().getFbScope(),
				REDIRECT_MOBILE_LOGIN_HTML_VIEW, successReturn, request, redirectAttributes);
	}
}