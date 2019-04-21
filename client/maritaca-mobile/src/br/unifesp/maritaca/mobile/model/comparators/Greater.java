package br.unifesp.maritaca.mobile.model.comparators;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.simpleframework.xml.Root;

import br.unifesp.maritaca.mobile.model.Comparison;

@Root(name="greater")
public class Greater extends Comparison implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String GREATER = "greater";

	public String getType() {
		return GREATER;
	}

	@Override
	public boolean evaluate(String value) {		
		if(value == null)
			return false;
		if (super.value.compareTo(value) > 0)
			return true;
		return false;
	}

	@Override
	public boolean evaluate(BigDecimal value) {
		if(value == null)
			return false;
		BigDecimal bgDefault = new BigDecimal(super.value);
		if(value.compareTo(bgDefault) > 0)
			return true;
		return false;
	}

	@Override
	public boolean evaluate(Float value) {
		if(value == null)
			return false;
		if (value > Float.parseFloat(super.value))
			return true;
		return false;
	}

	@Override
	public boolean evaluate(Date value) {
		if(value == null)
			return false;
		return evaluate(value.toString());
	}

	@Override
	public boolean evaluate(Integer value) {
		if(value == null)
			return false;
		if (value > Integer.parseInt(super.value))
			return true;
		return false;
	}
}