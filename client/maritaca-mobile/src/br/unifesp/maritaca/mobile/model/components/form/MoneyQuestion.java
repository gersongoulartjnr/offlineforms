package br.unifesp.maritaca.mobile.model.components.form;

import java.math.BigDecimal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.text.InputType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.unifesp.maritaca.mobile.activities.ControllerActivity;
import br.unifesp.maritaca.mobile.model.Comparison;
import br.unifesp.maritaca.mobile.model.Question;
import br.unifesp.maritaca.mobile.model.components.ComponentType;

@Root(name = "money")
public class MoneyQuestion extends Question {
	
	private static final long serialVersionUID = 1L;
	private static final int FIELD_SIZE = 80;
	
	@Attribute(required = false)
	private BigDecimal min;

	@Attribute(required = false)
	private BigDecimal max;
	
	@Element(required = false)
	private String currency;
	
	private EditText field;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.MONEY;
	}
	
	@Override
	public Integer getNext() {
		if (getComparisons() == null || getComparisons().isEmpty())
			return next;

		for (Comparison comp : getComparisons()) {
			BigDecimal value = this.getValue();
			if (comp.evaluate(value)) {
				return comp.getGoTo();
			}
		}
		return next;
	}

	@Override
	public BigDecimal getValue() {
		try {
			return (value != null && !"".equals(value)) ? new BigDecimal(
					value.toString()) : null;
		} catch (Exception e) {
			return null;
		}
	}

	//TODO
	@Override
	public View getLayout(ControllerActivity activity) {
		TableLayout tbl = new TableLayout(activity);
		TableRow tblRow = new TableRow(activity);
		field = new EditText(activity);
		field.setMinWidth(FIELD_SIZE);
		field.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		field.requestFocus();
		String value = getValue() != null ? getValue().toString() : getDefault();
		if(value == null || "".equals(value)){
			field.setText(value);
		} else {
			field.append(value);
		}
		TextView txtView = new TextView(activity);
		txtView.setText(currency);
		txtView.setPadding(20, 0, 0, 0);
		tblRow.addView(txtView);
		tblRow.addView(field);
		
		tbl.addView(tblRow);		
		return tbl;
	}

	@Override
	public boolean validate() {
		BigDecimal val = getValue();
		boolean flag = false;
		if (val != null) {
			if (min == null && max == null) {
				flag = true;
			}
			if (min != null && max != null) {
				flag = (val.compareTo(min) >= 0) && (val.compareTo(max) <= 0);
			}
			if (min != null) {
				flag = (val.compareTo(min) >= 0);
			}
			if (max != null) {
				flag = (val.compareTo(max) <= 0);
			}
		}
		if (!required)
			flag = true;
		return flag;
	}

	@Override
	public void save(View answer) {
		if(field != null){
			value = field.getText();
		}
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}