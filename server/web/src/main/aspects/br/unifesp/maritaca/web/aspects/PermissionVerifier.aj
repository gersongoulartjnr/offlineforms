package br.unifesp.maritaca.web.aspects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import br.unifesp.maritaca.web.controller.AbstractController;

/**
 * Aspect used to check permissions in the application.
 * @author tiagobarabasz
 *
 */
public aspect PermissionVerifier {
	
	private static Log log = LogFactory.getLog(PermissionVerifier.class);
	
	pointcut verifiedMethods():
		within(@Controller *) &&
		!execution(* br.unifesp.maritaca.web.controller.general.*.*(..)) &&
		!execution(* br.unifesp.maritaca.web.controller.authentication.*.*(..)) &&
		execution(@RequestMapping public String *(..));
	
	String around(): verifiedMethods(){
		log.debug("Verifying permission for: "+thisJoinPoint.getSignature().toLongString());
		
		Object thisObj = thisJoinPoint.getThis();		
		if(thisObj instanceof AbstractController){
			AbstractController controller = (AbstractController) thisJoinPoint.getThis();
			HttpServletRequest request    = extractRequest((Object[])thisJoinPoint.getArgs());			
			if(!controller.isAllowed(request)) {
				log.info("User not logged in. Redirecting to home page.");
				return AbstractController.REDIRECT_DEFAULT_HTML_VIEW;
			}
		}
		log.debug("Permission granted.");
		return proceed();
	}		

	private HttpServletRequest extractRequest(Object[] args) {
		for(Object arg : args){
			if(arg instanceof HttpServletRequest){
				return (HttpServletRequest) arg;
			}
		}
		return null;
	}
}
