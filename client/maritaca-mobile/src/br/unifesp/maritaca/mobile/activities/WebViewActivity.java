package br.unifesp.maritaca.mobile.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.maritaca.mobile.util.Constants;

public class WebViewActivity extends Activity {

	private MaritacaUser mUser;
	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		this.mUser = (MaritacaUser) getIntent().getSerializableExtra(Constants.USER_DATA);
        if(mUser != null) {
			String formUrl = getIntent().getStringExtra(Constants.FORM_URL_DATA);
			String url = Constants.URI_SERVER+"/report-viewer-mob.html?id="+formUrl+"&token="+mUser.getAccessToken();
			Log.i("URL", url);
			webView = (WebView) findViewById(R.id.webView1);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl(url);
        }
	}
}