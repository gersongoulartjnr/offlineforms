package br.unifesp.maritaca.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.web.util.ConstantsWeb;

public class MaritacaFilter implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		if(request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
		
			//map js, css, images, properties, ... 
			HttpSession session = httpRequest.getSession(true); //false?
			UserDTO mUser = (UserDTO)session.getAttribute(ConstantsWeb.CURRENT_USER);
			if(mUser == null) {
				((HttpServletResponse)response).sendRedirect(httpRequest.getContextPath());								
			}
			else {
				chain.doFilter(request, response);
			}			
		}
		else {
			throw new RuntimeException("request not allowed");
		}		
	}

	@Override
	public void destroy() {
	}
}