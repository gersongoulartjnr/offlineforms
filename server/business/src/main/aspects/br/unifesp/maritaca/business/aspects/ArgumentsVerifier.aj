package br.unifesp.maritaca.business.aspects;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.exception.ParameterException;

/**
 * Aspect used to check if one of the arguments passed to public
 * methods of EJB classes are null.
 * 
 * @author tiagobarabasz
 *
 */
public aspect ArgumentsVerifier {
			
	/**
	 * This pointcut matches all calls to public method of classes
	 * annoted with @Service, wich in our project correspond to
	 * the bean classes. It also exposes all the arguments passed in
	 * the function call.
	 */	
	pointcut verifiedArgumentMethod():
		within(@Service *) &&
		execution(public * *(..));
	
	before(): verifiedArgumentMethod(){		
		for(Object obj : thisJoinPoint.getArgs()){
			if(obj == null){
				throw new ParameterException("Parameter to "+
										 thisJoinPoint.getSignature().toString()+
										 " can't be null");
			}
		}
	}
}
