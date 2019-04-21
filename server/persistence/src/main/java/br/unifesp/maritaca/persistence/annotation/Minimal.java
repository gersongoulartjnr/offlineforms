package br.unifesp.maritaca.persistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to identify if a column is or not minimal. 
 * Also if the column will be or not an index (secondary index) 
 * 
 * @author 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Minimal {
	boolean indexed() default false;
	boolean ttl() default false;
}
