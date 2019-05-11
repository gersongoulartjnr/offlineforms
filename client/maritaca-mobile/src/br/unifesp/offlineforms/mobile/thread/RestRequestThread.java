package br.unifesp.offlineforms.mobile.thread;

import java.io.UnsupportedEncodingException;

import net.smartam.leeloo.common.OAuth;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import br.unifesp.offlineforms.mobile.exception.MaritacaException;
import br.unifesp.offlineforms.mobile.util.HttpMethod;

public class RestRequestThread extends AsyncTask<String, Integer, HttpResponse> {
	
	private static final String CONTENT_TYPE = "application/xml";
	private static final String ENCODE = "UTF-8";		
	
	private String errorMsg;	
	
	@Override
	protected HttpResponse doInBackground(String... params) {
		try {
				if (params.length != 5) {
					Log.e(this.getClass().getName(), "Only five values are allowed");
					throw new RuntimeException("Only five values are allowed");
				}
				String uri = params[0];
				String token = params[1];
				String content = params[3];
				errorMsg = params[4];
				
				HttpUriRequest request = null;
				
				switch (HttpMethod.getValue(params[2])) {
					case GET:
						request = getRequest(uri, token);
						break;
					case PUT:
						request = putRequest(uri, token, content);
						break;	
					default:
						throw new RuntimeException("Method is not allowed");
				}
		        DefaultHttpClient httpClient = new DefaultHttpClient();
		        
		        return httpClient.execute(request);
			} catch (Exception e) {
		        //Toast.makeText(MenuLoadFormActivity.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
		        throw new MaritacaException(e.getMessage());
			}
	}
	
	private HttpUriRequest putRequest(String uri, String token, String content) throws UnsupportedEncodingException  {
		HttpPut putRequest = new HttpPut(uri);
		StringEntity entity;
		entity = new StringEntity(content, ENCODE);
		entity.setContentType(CONTENT_TYPE);
		putRequest.setEntity(entity);
		putRequest.getParams().setParameter(OAuth.OAUTH_TOKEN, token);
		return putRequest;	
	}

	private HttpUriRequest getRequest(String uri, String token) {
		HttpGet getRequest = new HttpGet(uri);
		getRequest.getParams().setParameter(OAuth.OAUTH_TOKEN, token);
		return getRequest;
	}
}