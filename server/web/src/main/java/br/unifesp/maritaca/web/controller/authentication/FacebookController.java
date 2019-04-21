package br.unifesp.maritaca.web.controller.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.scribe.builder.api.FacebookApi;
import org.scribe.oauth.OAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@RequestMapping(value = "/facebook")
@Controller
public class FacebookController extends BaseAuthenticationController {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(FacebookController.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void signin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			
			OAuthService service = getOAuthService(FacebookApi.class,
					UtilsPersistence.retrieveConfigFile().getFbWebApiKey(),
					UtilsPersistence.retrieveConfigFile().getFbWebAppSecret(),
					UtilsPersistence.retrieveConfigFile().getFbWebRedirect(),
					UtilsPersistence.retrieveConfigFile().getFbScope());
			response.sendRedirect(service.getAuthorizationUrl(EMPTY_TOKEN));
		} catch (Exception e) {
			logger.error("fb-signin: " + e.getMessage());	 	
		}
	}
	
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String accessCode(@RequestParam(value = "code", required = true) String code, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		return loginSocial(code, FacebookApi.class,
				UtilsPersistence.retrieveConfigFile().getFbWebApiKey(),
				UtilsPersistence.retrieveConfigFile().getFbWebAppSecret(),
				UtilsPersistence.retrieveConfigFile().getFbWebRedirect(),
				UtilsPersistence.retrieveConfigFile().getFbScope(),
				REDIRECT_LOGIN_HTML_VIEW, REDIRECT_FORMS_HTML_VIEW, request, redirectAttributes);
	}
}