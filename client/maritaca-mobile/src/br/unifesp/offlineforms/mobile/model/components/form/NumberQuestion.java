package br.unifesp.offlineforms.mobile.model.components.form;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.model.Comparison;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;

@Root(name = "number")
public class NumberQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private static final int FIELD_SIZE = 80;

	@Attribute(required = false)
	private Integer min;

	@Attribute(required = false)
	private Integer max;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.NUMBER;
	}

	@Override
	public Integer getNext() {
		if(getComparisons() == null || getComparisons().isEmpty())
			return next;

		for(Comparison comp : getComparisons()){
			Integer value = this.getValue();
			if (comp.evaluate(value)) {
				return comp.getGoTo();
			}
		}
		return next;
	}

	@Override
	public Integer getValue(){
		try {
			return (value != null && !"".equals(value)) ? Integer.parseInt(value.toString()) : null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public View getLayout(ControllerActivity activity) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 0);
		EditText field = new EditText(activity);
		field.setMinWidth(FIELD_SIZE);
		field.setInputType(InputType.TYPE_CLASS_NUMBER);
		field.requestFocus();
		String value = getValue() != null ? getValue().toString() : getDefault();
		if(value == null || "".equals(value)){
			field.setText(value);
		} else {
			field.append(value);
		}
		field.setLayoutParams(params);
		return field;
	}

	@Override
	public boolean validate() {
		Integer val = getValue();
		boolean flag = false;
		if(val != null) {
			if (min == null && max == null) {
				flag = true;
			}
			if (min != null && max != null) {
				flag = (val >= min) && (val <= max);
			}
			if (min != null) {
				flag = (val >= min);
			}
			if (max != null) {
				flag = (val <= max);
			}
		}
		if(!required)
			flag = true;
		return flag;
	}

	@Override
	public void save(View answer){
		value = ((TextView) answer).getText();
	}
}