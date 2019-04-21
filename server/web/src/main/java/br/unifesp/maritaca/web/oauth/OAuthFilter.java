package br.unifesp.maritaca.web.oauth;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.message.types.ParameterStyle;
import net.smartam.leeloo.rs.request.OAuthAccessResourceRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.unifesp.maritaca.business.oauth.ManagementOAuth;
import br.unifesp.maritaca.business.oauth.dto.OAuthTokenDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.web.util.ConstantsWeb;
import br.unifesp.maritaca.web.util.UtilsWeb;

@Component("oauthFilter")
public class OAuthFilter implements Filter {
	
	private final Logger logger = Logger.getLogger(OAuthFilter.class);

	@Autowired private ManagementOAuth managementOAuth;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("Loading OAuth Filter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		_doFilter((HttpServletRequest)request, (HttpServletResponse)response, filterChain);		
	}

	protected void _doFilter(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			logger.info("Filtering " + request.getMethod() + " " + request.getRequestURL().toString());
			
			OAuthAccessResourceRequest oauthRequest = new 
					OAuthAccessResourceRequest(request, ParameterStyle.QUERY);

			String accessToken = oauthRequest.getAccessToken();

			OAuthTokenDTO oauthTokenDTO = managementOAuth.findOAuthToken(accessToken);
			logger.info("doFilter");
			logger.info("accessToken : " + accessToken);
			if(oauthTokenDTO == null) {
				logger.info("oauthTokenDTO === null");
				UtilsWeb.sendValuesInJson(response, 
						ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED),
						ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "Invalid oauth_token");
				
				response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
				return;
			}
			logger.info("oauthTokenDTO.getUserEmail() : " + oauthTokenDTO.getUserEmail());
			request.setAttribute(ConstantsBusiness.WS_USER_KEY, oauthTokenDTO.getUserEmail());
			
			filterChain.doFilter(request, response);
		} catch (OAuthProblemException e) {
			logger.info("OAuthProblemException : " + e.getMessage());
			UtilsWeb.makeResponseInJSON(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST), 
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, e.getDescription());
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		} catch (Exception e) {
			logger.info("Exception : " + e.getMessage());
			UtilsWeb.makeResponseInJSON(response, 
					ConstantsWeb.OAUTH_ERROR, String.valueOf(HttpURLConnection.HTTP_INTERNAL_ERROR), 
					ConstantsWeb.OAUTH_ERROR_DESCRIPTION, "internal server error");
			response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			
		}
		
	}
	
	@Override
	public void destroy() {
	}

}
