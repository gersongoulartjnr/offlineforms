package br.unifesp.maritaca.mobile.activities;

import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import br.unifesp.maritaca.mobile.exception.MaritacaException;
import br.unifesp.maritaca.mobile.thread.OAuthClientThread;

/**
 * 
 * @author Tiago Barabasz
 *
 */
public class OAuthWebViewClient extends WebViewClient {
	
	private Handler handler;
	private String code;
	
	public OAuthWebViewClient(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {		
		if(url.matches(".*code=.*") && !url.matches(".*facebook.*") && code == null){
			code = url.split("=")[1];			
			retrieveAccessToken(code);
		}
		super.onPageFinished(view, url);
	}
	
	private void retrieveAccessToken(String code) {
		try {
			if (!(new OAuthClientThread()).execute(code).get()) {
				throw new RuntimeException("Error in oauth service");
			}
			this.handler.sendEmptyMessage(0);
		} catch (Exception e) {
			throw new MaritacaException(e.getMessage());
		}
	}
}