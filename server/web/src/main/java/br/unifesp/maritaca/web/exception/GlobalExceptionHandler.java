package br.unifesp.maritaca.web.exception;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

public class GlobalExceptionHandler extends AbstractHandlerExceptionResolver {

	public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";

	@Override
	protected ModelAndView doResolveException(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3) {
		// TODO Auto-generated method stub
		return null;
	}
	
}