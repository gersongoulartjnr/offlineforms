package br.unifesp.offlineforms.mobile.activities;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.smartam.leeloo.common.OAuth;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaLog;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.offlineforms.mobile.thread.CheckServerUpThread;
import br.unifesp.offlineforms.mobile.util.Constants;

/**
 * 
 * @author Jimmy Valverde S&aacute;nchez
 * @version 0.1.6
 *
 */
public class ErrorReportActivity extends Activity {
	
	private MaritacaHelper sqliteHelper;	
	private CheckServerUpThread isServerUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.errorreport);
		showDialog(Constants.DIALOG_ERROR_REPORT);			
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
    	switch(id) {
    		case Constants.DIALOG_ERROR_REPORT:
    			dialog = showDialogErrorReport();
    			break;
    	}    
    	return dialog;
	}
	
	private Dialog showDialogErrorReport() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.label_error);
		dialog.setIcon(R.drawable.error);
		dialog.setMessage(R.string.label_error_has_occurred);
		dialog.setPositiveButton(R.string.button_yes, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendErrorReport();
				Intent intent = new Intent(ErrorReportActivity.this, LoginActivity.class);
				//intent.putExtra(Constants.USER_DATA, mUser);
				startActivity(intent);
				finish();
				dialog.cancel();
			}
		});
		dialog.setNegativeButton(R.string.button_no, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(ErrorReportActivity.this, LoginActivity.class);
				//intent.putExtra(Constants.USER_DATA, mUser);
				startActivity(intent);
				finish();
				dialog.cancel();
			}
		});
		return dialog.create();		
	}
	
	private void sendErrorReport() {
		isServerUp = new CheckServerUpThread();
		String code = getIntent().getStringExtra(Constants.UNCAUGHT_EXCEPTION_CODE);
		String error = getIntent().getStringExtra(Constants.UNCAUGHT_EXCEPTION_ERROR);
		if(code != null && error != null) {
			try {				
				sqliteHelper = new MaritacaHelper(this);
				MaritacaUser mUser = sqliteHelper.getValidUser();
				if(mUser != null) {
					String uri = Constants.URI_WS_ERRORREPORT + "?" + OAuth.OAUTH_TOKEN+ "=" + mUser.getAccessToken();
					sqliteHelper.addError(mUser.getId(), new MaritacaLog(null, error, System.currentTimeMillis(), android.os.Build.VERSION.RELEASE));
					
					if(isServerUp.execute().get() && mUser.getExpirationDate() > 0) {
						String report = getXMLLogs(sqliteHelper.getLogs());
						DefaultHttpClient httpClient = new DefaultHttpClient();
						HttpPut put = new HttpPut(uri);		
						StringEntity entity = new StringEntity(report, "UTF-8");					
						entity.setContentType("application/xml");
						put.setEntity(entity);
						put.getParams().setParameter(OAuth.OAUTH_TOKEN, mUser.getAccessToken());
						HttpResponse response = httpClient.execute(put);
						Log.i("Response Status", String.valueOf(response.getStatusLine().getStatusCode()));
						if(response.getStatusLine().getStatusCode() == 200) {
							sqliteHelper.deleteLogs();
						}
					}
				}
				else {
					//TODO: Save as anonymous?
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			} catch (Exception e) {
				Log.e(this.getClass().getName(), e.getMessage());
				throw new RuntimeException(e.getMessage());
			} finally {
				sqliteHelper.onClose();
			}
		}
	}
	
	/**
	 * 
	 * @param logs
	 * @return
	 */
	private String getXMLLogs(List<MaritacaLog> logs) {
		if(logs == null || logs.isEmpty())
			return "";
		
		try {
			XmlSerializer serializer = Xml.newSerializer();		    
		    StringWriter writer = new StringWriter();		    	
		    serializer.setOutput(writer);
		    serializer.startDocument("UTF-8", true);
		    	serializer.startTag("", "errorreport");
		    		serializer.startTag("", "errors");
		    		for(MaritacaLog	log : logs) {
		    			serializer.startTag("", "error");
				    	serializer.attribute("", "androidVersion", log.getAndroidVersion());
					    	serializer.startTag("", "value");
					    	serializer.text(log.getMessage());
					    	serializer.endTag("", "value");
				    	serializer.endTag("", "error");
		    		}
		    		serializer.endTag("", "errors");
		    	serializer.endTag("", "errorreport");
		    serializer.endDocument();
		    return writer.toString();		    
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}