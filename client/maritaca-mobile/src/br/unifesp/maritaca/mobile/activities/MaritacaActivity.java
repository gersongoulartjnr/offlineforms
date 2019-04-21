package br.unifesp.maritaca.mobile.activities;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.SharedPreferences;

import br.unifesp.maritaca.mobile.dataaccess.MaritacaHelper;
import br.unifesp.maritaca.mobile.util.Constants;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

public class MaritacaActivity extends SherlockActivity {

	private MaritacaHelper sqliteHelper;
	private int splashTime = 4000;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		sqliteHelper = new MaritacaHelper(this);

		setContentView(R.layout.splash_layout);

		final MaritacaActivity splashActivity = this;

		SharedPreferences settings = this.getSharedPreferences("appInfo", 0);
		boolean firstTime = settings.getBoolean("first_time", true);;
		if(firstTime==true)//if running for first time
		//Splash will load for first time
		{
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("first_time", false);
			editor.commit();

			startAnimations();
			new Thread(new Runnable() {
				public void run() {
					try {
						synchronized (this) {
							wait(splashTime);
						}
					} catch (InterruptedException e) {
						throw new RuntimeException(e.getMessage());
					} finally {
						finish();

						String path = Constants.PATH_FORM;
						File myFile = new File(path);
						if (myFile.exists()) {
							Intent intent = new Intent(splashActivity, MenuActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(splashActivity, MenuLoadFormActivity.class);
							startActivity(intent);
						}

						sqliteHelper.onClose();

					}
				}
			}).start();
		}
		else
		{
			String path = Constants.PATH_FORM;
			File myFile = new File(path);
			if (myFile.exists()) {
				Intent intent = new Intent(splashActivity, MenuActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(splashActivity, MenuLoadFormActivity.class);
				startActivity(intent);
			}
		}



		/*startAnimations();
		final MaritacaActivity splashActivity = this;
		new Thread(new Runnable() {
			public void run() {
				try {
					synchronized (this) {
						wait(splashTime);
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage());
				} finally {
					finish();

					String path = Constants.PATH_FORM;
					File myFile = new File(path);
					if (myFile.exists()) {
						Intent intent = new Intent(splashActivity, MenuActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(splashActivity, MenuLoadFormActivity.class);
						startActivity(intent);
					}

					sqliteHelper.onClose();


				}
			}
		}).start();*/

	}




	@Override
	protected void onPause() {
		if (sqliteHelper != null) {
			sqliteHelper.onClose();
			finish();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (sqliteHelper != null) {
			sqliteHelper.onClose();
		}
		super.onDestroy();
	}

	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	private void startAnimations() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim.reset();
		LinearLayout layout = (LinearLayout) findViewById(R.id.splash_body);
		layout.clearAnimation();
		layout.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.translate);
		anim.reset();
		ImageView imageView = (ImageView) findViewById(R.id.logo);
		imageView.clearAnimation();
		imageView.startAnimation(anim);
	}

}