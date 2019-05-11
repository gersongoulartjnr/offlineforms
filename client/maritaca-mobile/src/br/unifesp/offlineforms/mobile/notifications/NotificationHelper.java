package br.unifesp.offlineforms.mobile.notifications;

import br.unifesp.offlineforms.mobile.activities.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper {

	private Context context;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder mBuilder;
	private Notification notification;
	private CharSequence contentTitle;
	private PendingIntent contentIntent;
	private int NOTIFICATION_ID = 1;
	
	public NotificationHelper(Context context) {
		this.context = context;
	}

	public void createNotification() {
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
	    mBuilder.setContentTitle(context.getString(R.string.msg_upload_ticker))
	            .setContentText(context.getString(R.string.msg_upload_content_title))
	            .setSmallIcon(android.R.drawable.stat_sys_upload);
		
		int icon = android.R.drawable.stat_sys_upload;
		CharSequence tickerText = context.getString(R.string.msg_upload_ticker);
		long when = System.currentTimeMillis();
		notification = new Notification(icon, tickerText, when);
		
		contentTitle = context.getString(R.string.msg_upload_content_title);
		CharSequence contentText = R.string.msg_upload_sending + "0% 0/100.";
		
		Intent notificationIntent = new Intent();
		contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		mBuilder.setProgress(100, 0, false);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		} else {
			notificationManager.notify(NOTIFICATION_ID, notification);
		}
	}
	
    public void progressUpdate(int currentData, int currentAnswer, int totalAnswers) {
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mBuilder.setProgress(100, currentData, false);
			mBuilder.setContentText(context.getString(R.string.msg_upload_sending) + " " + currentAnswer + " of " + totalAnswers + ".");
			notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		} else {
	        CharSequence contentText = R.string.msg_upload_sending + currentData + "% " + currentData+ "/100.";
	        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		}
    }
    
    public void completed()    {
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
