package br.unifesp.offlineforms.mobile.model.components.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.activities.R;
import br.unifesp.offlineforms.mobile.model.Comparison;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.adaptors.CheckBoxAdapter;
import br.unifesp.offlineforms.mobile.model.adaptors.OptionViewHolder;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;
import br.unifesp.offlineforms.mobile.model.components.util.Option;

@Root(name = "checkbox")
public class CheckBoxQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private ListView listView;
	private CheckBox checkBox;
	private ArrayAdapter<Option> listAdapter;

	@ElementListUnion(value = { @ElementList(entry = "option", inline = true, type = Option.class, required = false) })
	private List<Option> options;

	private Option option;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.CHECKBOX;
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
					//It is valid only for one value
					if(jsonObj.length() == 1){
						Iterator it = jsonObj.keys();
						while (it.hasNext()) {
							String key = (String) it.next();
							newValue = jsonObj.getString(key);
						}
					} else {
						return next;
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
		listView = new ListView(activity);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View item,
					int position, long id) {
				Option option = listAdapter.getItem(position);
				option.toggleChecked();
				OptionViewHolder viewHolder = (OptionViewHolder) item.getTag();
				viewHolder.getCheckBox().setChecked(option.isChecked());
				viewHolder.getCheckBox().setTextColor(Color.BLACK);
			}
		});
		//Set Ids
		setOptionIds();
		
		List<Option> optionList = new ArrayList<Option>();
	    optionList.addAll(options);	    
	    listAdapter = new CheckBoxAdapter(activity, optionList);
	    listView.setAdapter(listAdapter);
	    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    listView.setPadding(20, 0, 20, 0);
		return listView;
	}

	@Override
	public boolean validate() {
		return required ? (!getValue().equals("") ? true : false) : true;
	}

	@Override
	public void save(View answer) {
		try {
			value = "";
			int firstPosition = listView.getFirstVisiblePosition();
			int size = listView.getCount();
			JSONObject jsonObj = new JSONObject();
			for (int i = firstPosition; i < size; i++) {
				View view = listView.getChildAt(i);
				if (view != null) {
					checkBox = (CheckBox) view.findViewById(R.id.rowCheckBox);
					if (checkBox.isChecked()) {
						option = (Option) listAdapter.getItem(i);
						jsonObj.put(String.valueOf(option.getId()),
								option.getText());
					}
				}
			}
			value = (String) jsonObj.toString();
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
				JSONObject jsonObj = new JSONObject();
				for(Option o : options) {
					if(o.isChecked()){
						jsonObj.put(String.valueOf(o.getId()), o.getText());				
					}
				}
				return jsonObj.toString();
			} catch (JSONException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return _default;
	}
}