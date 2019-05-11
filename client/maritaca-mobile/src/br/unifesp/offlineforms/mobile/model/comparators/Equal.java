package br.unifesp.offlineforms.mobile.model.comparators;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.simpleframework.xml.Root;

import br.unifesp.offlineforms.mobile.model.Comparison;

@Root(name = "equal")
public class Equal extends Comparison implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String EQUAL = "equal";

	public String getType() {
		return EQUAL;
	}

	@Override
	public boolean evaluate(String value) {
		if(value == null)
			return false;
		if (super.value.compareTo(value) == 0)
			return true;
		return false;
	}

	@Override
	public boolean evaluate(BigDecimal value) {
		if(value == null)
			return false;
		return evaluate(value.toString());
	}

	@Override
	public boolean evaluate(Float value) {
		if(value == null)
			return false;
		return evaluate(value.toString());
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
		return evaluate(value.toString());
	}
}