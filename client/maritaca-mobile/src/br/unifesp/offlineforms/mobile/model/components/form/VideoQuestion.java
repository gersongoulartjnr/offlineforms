package br.unifesp.offlineforms.mobile.model.components.form;

import java.io.File;

import org.simpleframework.xml.Root;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.activities.R;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;
import br.unifesp.offlineforms.mobile.util.Constants;

/**
 * 
 * @author Walkirya Heto Silva
 * @version 0.1.10
 * 
 */

@Root(name = "video")
public class VideoQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private File video;

	private Button captureVideoButton;
	private Button playVideoButton;

	private VideoView videoView;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.VIDEO;
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
		//Margin params
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 20, 0);

		// Main layout
		LinearLayout outputLayout = new LinearLayout(activity);
		outputLayout.setOrientation(LinearLayout.VERTICAL);
		// Buttons Layout
		LinearLayout buttonsLayout = new LinearLayout(activity);
		outputLayout.addView(buttonsLayout, params);
		//VideoView Layout
		LinearLayout videoViewLayout = new LinearLayout(activity);
		// Buttons configuration
		captureVideoButton = new Button(activity);
		captureVideoButton.setText(R.string.button_start_video);
		
		captureVideoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startCameraActivity(activity);
			}
		});
		buttonsLayout.addView(captureVideoButton);

		video = new File(getValue());
		if (video != null && video.exists()) {
			if(video.length() <= Constants.MAX_VIDEO_SIZE){			
				// VideoView configuration
				videoView = new VideoView(activity);
				videoView.setPadding(30, 20, 30, 20);
				videoViewLayout.addView(videoView);
				outputLayout.addView(videoViewLayout);
				playVideoButton = new Button(activity);
				playVideoButton.setText(R.string.button_play_video);
				playVideoButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						videoView.setVideoURI(Uri.fromFile(video));
						videoView.start();
					}
				});
				LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		        btnParams.setMargins(20, 0, 20, 0);
				buttonsLayout.addView(playVideoButton, btnParams);
			}
			else{
				video = null;
				value = null;
				Toast.makeText(activity, activity.getString(R.string.lbl_file_size_error), Toast.LENGTH_LONG).show();
			}
		}

		return outputLayout;
	}

	private void startCameraActivity(ControllerActivity activity) {
		Intent intent = new Intent(	android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		activity.startActivityForResult(intent, Constants.VIDEO_REQUEST);
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
		if (video != null && video.exists()) {
			value = video.getAbsolutePath();
		} else
			value = null;
	}
}