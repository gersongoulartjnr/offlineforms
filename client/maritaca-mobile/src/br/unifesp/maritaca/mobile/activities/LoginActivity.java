package br.unifesp.maritaca.mobile.activities;

import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.common.OAuth;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.ResponseType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import br.unifesp.maritaca.mobile.dataaccess.MaritacaHelper;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.maritaca.mobile.thread.CheckServerUpThread;
import br.unifesp.maritaca.mobile.util.Constants;

public class LoginActivity extends Activity {

	private MaritacaHelper sqliteHelper;
	private MaritacaUser mUser;
	
	private static Context context;
	private WebView webView;
	
	private CheckServerUpThread isServerUp;
	
	/**
	 * Performs the initial oauth request opening the embedded
	 * browser in the oauth login page.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isServerUp = new CheckServerUpThread();
		context = getApplicationContext();		
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);	
		setContentView(R.layout.login);
		try {
			if(isServerUp.execute().get()) {
				OAuthClientRequest request = OAuthClientRequest
						.authorizationLocation(Constants.URI_AUTH_REQUEST)
						.setClientId(Constants.OAUTH_CLIENT_ID)
						.setRedirectURI(Constants.OAUTH_REDIRECT_URI)
						.setParameter(OAuth.OAUTH_CLIENT_SECRET, Constants.OAUTH_CLIENT_SECRET)
						.setParameter(Constants.FORM_NAME_ID, Constants.FORM_ID)
						.setResponseType(ResponseType.CODE.toString())
						.buildQueryMessage();
				openBrowserInUrl(request.getLocationUri());
			}
			else {
				showDialog(Constants.DIALOG_OFFLINE);
			}
		} catch (OAuthSystemException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			showDialog(Constants.DIALOG_OFFLINE);
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
	
	@Override
	protected void onPause() {
		if(sqliteHelper != null) {
			sqliteHelper.onClose();
			finish();
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		if(sqliteHelper != null) {
			sqliteHelper.onClose();
		}
		super.onDestroy();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
    	switch(id) {
    		case Constants.DIALOG_OFFLINE:
    			dialog = showDialogWhenMaritacaIsOffline();
    			break;
    	}    
    	return dialog;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(webView != null)
			webView.saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(webView != null)
			webView.restoreState(savedInstanceState);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private Dialog showDialogWhenMaritacaIsOffline() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.label_connection_error);
		dialog.setIcon(R.drawable.error);
		dialog.setMessage(R.string.label_server_down);
		dialog.setPositiveButton(R.string.button_close, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				dialog.cancel();
			}
		});
		return dialog.create();		
	}
	
	/**
	 * Opens the embedded web kit browser window 
	 * @param url
	 */
	private void openBrowserInUrl(String url) {
		Log.d(this.getClass().getName(), "Acessing URL: "+url);
		webView = (WebView) findViewById(R.id.web_engine);		
		WebViewClient maritacaViewClient = new OAuthWebViewClient(tokenReceiverHandler);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(maritacaViewClient);
		final Activity mActivity = this;
		webView.setWebChromeClient(new WebChromeClient() {
        public void onProgressChanged(WebView view, int progress) {
        	mActivity.setTitle(R.string.label_loading);
        	mActivity.setProgress(progress * 100);
            if(progress == 100)
            	mActivity.setTitle(R.string.app_name);
          }
        });		
		webView.loadUrl(url);
	}
	
	private final Handler tokenReceiverHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			if(what==0) {
				closeBrowserWindow();
			}
		};
	};

	/**
	 * Closes the embedded web kit browser window 
	 */
	private void closeBrowserWindow(){
		webView.setVisibility(View.GONE);
		webView.clearCache(true);
		sqliteHelper = new MaritacaHelper(this);
		mUser = sqliteHelper.getValidUser();
		if(mUser != null) {
			Intent intent = new Intent(this, MenuLoadFormActivity.class);
			intent.putExtra(Constants.USER_DATA, mUser);
			sqliteHelper.onClose();
			startActivity(intent);
		}
	}

	public static Context getContext() {
		return context;
	}
}