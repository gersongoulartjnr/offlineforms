package br.unifesp.offlineforms.mobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaSetting;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.offlineforms.mobile.exception.MaritacaExceptionHandler;
import br.unifesp.offlineforms.mobile.util.Constants;

/**
 * 
 * @author alvarohenry and Leonardo Sueoka
 * @version 1.1.0
 * 
 */
public class SettingsActivity extends Activity {

	private static final int MAX_SYNC_INTERVAL_TIME = 60;
	private MaritacaUser mUser;
    private MaritacaHelper sqliteHelper;

	private ToggleButton toggleBtnSync;
	private SeekBar seekBarSync;
	private TextView textValueSync;
	private Button saveBtn;
	private RadioButton radioCollectData;
	private RadioButton radioSendData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(SettingsActivity.this));
		this.mUser = (MaritacaUser) getIntent().getSerializableExtra(Constants.USER_DATA);
		initOptions();
	}
	
	private void initOptions() {
		sqliteHelper = new MaritacaHelper(this);
		setContentView(R.layout.settings);
		
		seekBarSync 	= (SeekBar) findViewById(R.id.seekBarSync);
		textValueSync 	= (TextView) findViewById(R.id.textValueSync);
		toggleBtnSync 	= (ToggleButton) findViewById(R.id.toggleBtnSync);
		radioCollectData= (RadioButton) findViewById(R.id.radioCollectingList);
		radioSendData	= (RadioButton) findViewById(R.id.radioSendingAuto);
		saveBtn 		= (Button) findViewById(R.id.btnSaveSettings);
		
		// init SYNCHRONIZATION INTERVAL
		this.setSyncIntervalActions();
		
		MaritacaSetting ms = sqliteHelper.getMaritacaSetting(mUser.getId());
		seekBarSync.setMax(MAX_SYNC_INTERVAL_TIME);
		seekBarSync.setProgress(ms.getSyncInterval());	
		toggleBtnSync.setChecked(ms.isSyncActive());
		radioCollectData.setChecked(ms.isCollectaDataList());
		radioSendData.setChecked(ms.isSendingAuto());
		seekBarSync.setEnabled(toggleBtnSync.isChecked());
		
		saveBtn.setOnClickListener(new Button.OnClickListener() {
			boolean saveSuccessful;
			@Override		

			public void onClick(View v) {
				saveSuccessful = sqliteHelper.saveSettings(toggleBtnSync.isChecked(), 
														   seekBarSync.getProgress(),
														   radioCollectData.isChecked(),
														   radioSendData.isChecked(),
														   mUser.getId());
				if(saveSuccessful){
					Toast.makeText(getApplicationContext(), R.string.msg_settings_saved, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.msg_settings_nosaved, Toast.LENGTH_SHORT).show();
				}
				goToMenuActivity();
			}
		});
	}
	
	private void setSyncIntervalActions() {
		toggleBtnSync.setOnClickListener(new ToggleButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), toggleBtnSync.getText(), Toast.LENGTH_SHORT).show();
				if (toggleBtnSync.isChecked()){
					seekBarSync.setEnabled(true);
					textValueSync.setEnabled(true);					
				}
				else{
					seekBarSync.setEnabled(false);
					textValueSync.setEnabled(false);
				} 				
			}
		});
		
		seekBarSync.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(progress == 1) {
					textValueSync.setText(progress + " " + getString(R.string.msg_settings_sync_minute));
				} else {
					textValueSync.setText(progress + " " + getString(R.string.msg_settings_sync_minutes));
				}
			}	
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goToMenuActivity();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void goToMenuActivity() {
		Intent intent = new Intent(this, MenuLoadFormActivity.class);
		intent.putExtra(Constants.USER_DATA, mUser);
		startActivity(intent);
		finish();
	}
}