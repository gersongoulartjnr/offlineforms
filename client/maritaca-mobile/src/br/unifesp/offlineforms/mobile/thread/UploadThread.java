package br.unifesp.offlineforms.mobile.thread;

import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.offlineforms.mobile.notifications.NotificationHelper;
import br.unifesp.offlineforms.mobile.util.RequestStatus;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public class UploadThread extends AsyncTask<Integer, Integer, Void> {

	private Activity activity;
	private NotificationHelper notificationHelper;
	private MaritacaUser mUser;
	private MaritacaHelper sqliteHelper;
	private Context context;
	private long totalSize;
	private Integer totalAnswers;
	private Integer currentAnwer;
	private RequestStatus reqStatus;
	
	public UploadThread(Activity activity, Context context, MaritacaUser mUser) {
		this.mUser = mUser;
		this.context = context;
		this.activity = activity;
		this.notificationHelper = new NotificationHelper(context);
		this.sqliteHelper = new MaritacaHelper(context);
	}
	
	@Override
	protected void onPreExecute() {
		notificationHelper.createNotification();
	}
	
	@Override
	protected Void doInBackground(Integer... params) {


		 sqliteHelper.getAnswersGSTeste();
		return null;
	}
	
	protected Integer getTotalAnswers() {		
		return totalAnswers;
	}

	protected Integer getCurrentAnswer() {
		return currentAnwer;
	}

	@Override
    protected void onProgressUpdate(Integer... progress) {
        notificationHelper.progressUpdate(progress[0], progress[1], progress[2]);
    }

	@Override
	protected void onPostExecute(Void result) {
		notificationHelper.completed();

		
	/*	File file = new File(Constants.PATH_CSV);
		file.setReadable(true,false);
		Intent email = new Intent(Intent.ACTION_SEND);	  
		email.putExtra(Intent.EXTRA_SUBJECT, "subject");
		email.putExtra(Intent.EXTRA_TEXT, "message");
		email.setType("message/rfc822");
	    email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

		activity.startActivity(Intent.createChooser(email, "Choose an Email client :"));*/
		
		
		
		//if (MenuLoadFormActivity.isMenuActivityRunning) {
			
			//Intent intent = new Intent(context, MenuLoadFormActivity.class);
			//intent.putExtra(Constants.USER_DATA, mUser);
			//activity.startActivity(intent);
	//	}
		
	}

}	