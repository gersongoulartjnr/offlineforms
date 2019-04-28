package br.unifesp.maritaca.mobile.model.components.form;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import br.unifesp.maritaca.mobile.activities.ControllerActivity;
import br.unifesp.maritaca.mobile.model.Comparison;
import br.unifesp.maritaca.mobile.model.Question;
import br.unifesp.maritaca.mobile.model.components.ComponentType;
import br.unifesp.maritaca.mobile.util.Constants;

@Root(name = "text")
public class TextQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private EditText field;

	@Element(required = false)
	private Integer size;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.TEXT;
	}

	@Override
	public View getLayout(final ControllerActivity activity) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
											   LayoutParams.WRAP_CONTENT);
		
		params.setMargins(20,0,20,0);
		LinearLayout outputLayout = new LinearLayout(activity);
		
		LinearLayout editText = new LinearLayout(activity);
		outputLayout.addView(editText);
			field = new EditText(activity);
		    field.setTextColor(Color.BLACK);
			//field.setLayoutParams(params);
			field.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | 0x00020001);
			field.requestFocus();
			//Limpando a primeira questão
			/*String value = getValue() != null ? getValue().toString() : getDefault();
			field.setText(value);*/
			editText.addView(field);			
		
		PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        /*gravar texto por voz*/
		/*if(activities.size() != 0){
			LinearLayout recogVoice = new LinearLayout(activity);
			outputLayout.addView(recogVoice);
				ImageButton imgBtn = new ImageButton(activity);
	    		imgBtn.setImageResource(android.R.drawable.ic_btn_speak_now); 
	    		imgBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						startVoiceRecognitionActivity(activity);
					}
				});		
			recogVoice.addView(imgBtn);
        }*/
        
        if(activity.getSuggestionsList() != null){
        	LinearLayout suggestions = new LinearLayout(activity);
        	outputLayout.addView(suggestions);
        	ListView lView = activity.getSuggestionsList();        	
          	        	
        	activity.getSuggestionsList().setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
					String newValue = (String)parent.getItemAtPosition(position);
					field.setText(newValue);
					setValue(newValue);
					activity.setSuggestionsList(null);
				}
			});      
        	activity.setSuggestionsList(null);
        	suggestions.addView(lView);        	
        }		
		return outputLayout;
		
	}
	
	private void startVoiceRecognitionActivity(ControllerActivity activity){
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        activity.startActivityForResult(intent, Constants.VOICE_RECOGNITION_REQUEST);
	}

	@Override
	public boolean validate() {
		if (required) {
			if (!"".equals(getValue()))
				return true;
			return false;
		}
		return true;
	}

	@Override
	public void save(View answer) {
		if(field != null){
			value = field.getText();			
		}
	}

	@Override
	public String getValue() {
		return (value != null && !"".equals(value)) ? value.toString() : null;
	}

	@Override
	public Integer getNext() {
		if (getComparisons() == null || getComparisons().size() < 1)
			return next;

		for (Comparison comp : getComparisons()) {
			String value = this.getValue();
			if (comp.evaluate(value)) {
				return comp.getGoTo();
			}
		}
		return next;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}