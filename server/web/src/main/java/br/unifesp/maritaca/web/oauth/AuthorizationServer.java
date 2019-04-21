package br.unifesp.maritaca.web.oauth;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartam.leeloo.as.issuer.MD5Generator;
import net.smartam.leeloo.as.issuer.OAuthIssuerImpl;
import net.smartam.leeloo.as.request.OAuthAuthzRequest;
import net.smartam.leeloo.common.OAuth;
import net.smartam.leeloo.common.exception.OAuthProblemException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.oauth.ManagementOAuth;
import br.unifesp.maritaca.business.oauth.dto.DataAccessTokenDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthClientDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthCodeDTO;
import br.unifesp.maritaca.business.oauth.dto.OAuthTokenDTO;
import br.unifesp.maritaca.web.util.ConstantsWeb;
import br.unifesp.maritaca.web.util.UtilsWeb;

@Component("authorizationServer")
public class AuthorizationServer extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(AuthorizationServer.class);
	private static final long serialVersionUID = 1L;

	private static final String AUTHORIZATION_REQUEST = "/authorizationRequest";
	private static final String AUTHORIZATION_CONFIRM = "/authorizationConfirm";
	private static final String ACCESS_TOKEN_REQUEST  = "/accessTokenRequest";

	private static final String USER_PARAM            = "user";

	private OAuthAuthzRequest oauthRequest    = null;	
	private OAuthIssuerImpl   oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
	
	@Autowired private ManagementOAuth managementOAuth;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
		logger.info("Loading OAuth AuthorizationServer Servlet");
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String pathInfo = request.getPathInfo();
		logger.debug("Serving " + pathInfo);
		logger.debug("Query " + request.getQueryString());

		if (AUTHORIZATION_REQUEST.equals(pathInfo)) {
			authorize(request, response);
		} else if(AUTHORIZATION_CONFIRM.equals(pathInfo)) {
			authorizationConfirm(request, response);
		} else if(ACCESS_TOKEN_REQUEST.equals(pathInfo)) {
			accessToken(request, response);
		} else {
			response.setContentType(ConstantsWeb.APPLICATION_JSON);
			UtilsWeb.sendValuesInJson(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST),
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "Bad Request");
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		}
	}

	/**
	 * This method allows to the third-party application request an authorization
	 * through a request URI by adding the following parameters: client_id, response_type
	 * and redirect_uri, these parameters are mandatory. If the client_id exist in the
	 * system this redirect to the user login to ask if he agrees. 
	 * @param request 
	 * @param response
	 */
	public void authorize(HttpServletRequest request, HttpServletResponse response){
		try {
			oauthRequest = new OAuthAuthzRequest(request);
			String clientId = oauthRequest.getClientId();
			OAuthClientDTO clientDTO = managementOAuth.findOAuthClient(clientId);
			if(clientDTO == null) {
				UtilsWeb.makeResponseInJSON(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_NO_CONTENT), 
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "client_id not found",
						ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
				response.setStatus(HttpURLConnection.HTTP_NO_CONTENT);
				return;
			}
			
			String clientSecret = oauthRequest.getClientSecret();
			if (clientSecret == null) {
				UtilsWeb.makeResponseInJSON(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST), 
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "Missing parameters: client_secret",
						ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
				response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
				return;
			}
			if(!clientDTO.getSecret().equals(clientSecret)) {
				UtilsWeb.makeResponseInJSON(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED), 
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "Authentication failed",
						ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
				response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
				return;
			}
			
			response.setStatus(HttpURLConnection.HTTP_OK);
			request.getRequestDispatcher(ConstantsWeb.MOBILE_LOGIN_URI).forward(request, response);
			
		} catch (OAuthProblemException e) {
			UtilsWeb.makeResponseInJSON(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST), 
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, e.getDescription(),
					ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		} catch (Exception e) {
			UtilsWeb.makeResponseInJSON(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_INTERNAL_ERROR), 
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "internal server error");
			response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method is called when the resource owner grants the client's
	 * access request. In this method also is generated the authorization 
	 * code and save temporarily in the DB.
	 * 
	 * @param request
	 * @param response
	 */
	private void authorizationConfirm(HttpServletRequest request, HttpServletResponse response) {
		try {
			oauthRequest = new OAuthAuthzRequest(request);
			
			OAuthCodeDTO oauthCodeDTO = new OAuthCodeDTO();
			oauthCodeDTO.setCode(oauthIssuerImpl.authorizationCode());
			
			String userId = oauthRequest.getParam(ConstantsWeb.OAUTH_USER_ID);
			if (userId == null) {
				UtilsWeb.makeResponseInJSON(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED), 
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "Unauthorized",
						ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
				response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
				return;
			}
			
			oauthCodeDTO.setUserEmail(userId);
			
			oauthCodeDTO.setClientId(oauthRequest.getClientId());
			
			managementOAuth.saveAuthorizationCode(oauthCodeDTO);
			
			UtilsWeb.sendValuesInJson(response, OAuth.OAUTH_CODE, oauthCodeDTO.getCode());
			String redirectURI = oauthRequest.getRedirectURI() + "?code=" + oauthCodeDTO.getCode();
			response.sendRedirect(redirectURI);
		} catch (OAuthProblemException e) {
			UtilsWeb.makeResponseInJSON(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST), 
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, e.getDescription(),
					ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		} catch (Exception e) {
			UtilsWeb.makeResponseInJSON(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_INTERNAL_ERROR), 
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "internal server error");
			response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			throw new MaritacaException(e.getMessage());
		}
	}
	
	/**
	 * This method generate an access and refresh token to the third-party application, 
	 * the request HAVE TO be a POST request.
	 * @param request
	 * @param response
	 */
	public void accessToken(HttpServletRequest request, HttpServletResponse response) {
		try {					
			oauthRequest = new OAuthAuthzRequest(request);
			
			String code = oauthRequest.getParam(OAuth.OAUTH_CODE);
			String clientId = oauthRequest.getClientId();
			DataAccessTokenDTO dataDTO = managementOAuth.findOAuthCodeAndClient(code, clientId);
			
			// verify OAuthCode
			OAuthCodeDTO oauthCodeDTO = dataDTO.getOauthCodeDTO();
			if(oauthCodeDTO == null) {
				UtilsWeb.makeResponseInJSON(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_NO_CONTENT), 
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "Invalid authorization code",
						ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
				response.setStatus(HttpURLConnection.HTTP_NO_CONTENT);
				return;
			}

			// Verify OauthClient
			OAuthClientDTO clientDTO = dataDTO.getOauthClientDTO();			
			if(clientDTO == null) {
				UtilsWeb.makeResponseInJSON(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_NO_CONTENT), 
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "client_id not found",
						ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
				response.setStatus(HttpURLConnection.HTTP_NO_CONTENT);
				return;
			}
			
			if (oauthCodeDTO.getClientId().toString().equals(clientDTO.getKey()) &&
					!clientDTO.getClientId().equals(clientId)) {
				UtilsWeb.makeResponseInJSON(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_NO_CONTENT), 
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "Invalid client_id",
						ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
				response.setStatus(HttpURLConnection.HTTP_NO_CONTENT);
				return;
			}
			
			if (!clientDTO.getSecret().equals(oauthRequest.getClientSecret())) {
				UtilsWeb.makeResponseInJSON(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED), 
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "Invalid client_secret",
						ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
				response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
				return;
			}
			
			// If all is correct :) Generate Tokens		
			OAuthTokenDTO tokenDTO = new OAuthTokenDTO();
			tokenDTO.setAccessToken(oauthIssuerImpl.accessToken());
			tokenDTO.setRefreshToken(oauthIssuerImpl.refreshToken());
			tokenDTO.setUserEmail(oauthCodeDTO.getUserEmail());
			tokenDTO.setClientId(clientDTO.getClientId()); 
			
			managementOAuth.saveOAuthToken(tokenDTO);
			
			response.setContentType(ConstantsWeb.APPLICATION_JSON);
			UtilsWeb.sendValuesInJson(response, 
							 OAuth.OAUTH_ACCESS_TOKEN, tokenDTO.getAccessToken(),
							 OAuth.OAUTH_EXPIRES_IN, String.valueOf(tokenDTO.getExpirationDate()),
							 OAuth.OAUTH_REFRESH_TOKEN, tokenDTO.getRefreshToken(),
							 USER_PARAM, dataDTO.getOauthCodeDTO().getUserEmail());
			response.setStatus(HttpURLConnection.HTTP_OK);
		} catch (OAuthProblemException e) {
			UtilsWeb.makeResponseInJSON(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST), 
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, e.getDescription(),
					ConstantsWeb.OAUTH_ERROR_URI, request.getParameter(OAuth.OAUTH_REDIRECT_URI));
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		} catch (Exception e) {
			UtilsWeb.makeResponseInJSON(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_INTERNAL_ERROR), 
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "internal server error");
			response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			throw new MaritacaException(e.getMessage());
		}
	}	
}
