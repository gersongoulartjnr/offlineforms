package br.unifesp.maritaca.mobile.services;

import java.util.Date;

import net.smartam.leeloo.common.OAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.unifesp.maritaca.mobile.activities.MenuLoadFormActivity;
import br.unifesp.maritaca.mobile.activities.R;
import br.unifesp.maritaca.mobile.dataaccess.MaritacaHelper;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.maritaca.mobile.exception.MaritacaException;
import br.unifesp.maritaca.mobile.thread.RestRequestThread;
import br.unifesp.maritaca.mobile.util.Constants;
import br.unifesp.maritaca.mobile.util.HttpMethod;
import br.unifesp.maritaca.mobile.util.Utils;

/**
 * 
 * @author Leonardo Sueoka
 * @version 1.1.0
 * 
 */
public class NotificationService extends IntentService {
	
	private static final int notificationMaritacaID = 1;
	private MaritacaUser mUser;
	private NotificationManager notificationManager;
	private String token = "";
	private MaritacaHelper sqliteHelper;
	
    public NotificationService() {
		super("NotificationService");
	}    
	
	@Override
	public void onCreate() {
		  super.onCreate();
		  notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		  sqliteHelper = new MaritacaHelper(this);
	}
	
	/*@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}*/
	
	@Override
	protected void onHandleIntent(Intent intent) {
		mUser = (MaritacaUser) intent.getSerializableExtra(Constants.USER_DATA);
		if (mUser != null)
			token = mUser.getAccessToken();
		else
			throw new MaritacaException("Service Stopped - User Invalid");
		
		long date = sqliteHelper.getLastSyncTime(mUser.getId(), Constants.SETTINGS_LAST_SYNC_SERVER_TIME);
		Log.i("DATE SYNC", String.valueOf(date));
		String uri = Constants.URI_WS_GET_LAST_ANSWERS + Constants.FORM_ID + "?" + "date=" + date + "&" + OAuth.OAUTH_TOKEN + "=" + token;
		String errorMessage = null;
		
		Log.i("URL", uri);
		
		try {
			HttpResponse response = (HttpResponse) (new RestRequestThread())
					.execute(uri, token, HttpMethod.GET.toString(), "", "")
					.get();
			HttpEntity entity = response.getEntity();
			
			String responseContent = EntityUtils.toString(entity);
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
				JSONObject json = new JSONObject(responseContent);
				String value = (String) json.get(Constants.JSON_GENERIC_RESPONSE_VALUE);
				startNotification(Integer.parseInt(value));
				value = (String) json.get(Constants.JSON_GENERIC_RESPONSE_EXTRA);
				sqliteHelper.updateLastSyncTime(new Date().getTime(), Long.parseLong(value), mUser.getId());
			} else {
				errorMessage = Utils.getMessageFromErrorResponse(responseContent);
				throw new RuntimeException(errorMessage != null ? errorMessage : "");
			}
			sqliteHelper.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Integer handleResponse(String content){
		int initialIndex = content.lastIndexOf("<value>") + 7;
		int finalIndex = initialIndex;
		
		while(content.charAt(finalIndex) != '<'){
			finalIndex++;
		}
		return Integer.parseInt(content.substring(initialIndex, finalIndex));
	}
	
	public void startNotification(int collectQuantity) {
		if (collectQuantity == 0)
			return;

		Notification notification = new Notification(R.drawable.info, "Maritaca Info", System.currentTimeMillis());
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "New data collected!";
		CharSequence contentText = collectQuantity +" more individual collect!";
		Intent notificationIntent = new Intent(this, MenuLoadFormActivity.class);
		notificationIntent.putExtra(Constants.USER_DATA, mUser);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;  
		
		notificationManager.notify(notificationMaritacaID, notification);
	}
}