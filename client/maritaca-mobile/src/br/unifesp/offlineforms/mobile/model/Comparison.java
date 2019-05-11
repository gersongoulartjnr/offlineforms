package br.unifesp.offlineforms.mobile.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.simpleframework.xml.Attribute;

/**
 * 
 * @author Bruno G. Santos
 * @version  1.0.1
 *
 */

public abstract class Comparison implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(name = "goto")
	protected Integer goTo;

	@Attribute(name = "value")
	protected String value;

	public Integer getGoTo() {
		return goTo;
	}

	public void setGoTo(Integer goTo) {
		this.goTo = goTo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public abstract boolean evaluate(String value);
	
	public abstract boolean evaluate(Integer value);
	
	public abstract boolean evaluate(BigDecimal value);
	
	public abstract boolean evaluate(Float value);
	
	public abstract boolean evaluate(Date value);
}