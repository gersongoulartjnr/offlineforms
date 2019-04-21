package br.unifesp.maritaca.web.controller;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.smartam.leeloo.common.OAuth;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthParamsDTO;
import br.unifesp.maritaca.web.util.ConstantsWeb;

public abstract class AbstractController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected static final String MESSAGE		= "maritaca_message";	
	protected static final String APK_BUILDING 	= "building_apk";
	protected static final String DEFAULT_HTML_VIEW		= "index.html";
	protected static final String LOGIN_HTML_VIEW			= "index.html";
	protected static final String MOBILE_LOGIN_HTML_VIEW 	= "mobile-login.html";
	protected static final String DEFAULT_TILES_NAME		= "index";
	protected static final String FORMS_HTML_VIEW			= "forms.html";
	
	public    static final String REDIRECT_DEFAULT_HTML_VIEW		= "redirect:/"+DEFAULT_HTML_VIEW;
	protected static final String REDIRECT_LOGIN_HTML_VIEW		= "redirect:/"+LOGIN_HTML_VIEW;
	protected static final String REDIRECT_MOBILE_LOGIN_HTML_VIEW	= "redirect:/"+MOBILE_LOGIN_HTML_VIEW;
	protected static final String REDIRECT_FORMS_HTML_VIEW		= "redirect:/"+FORMS_HTML_VIEW;
	
	public boolean isAllowed(HttpServletRequest request) {
		boolean isAllowed = false;
		if(getCurrentUser(request) != null) {
			request.getSession().setAttribute(ConstantsWeb.LAST_SESSION_ACCESS, (new Date()).getTime());
			isAllowed = true;
		} 
		return isAllowed;
	}
	
	/* server's users*/
	protected UserDTO getCurrentUser(HttpServletRequest request) {
		return (UserDTO)request.getSession().getAttribute(ConstantsWeb.CURRENT_USER);
	}	
	protected void setCurrentUser(HttpServletRequest request, UserDTO maritacaUser){
		request.getSession().setAttribute(ConstantsWeb.CURRENT_USER, maritacaUser);
	}
	
	/* mobile's users*/
	protected UserDTO getMobileCurrentUser(HttpServletRequest request) {
		return (UserDTO)request.getSession().getAttribute(ConstantsWeb.MOBILE_CURRENT_USER);
	}	
	protected void setMobileCurrentUser(HttpServletRequest request, UserDTO maritacaUser){
		request.getSession().setAttribute(ConstantsWeb.MOBILE_CURRENT_USER, maritacaUser);
	}
	
	/* asynchronous calls */
	protected void setValueInBroadcaster(String broadcasterName, String message) {
		Broadcaster b = BroadcasterFactory.getDefault().lookup(broadcasterName, true);
		if (message != null) {
			b.broadcast(message);
		}
	}
	
	protected String buildUrl(OAuthParamsDTO oAuthParamsDTO) {
		if(oAuthParamsDTO == null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append(OAuth.OAUTH_RESPONSE_TYPE);
		sb.append("=");
		sb.append(oAuthParamsDTO.getResponseType());
		sb.append("&");
		sb.append(OAuth.OAUTH_CLIENT_ID);
		sb.append("=");
		sb.append(oAuthParamsDTO.getClientId());
		sb.append("&");				
		sb.append(OAuth.OAUTH_REDIRECT_URI);
		sb.append("=");
		sb.append(oAuthParamsDTO.getRedirectUri());
		if(oAuthParamsDTO.getFormId() != null){
			sb.append("&");
			sb.append(ConstantsWeb.FORM_ID_NAME);
			sb.append("=");
			sb.append(oAuthParamsDTO.getFormId());
		}
		return sb.toString();
	}
}