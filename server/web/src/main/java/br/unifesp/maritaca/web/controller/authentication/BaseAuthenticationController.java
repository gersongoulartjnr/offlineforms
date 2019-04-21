package br.unifesp.maritaca.web.controller.authentication;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.expressme.openid.Association;
import org.expressme.openid.Authentication;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.authentication.Login;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.exception.AuthorizationDenied;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.oauth.dto.OAuthParamsDTO;
import br.unifesp.maritaca.web.controller.AbstractController;
import br.unifesp.maritaca.web.util.ConstantsWeb;
import br.unifesp.maritaca.web.util.UtilsWeb;

import com.google.gson.Gson;

public class BaseAuthenticationController extends AbstractController {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(BaseAuthenticationController.class);
	protected static final String USER = "user";
	protected OpenIdManager manager;
	 
	protected static final Token EMPTY_TOKEN 	= null;
	protected static short SUCCESS_CODE 		= 200;
	protected static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
	protected static final String ERROR_MESSAGE = "error_message";
	protected static final String OAUTH_PARAMS = "oAuthParams";
	
	@Autowired 
	protected Login login;
	
	protected String loginOpenId(String strReturn, String strReturnTo, String op, 
			HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		if(op != null) {
			manager = new OpenIdManager();
			String returnToUrl = UtilsWeb.buildContextUrl(request);
			manager.setRealm(returnToUrl);
			manager.setReturnTo(returnToUrl+strReturnTo);
			if("Google".equals(op) || "Yahoo".equals(op)) {
				Endpoint endpoint = manager.lookupEndpoint(op);
				Association association = manager.lookupAssociation(endpoint);
				request.getSession().setAttribute(ConstantsWeb.ATTR_MAC, association.getRawMacKey());
				request.getSession().setAttribute(ConstantsWeb.ATTR_ALIAS, endpoint.getAlias());
				String url = manager.getAuthenticationUrl(endpoint, association);
				response.sendRedirect(url);
			}
		}
        return strReturn;
	}
	
	protected String homeOpenId(String defaultReturn, String successReturn,  
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) 
			throws IOException {
		String endPoint = request.getParameter(ConstantsWeb.ATTR_ENDPOINT);
		if(endPoint != null) {
			String formId = (String)request.getSession().getAttribute(ConstantsWeb.FORM_ID_NAME);
			byte[] mac_key = (byte[]) request.getSession().getAttribute(ConstantsWeb.ATTR_MAC);
			String alias = (String) request.getSession().getAttribute(ConstantsWeb.ATTR_ALIAS);
			Authentication authentication = manager.getAuthentication(request, mac_key, alias);
			if(authentication.getFirstname() != null && authentication.getEmail() != null) {
				AccountDTO accountDTO = new AccountDTO();
				accountDTO.setFirstName(authentication.getFirstname());
				accountDTO.setLastName(authentication.getLastname());
				accountDTO.setEmail(authentication.getEmail());
				accountDTO.setFormId(formId);
				
				try{
					accountDTO = login.doLoginSocial(accountDTO);
					if(accountDTO.getKey() != null) {
						UserDTO maritacaUser = new UserDTO(accountDTO.getEmail());
						maritacaUser.setFullName(accountDTO.retrieveFullName());			
						maritacaUser.setLoggedBefore(accountDTO.getLoggedBefore());
						if(successReturn.equals(REDIRECT_FORMS_HTML_VIEW)) {
							setCurrentUser(request, maritacaUser);
							logger.info("Server-LoginOpenId: " + maritacaUser.getUsername());
							return successReturn;
						}
						else {
							logger.info("Mobile-LoginOpenId: " + maritacaUser.getUsername());
							return successReturn+buildUrl(new OAuthParamsDTO(
									ConstantsWeb.ATTR_RESPONSE_TYPE, 
									ConstantsWeb.ATTR_CLIENT_ID, 
									ConstantsWeb.ATTR_REDIRECT_URI))+"&user_id="+maritacaUser.getUsername();
						}
					}
				} catch (AuthorizationDenied e) {
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getUserMessage());
					return "redirect:/mobile-login.html?"+buildUrl(getOAuthParamsDTO(request));
				} catch (MaritacaException e) {
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
					return "redirect:/mobile-login.html?"+buildUrl(getOAuthParamsDTO(request));
				}
			}
		}
		return defaultReturn;
	}
	
	protected OAuthParamsDTO getOAuthParamsDTO(HttpServletRequest request) {
		return (OAuthParamsDTO)request.getSession().getAttribute(OAUTH_PARAMS);
	}
	
	protected String loginSocial(String code, Class clazz, String apiKey, String appSecret, String redirectUri, String scope,
			String defaultReturn, String successReturn, HttpServletRequest request, RedirectAttributes redirectAttributes){
		try{
			OAuthService service = getOAuthService(clazz, apiKey, appSecret, redirectUri, scope);
			Verifier verifier = new Verifier(code);
			Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
			
			OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		    service.signRequest(accessToken, oAuthRequest);
		    Response scribeResponse = oAuthRequest.send();
		    if(scribeResponse.getCode() == SUCCESS_CODE){
		    	String formId = (String)request.getSession().getAttribute(ConstantsWeb.FORM_ID_NAME);
		    	Gson gson = new Gson();
		    	HashMap<String, String> socUser = gson.fromJson(scribeResponse.getBody(), HashMap.class);
		    	AccountDTO accountDTO = new AccountDTO();
				accountDTO.setFirstName(socUser.get("first_name"));
				accountDTO.setLastName(socUser.get("last_name"));
				accountDTO.setEmail(socUser.get("email"));
				accountDTO.setFormId(formId);
				accountDTO = login.doLoginSocial(accountDTO);
				
				if(accountDTO.getKey() != null) {
					UserDTO maritacaUser = new UserDTO(accountDTO.getEmail());
					maritacaUser.setFullName(accountDTO.retrieveFullName());			
					maritacaUser.setLoggedBefore(accountDTO.getLoggedBefore());
					if(successReturn.equals(REDIRECT_FORMS_HTML_VIEW)) {
						setCurrentUser(request, maritacaUser);
						logger.info("Server-LoginSocial: " + maritacaUser.getUsername());
						return successReturn;
					}
					else {
						logger.info("Mobile-LoginSocial: " + maritacaUser.getUsername());
						return successReturn+buildUrl(new OAuthParamsDTO(
								ConstantsWeb.ATTR_RESPONSE_TYPE, 
								ConstantsWeb.ATTR_CLIENT_ID, 
								ConstantsWeb.ATTR_REDIRECT_URI))+"&user_id="+maritacaUser.getUsername();
					}
				}
		    }
		} catch (AuthorizationDenied e) {
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getUserMessage());
			return "redirect:/mobile-login.html?"+buildUrl(getOAuthParamsDTO(request));
		} catch (MaritacaException e) {
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
			return "redirect:/mobile-login.html?"+buildUrl(getOAuthParamsDTO(request));		 
		} catch(Exception ex){
			logger.error("loginSocial: " + ex.getMessage());
		}	    
	    return defaultReturn;
	}	
	
	protected OAuthService getOAuthService(Class clazz, String apiKey, String appSecret, String redirectUri, String scope){
		return new ServiceBuilder()
        .provider(clazz)
        .apiKey(apiKey)
        .apiSecret(appSecret)
        .callback(redirectUri)
        .scope(scope)
        .build();
	}
}