package br.unifesp.offlineforms.mobile.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import net.smartam.leeloo.common.OAuth;

import org.apache.http.HttpResponse;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.unifesp.offlineforms.mobile.custom.CustomAlertDialog;
import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.offlineforms.mobile.exception.MaritacaExceptionHandler;
import br.unifesp.offlineforms.mobile.services.NotificationService;
import br.unifesp.offlineforms.mobile.thread.CheckServerUpThread;
import br.unifesp.offlineforms.mobile.thread.RestRequestThread;
import br.unifesp.offlineforms.mobile.util.Constants;
import br.unifesp.offlineforms.mobile.util.HttpMethod;
import br.unifesp.offlineforms.mobile.util.Utils;

public class MenuActivityOld extends SherlockActivity implements OnClickListener {

	private MaritacaHelper sqliteHelper;
	private MaritacaUser mUser;
	
	private String emailsList;

	public static boolean isMenuActivityRunning = false;

	@Override
	protected void onStart() {
		super.onStart();
		isMenuActivityRunning = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isMenuActivityRunning = false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(
				MenuActivityOld.this));
		this.initMenu();
	}
	
	public void onBackPressed() {
		Intent mainActivity = new Intent(Intent.ACTION_MAIN);
		mainActivity.addCategory(Intent.CATEGORY_HOME);
		mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(mainActivity);
		finish();
	}

	private void initMenu() {
		sqliteHelper = new MaritacaHelper(this);
		setContentView(R.layout.novomenu);
		//this.mUser = (MaritacaUser) getIntent().getSerializableExtra(
	//			Constants.USER_DATA);
	//	if (mUser != null) {
			/**
			 * Creating all buttons instances
			 * */
			// Dashboard Collect button
			Button btn_collect = (Button) findViewById(R.id.btn_collect);
			btn_collect.setOnClickListener(this);
			// Dashboard Send Data button
		//	int numOfCollects = sqliteHelper.getCollectNumber(mUser.getEmail());
			Button btn_send = (Button) findViewById(R.id.btn_send);
		/*	if (numOfCollects > 0) {
				if (sqliteHelper.isSendingDataAuto(mUser.getId())) {
					new UploadThread(this, getApplicationContext(), mUser).execute(0);
					btn_send.setEnabled(false);
				} else {
					String collectButtonLbl = String
							.valueOf(btn_send.getText())
							+ " ("
							+ numOfCollects
							+ ")";
					btn_send.setText(collectButtonLbl);
				}
			} else {*/
				btn_send.setEnabled(true);
			//}
			sqliteHelper.onClose();
			btn_send.setOnClickListener(this);
			// Dashboard Answers button
			Button btn_answers = (Button) findViewById(R.id.btn_answers);
			btn_answers.setOnClickListener(this);
			// Dashboard Settings button
			Button btn_settings = (Button) findViewById(R.id.btn_settings);
			btn_settings.setOnClickListener(this);
			// Dashboard About button
			Button btn_about = (Button) findViewById(R.id.btn_about);
			btn_about.setOnClickListener(this);
			// Dashboard Tell a Friend button
			Button btn_friend = (Button) findViewById(R.id.btn_tell_friend);
			btn_friend.setOnClickListener(this);
			// Foot actions

			TextView userText = (TextView) findViewById(R.id.logged_user);
		//	userText.setText(mUser.getEmail());
			TextView expiresText = (TextView) findViewById(R.id.expiration_time);
			//expiresText.setText(getStrExpDate(mUser.getExpirationDate()));
			View logoutButton = findViewById(R.id.logout);
			logoutButton.setOnClickListener(this);
	//	}
	}

	private String getStrExpDate(long millis) {
		if (millis <= 0) {
			return "0m";
		}
		int minutes = (int) ((millis / (1000 * 60)) % 60);
		int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
		int days = (int) ((millis / (1000 * 60 * 60 * 24)) % 365);
		String txt = "";
		if (days > 0)
			txt = txt + String.valueOf(days) + "d";
		if (hours > 0)
			txt = txt + String.valueOf(hours) + "h";
		if (minutes > 0)
			txt = txt + String.valueOf(minutes) + "m";
		return "".equals(txt) ? "0m" : txt;
	}

	private boolean expirationDateIsValid() {
		boolean isValid = false;
		sqliteHelper = new MaritacaHelper(MenuActivityOld.this);
		if (sqliteHelper != null && mUser != null) {
			Long expDate = sqliteHelper.getExpirationDateByUsername(mUser
					.getEmail());
			if (expDate != null && expDate > 0) {
				isValid = true;
			}
			sqliteHelper.close();
		}
		return isValid;
	}

	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.btn_collect:
			intent = new Intent(this, ControllerActivity.class);
			//intent.putExtra(Constants.USER_DATA, mUser);
			intent.putExtra(Constants.USER_DATA, "");
			startActivity(intent);
			break;
		case R.id.btn_send:
			//if (expirationDateIsValid()) {
				sendCollectedData();
			
				
		//	} else {
			//	showDialog(Constants.DIALOG_TIME_EXPIRED);
			//}
			break;
		case R.id.btn_answers:
		//	if (expirationDateIsValid()) {
				intent = new Intent(this, ResultsActivity.class);
				intent.putExtra(Constants.USER_DATA, "");
				startActivity(intent);
			//} else {
				//showDialog(Constants.DIALOG_TIME_EXPIRED);
			//}
			break;
		case R.id.btn_about:
			intent = new Intent(this, AboutActivity.class);
			intent.putExtra(Constants.USER_DATA, mUser);
			startActivity(intent);
			break;
		case R.id.logout:
			showDialog(Constants.DIALOG_LOGOUT);
			break;
		case R.id.btn_tell_friend:
			if (expirationDateIsValid()) {
				inviteFriends(null);
			} else {
				showDialog(Constants.DIALOG_TIME_EXPIRED);
			}
			break;

		case R.id.btn_settings:
			intent = new Intent(this, SettingsActivity.class);
			intent.putExtra(Constants.USER_DATA, mUser);
			startActivity(intent);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initMenu();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy() {
		if (sqliteHelper != null) {
			sqliteHelper.onClose();
		}
		super.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case Constants.DIALOG_INVITE_OFFLINE_OK_CANCEL:
			dialog = showDialogInviteWhenServerIsOffline();
			break;
		case Constants.DIALOG_OFFLINE:
			dialog = showDialogWhenMaritacaIsOffline();
			break;
		case Constants.DIALOG_TIME_EXPIRED:
			dialog = showDialogWhenSessionHasExpired();
			break;
		case Constants.DIALOG_LOGOUT:
			dialog = doLogout();
			break;
		}
		return dialog;
	}
	
	private Dialog showDialogInviteWhenServerIsOffline(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.label_connection_error);
        dialog.setIcon(R.drawable.error);
        dialog.setMessage(R.string.label_server_down);
        dialog.setPositiveButton("Retry", 
            new Dialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which){
                    inviteFriends(getEmailsList());
                }
            });
        dialog.setNegativeButton(R.string.button_close,
            new Dialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        return dialog.create();
    }

	private Dialog showDialogWhenSessionHasExpired() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.title_logout);
		dialog.setIcon(R.drawable.error);
		dialog.setMessage(R.string.msg_session_expired);
		dialog.setPositiveButton(R.string.button_close,
				new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						sqliteHelper = new MaritacaHelper(MenuActivityOld.this);
						sqliteHelper.updateUser(mUser.getEmail(), "", "", 0, 0);
						sqliteHelper.onClose();
						Intent intent = new Intent(MenuActivityOld.this,
								LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						dialog.cancel();
					}
				});
		return dialog.create();
	}

	private Dialog showDialogWhenMaritacaIsOffline() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.label_connection_error);
		dialog.setIcon(R.drawable.error);
		dialog.setMessage(R.string.label_server_down);
		dialog.setPositiveButton(R.string.button_close,
				new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return dialog.create();
	}

	private Dialog doLogout() {
		AlertDialog.Builder aDialog = new AlertDialog.Builder(MenuActivityOld.this);
		aDialog.setTitle(R.string.label_confirmation);
		aDialog.setIcon(R.drawable.info);
		aDialog.setMessage(R.string.msg_logout);
		aDialog.setPositiveButton(R.string.button_yes,
				new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						sqliteHelper = new MaritacaHelper(MenuActivityOld.this);
						sqliteHelper.updateUser(mUser.getEmail(), "", "", 0, 0);
						sqliteHelper.onClose();
						Intent intent = new Intent(MenuActivityOld.this,
								LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						dialog.cancel();
					}
				});
		aDialog.setNegativeButton(R.string.button_no,
				new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return aDialog.create();
	}

	private void sendCollectedData() {
	//	try {
		//	if ((new CheckServerUpThread()).execute().get()) {
			/*	if (mUser == null && mUser.getId() == null) {
					showDialog(Constants.DIALOG_TIME_EXPIRED);
					return;
				}*/
        sqliteHelper = new MaritacaHelper(this);
		Object [] resp = sqliteHelper.getAnswersGSTeste();
        Intent intent = new Intent(this, SendResponses.class);
        intent.putExtra("r1", resp);
		startActivity(intent);

				//new UploadThread(this, getApplicationContext(), null)
				//		.execute(0);

						
	//		} else {
		//		showDialog(Constants.DIALOG_OFFLINE);
			//}
	//	} catch (Exception e) {
		//	Log.e(this.getClass().getName(), e.getMessage());
		//}
	}

	public void checkNotificationService() {
		sqliteHelper = new MaritacaHelper(this);
		if (!sqliteHelper.isSyncActive(mUser.getId())) {
			sqliteHelper.onClose();
			return;
		}
		int interval = sqliteHelper.getSyncInteval(mUser.getId()) * 60 * 1000; // 30min = 1800
		Intent intentService = new Intent(MenuActivityOld.this,
				NotificationService.class);
		intentService.putExtra(Constants.USER_DATA, mUser);
		PendingIntent pintent = PendingIntent.getService(MenuActivityOld.this, 0,
				intentService, 0);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		long lastSync = sqliteHelper.getLastSyncTime(mUser.getId(), Constants.SETTINGS_LAST_SYNC_LOCAL_TIME);
		long currentTime = (new Date()).getTime();
		long triggerAtTime = ((currentTime - lastSync) > interval) ? currentTime
				: (currentTime + (currentTime - lastSync));
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, interval,
				pintent);
		sqliteHelper.onClose();
	}

	/* Invite */
	/**
	 * Create the query to get the emails
	 * 
	 * @return
	 */
	private Cursor getEmails() {
		Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Email.DATA };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + "='1'";
		String sortOrder = ContactsContract.CommonDataKinds.Email.DATA
				+ " COLLATE LOCALIZED ASC";
		return managedQuery(uri, projection, selection, null, sortOrder);
	}

	/**
	 * Gets the string of emails
	 * 
	 * @param preEmails
	 * @return
	 */
	private String getEmailsList(String preEmails) {
		final StringBuilder emails = new StringBuilder();
		if (preEmails != null && !"".equals(preEmails)) {
			emails.append(preEmails);
			if (!preEmails.trim().endsWith(",")) {
				emails.append(", ");
			}
		}
		Cursor cursor = getEmails();
		try {
			if (cursor != null) {
				while (cursor.moveToNext()) {
					emails.append(cursor.getString(2));
					emails.append(", ");
				}
			}
			return emails.toString();
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getMessage());
			return preEmails;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	private void inviteFriends(String emailsList){        
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.invite_friends, null);
        CustomAlertDialog builder = new CustomAlertDialog(this);
        builder.setTitle(getString(R.string.title_invite_friends))
                .setMessage(getString(R.string.label_invite_friends))
                .setView(view);

        final EditText friendsLst = (EditText) view.findViewById(R.id.friends_list);
        if(emailsList != null){
            friendsLst.setText(getEmailsList());
        }
        final Button btnMyContacts = (Button) view.findViewById(R.id.btnMyContacts);
        btnMyContacts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String emails = getEmailsList(friendsLst.getText().toString());
                if (emails != null) {
                    friendsLst.setText(emails);
                }
            }
        });
        final Button btnClear = (Button) view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                friendsLst.setText("");
                setEmailsList(null);
            }
        });

        builder.setPositiveButton(getString(R.string.button_invite),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inviteList = getInviteXML(friendsLst.getText().toString());
                        if (inviteList != null) {
                            setEmailsList(friendsLst.getText().toString());
                            sendEmailsToServer(inviteList);
                            dialog.dismiss();
                        } else {
                            Toast t = Toast.makeText(getApplicationContext(), R.string.label_error_emailslist, Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
        builder.setNegativeButton(getString(R.string.button_cancel), null);
        builder.show();
    }

	private void sendEmailsToServer(String inviteXML) {
		try {
			if ((new CheckServerUpThread()).execute().get()) {
				if (inviteXML != null || !"".equals(inviteXML)) {
					String uri = Constants.URI_WS_INVITE_FRIENDS + "?" + OAuth.OAUTH_TOKEN + "=" + mUser.getAccessToken();
					String token = mUser.getAccessToken();
					String errorMsg = getString(R.string.label_sending_data_error);
					HttpResponse response = (HttpResponse) (new RestRequestThread())
							.execute(uri, token, HttpMethod.PUT.toString(),
									inviteXML, errorMsg).get();

					Log.i("Response Status", String.valueOf(response.getStatusLine().getStatusCode()));
					if (response.getStatusLine().getStatusCode() == 200) {
						Toast.makeText(this, getString(R.string.label_sending_invitations), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, getString(R.string.label_sending_data_error), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, getString(R.string.label_friends_required), Toast.LENGTH_SHORT).show();
				}
			} else {
				showDialog(Constants.DIALOG_INVITE_OFFLINE_OK_CANCEL);
			}
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
	}

	private String getInviteXML(String emails) {
		List<String> emailList = getEmailsListFromString(emails);
		if (emails == null || "".equals(emails) || emailList == null
				|| emailList.isEmpty())
			return null;

		try {
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "invite");
			serializer.startTag("", "formId");
			serializer.text(Constants.FORM_ID);
			serializer.endTag("", "formId");
			serializer.startTag("", "friends");
			for (String s : emailList) {
				serializer.startTag("", "friend");
				serializer.text(s);
				serializer.endTag("", "friend");
			}
			serializer.endTag("", "friends");
			serializer.endTag("", "invite");
			serializer.endDocument();
			return writer.toString();
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			return null;
		}
	}

	private List<String> getEmailsListFromString(String emails) {
		List<String> emailsList = new ArrayList<String>();
		String[] tokens = emails.split(",");
		for (int i = 0; i < tokens.length; i++) {
			String email = tokens[i].trim();
			if (!Utils.EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
				return null;
			}
			emailsList.add(email);
		}
		return emailsList;
	}
	

	

	/*** Invite friends ***/
	public String getEmailsList() {
		return emailsList;
	}
	public void setEmailsList(String emailsList) {
		this.emailsList = emailsList;
	}
}