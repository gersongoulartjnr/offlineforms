package br.unifesp.offlineforms.mobile.model.components.form;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.activities.R;
import br.unifesp.offlineforms.mobile.model.Comparison;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;
import br.unifesp.offlineforms.mobile.model.components.util.Option;

@Root(name = "combobox")
public class ComboBoxQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private static String CHOOSE_ONE = "";
	private Spinner spinner;

	@ElementListUnion(value = { @ElementList(entry = "option", inline = true, type = Option.class, required = false) })
	private List<Option> options;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.COMBOBOX;
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
		int pos = 0;
		CHOOSE_ONE = activity.getString(R.string.label_combo_chooseone);
		// Main layout
		LinearLayout outputLayout = new LinearLayout(activity);
		outputLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 0);
		// Combo
		spinner = new Spinner(activity);

		if (!CHOOSE_ONE.equals(options.get(0).getText())) {
			options.add(0, new Option(-1, CHOOSE_ONE, false, CHOOSE_ONE));
		}
		//Set Ids
		setOptionIds();
		
		List<String> optionsLst = new ArrayList<String>();
		for (Option option : options) {
			optionsLst.add(option.getText());
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
					if (option.getText().equals(jsonVal)) {
						pos = option.getId() + 1;
					}
					*/
				} catch (JSONException e) {
					throw new RuntimeException(e.getMessage());
				}
			} else {
				if(option.isChecked()){
					pos = option.getId() + 1;
				}				
			}
		}
		// TODO: Create a custom adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				R.layout.comboboxblack, optionsLst);
		spinner.setAdapter(adapter);
		spinner.setSelection(pos);
		outputLayout.addView(spinner, params);
		return outputLayout;
	}

	@Override
	public boolean validate() {
		return required ? (!getValue().equals("") ? true : false) : true;
	}

	@Override
	public void save(View answer) {
		try {
			if (spinner.getSelectedItem() != null) {
				String _value = spinner.getSelectedItem().toString();
				JSONObject jsonObj = new JSONObject();
				if (!CHOOSE_ONE.equals(_value)) {
					jsonObj.put(String.valueOf((spinner.getSelectedItemId() -1)), spinner.getSelectedItem().toString());
				}
				value = jsonObj.toString();
			}
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void setOptionIds(){
		int i = 0;
		for (Option o : options) {
			if(CHOOSE_ONE.equals(o.getText())){
				o.setId(-1);
			} else {
				o.setId(i);
				i++;
			}
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