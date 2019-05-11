package br.unifesp.offlineforms.mobile.model.components.form;

import java.io.File;

import org.simpleframework.xml.Root;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.activities.R;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;
import br.unifesp.offlineforms.mobile.util.Constants;

/**
 * 
 * @author Walkirya Heto Silva
 * @version 0.1.7
 *
 */

@Root(name = "audio")
public class AudioQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private File audioFile;
	
	@Override
	public ComponentType getComponentType() {
		return ComponentType.AUDIO;
	}

	@Override
	public Integer getNext() {
		return next;
	}

	@Override
	public String getValue() {
		return value != null ? value.toString() : "";
	}

	@Override
	public View getLayout(final ControllerActivity activity) {
		RelativeLayout layout = new RelativeLayout(activity);
        TableLayout tableLayout = new TableLayout(activity);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 10, 0);
        TableRow firstRow = new TableRow(activity);
        Button btnRec = new Button(activity);
        btnRec.setText(R.string.button_record_audio);
        firstRow.addView(btnRec);
        btnRec.setOnClickListener(new OnClickListener(){	        	
        	public void onClick(View v) {
				startAudioActivity(activity);
			}
        });
        
        audioFile = new File(getValue());        
        if (audioFile != null && audioFile.exists()){ 
	        Button btnPlayRec = new Button(activity);
	        btnPlayRec.setText(R.string.button_play_audio);
	        firstRow.addView(btnPlayRec);
	        btnPlayRec.setOnClickListener(new OnClickListener(){
	    		public void onClick(View v) {
	    			playAudioActivity(activity);
	    		}
	    	});
        }        
        tableLayout.addView(firstRow, params);
        layout.addView(tableLayout);
		return layout;        
	}
	
	private void startAudioActivity(ControllerActivity activity){
    	Intent recIntent = new Intent(android.provider.MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		activity.startActivityForResult(recIntent, Constants.AUDIO_REQUEST);		
	}

	protected void playAudioActivity(ControllerActivity activity){
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(audioFile), "audio/3gpp");
		activity.startActivity(intent);
	}

	@Override
	public boolean validate() {
		if(required) {
			if(!"".equals(getValue()))
				return true;
			return false;
		}
		return true;
	}

	@Override
	public void save(View answer) {
		String audioPath = getValue();
		if(audioPath != null) {
            value = audioPath;
        }
        else
            value = null;
	}
}