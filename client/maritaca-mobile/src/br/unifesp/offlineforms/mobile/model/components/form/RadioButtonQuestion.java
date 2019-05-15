package br.unifesp.offlineforms.mobile.model.components.form;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.model.Comparison;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;
import br.unifesp.offlineforms.mobile.model.components.util.Option;

@Root(name = "radio")
public class RadioButtonQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private RadioGroup radioGroup;
	private int idWrapContent = RadioGroup.LayoutParams.WRAP_CONTENT;

	@ElementListUnion(value = { @ElementList(entry = "option", inline = true, type = Option.class, required = false) })
	private List<Option> options;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.RADIOBOX;
	}

	@Override
	public Integer getNext() {
		int size = 0;
		String value = this.getValue();
		if (getComparisons() != null && getComparisons().size() > 0)
			size = getComparisons().size();

		if (getComparisons() == null || size < 1)
			return next;
		
		String newValue = "";
			if(value != null && !"".equals(value)){
				try {
					JSONObject jsonObj = new JSONObject(value);
					Iterator it = jsonObj.keys();
					while (it.hasNext()) {
						String key = (String) it.next();
						newValue = jsonObj.getString(key);
					}				
				} catch (JSONException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		
		for (Comparison comp : getComparisons()) {
			if (comp.evaluate(newValue)) {
				return comp.getGoTo();
			}
		}
		return next;
	}

	@Override
	public String getValue() {
		return value != null ? value.toString() : "";
	}

	@Override
	public View getLayout(ControllerActivity activity) {
		radioGroup = new RadioGroup(activity);
		//Set Ids
		setOptionIds();
				
		for (int i = options.size() - 1; i >= 0; i--) {
			RadioButton radioButton = new RadioButton(activity);
			radioButton.setId(options.get(i).getId());
			radioButton.setText(options.get(i).getText());
			if(value != null){
				try {
					String jsonVal = "";
					JSONObject jsonObj = new JSONObject(value.toString());
					Iterator it = jsonObj.keys();
					while (it.hasNext()) {
						String key = (String) it.next();
						jsonVal = jsonObj.getString(key);						
					}
					//Limpando a primeira questão
					/*
					if (radioButton.getText().equals(jsonVal)) {
						radioButton.setChecked(true);
					}
					*/
				} catch (JSONException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
			else{
				if(options.get(i).isChecked()) {
					radioButton.setChecked(true);
				}			
			}
			//
			LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
					idWrapContent, idWrapContent);
			layoutParams.setMargins(20, 0, 0, 0);
			radioGroup.addView(radioButton, 0, layoutParams);
		}
		return radioGroup;
	}

	@Override
	public boolean validate() {
		return required ? (!getValue().equals("") ? true : false) : true;
	}

	@Override
	public void save(View answer) {
		try {
			RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
			if (radioButton != null && radioButton.isChecked()) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(String.valueOf(radioGroup.getCheckedRadioButtonId()), radioButton.getText());
				value = jsonObj.toString();
			}
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage());
		}	
	}

	public void setOptionIds() {
		int i = 0;
		for(Option next : options) {
			next.setId(i);
			i++;
		}
	}

	public String getDefault() {
		if(options != null){
			try {
				for(Option o : options) {
					if(o.isChecked()){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put(String.valueOf(o.getId()), o.getText());				
						return jsonObj.toString();
					}
				}
			} catch (JSONException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return _default;
	}	
}