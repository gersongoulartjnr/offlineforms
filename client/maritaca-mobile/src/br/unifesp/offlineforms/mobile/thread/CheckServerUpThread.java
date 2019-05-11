package br.unifesp.offlineforms.mobile.thread;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import br.unifesp.offlineforms.mobile.util.Constants;

import android.os.AsyncTask;
import android.util.Log;

public class CheckServerUpThread extends AsyncTask<Void, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			URL url = new URL(Constants.URI_SERVER);
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setRequestProperty("User-Agent", "MaritacaMobile");
			urlCon.setRequestProperty("Connection", "close");
			urlCon.setConnectTimeout(2000);
			urlCon.connect();
			return (urlCon.getResponseCode() == 200);
		} catch (IOException e) {
			Log.i("ERROR", "" + e.getMessage());
			return false;
		}
	}

}
