package br.unifesp.offlineforms.mobile.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaAnswer;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.offlineforms.mobile.exception.MaritacaExceptionHandler;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;
import br.unifesp.offlineforms.mobile.util.Constants;
import br.unifesp.offlineforms.mobile.util.XMLFormParser;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class LocalDataActivity extends SherlockListActivity {
	
	private MaritacaHelper sqliteHelper;
	private MaritacaUser mUser;
	private String[] questions;
	private List<MaritacaAnswer> answers;
	//
	private Bitmap bitmap;
	private Bitmap rotatedBitmap;
	private ProgressDialog progressDialog;
	
	private String answerGroupId;
	
	public class LocalDataAdapter extends ArrayAdapter<String> {
		public LocalDataAdapter(Context context, int resourceId, String[] questions){
			super(context, resourceId, questions);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.localdata_input, parent, false);
			
			TextView question = (TextView)row.findViewById(R.id.localdata_question_title);
			question.setText(questions[position]);
			
			TableLayout layout = (TableLayout)row.findViewById(R.id.localdata_answer);
			TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
	        layoutParams.setMargins(2, 2, 2, 2);
	        TableRow tblRow = new TableRow(getApplicationContext());
			MaritacaAnswer answer = answers.get(position);			
			tblRow.addView(getViewComponent(answer));
			layout.addView(tblRow);
			
			return row;
		}
		
	private View getViewComponent(MaritacaAnswer answer){
			if(answer.getValue() != null || !"".equals(answer.getValue())){			
				if(answer.getType().equals(ComponentType.AUDIO.getDescription()) && 
						answer.getValue() != null && !answer.getValue().equals("")){
					final File audioFile = new File(answer.getValue());        
			        if (audioFile != null && audioFile.exists()){ 
				        Button btnPlay = new Button(getApplicationContext());
				        btnPlay.setText(R.string.button_play_audio);
				        btnPlay.setOnClickListener(new View.OnClickListener(){
				    		public void onClick(View v) {
				    			playAudioActivity(audioFile);
				    		}
				    	});
				        return btnPlay;
			        }
			        return getEmptyTextView();
				} else if(answer.getType().equals(ComponentType.VIDEO.getDescription()) && 
						answer.getValue() != null && !answer.getValue().equals("")){
					final File videoFile = new File(answer.getValue());
			        if(videoFile != null && videoFile.exists()){
				        Button btnPlay = new Button(getApplicationContext());
				        btnPlay.setText(R.string.button_start_video);
				        btnPlay.setOnClickListener(new View.OnClickListener(){
				    		public void onClick(View v) {
				    			playVideoActivity(videoFile);
				    		}
				    	});
				        return btnPlay;
			        }
			        return getEmptyTextView();
				} else if((answer.getType().equals(ComponentType.PICTURE.getDescription()) || 
						answer.getType().equals(ComponentType.DRAW.getDescription())) && 
						answer.getValue() != null && !answer.getValue().equals("")){
					ImageView imgView = new ImageView(getApplicationContext());					
					//		            
		            try{
		            	BitmapFactory.Options options = new BitmapFactory.Options();
		            	options.inSampleSize = 2;
		            	bitmap = BitmapFactory.decodeFile(answer.getValue(), options);
		            	int w = bitmap.getWidth();
		            	int h = bitmap.getHeight();
		            	ExifInterface exif = new ExifInterface(answer.getValue());
		            
			            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
			            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
			            int rotationAngle = 0;
			            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
			            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
			            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
			
			            Matrix matrix = new Matrix();
			            matrix.setRotate(rotationAngle, (float) w / 2, (float) h / 2);
			            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
			            imgView.setImageBitmap(rotatedBitmap);
		            }
					catch (IOException e) {						
						e.printStackTrace();
					}
					return imgView;				
				} else if(answer.getType().equals(ComponentType.BARCODE.getDescription()) || 
						answer.getType().equals(ComponentType.MONEY.getDescription())){
					TextView t = new TextView(getApplicationContext());
					t.setText(answer.getSubtype() + " " + answer.getValue());
					t.setTextColor(Color.BLACK);
					return t;
				} else if(answer.getType().equals(ComponentType.GEOLOCATION.getDescription()) && 
						answer.getValue() != null && !answer.getValue().equals("")){
					String[] tokens = answer.getValue().split(";");
					if(tokens.length == 2 && !"".equals(tokens[0])){
						TableLayout tbl = new TableLayout(getApplicationContext());
						TableRow tblLatitude = new TableRow(getApplicationContext());
						TextView txtLat = new TextView(getApplicationContext());
						txtLat.setText(getString(R.string.label_latitude) + ": " + tokens[0]);
						txtLat.setTextColor(Color.BLACK);
						tblLatitude.addView(txtLat);
												
						TableRow tblLongitude = new TableRow(getApplicationContext());
						TextView txtLon = new TextView(getApplicationContext());
						txtLon.setText(getString(R.string.label_longitude) + ": " + tokens[1]);
						txtLon.setTextColor(Color.BLACK);
						tblLongitude.addView(txtLon);
						
						tbl.addView(tblLatitude);
						tbl.addView(tblLongitude);
						return tbl;
					} else {
						TextView t = new TextView(getApplicationContext());
						t.setText(answer.getValue());
						return t;
					}					
				} else if(answer.getType().equals(ComponentType.CHECKBOX.getDescription()) || 
						answer.getType().equals(ComponentType.COMBOBOX.getDescription()) || 
						answer.getType().equals(ComponentType.RADIOBOX.getDescription())){
					
					TableLayout tbl = new TableLayout(getApplicationContext());					
					try {
						JSONObject jsonObj = new JSONObject(answer.getValue());
						Iterator it = jsonObj.keys();						
						while (it.hasNext()) {
							String _key = (String) it.next();
							String _value = jsonObj.getString(_key);
							TableRow row = new TableRow(getApplicationContext());
							TextView t = new TextView(getApplicationContext());
							t.setText(_value);
							t.setTextColor(Color.BLACK);
							row.addView(t);
							tbl.addView(row);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}			
					return tbl;
				} else {
					TextView t = new TextView(getApplicationContext());
					t.setText(answer.getValue());
					t.setTextColor(Color.BLACK);
					return t;
				}
			} else {
				return getEmptyTextView();
			}
		}
	}
	
	private TextView getEmptyTextView(){
		TextView t = new TextView(getApplicationContext());
		t.setText("");
		return t;
	}
	
	private void playAudioActivity(File audioFile){
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(audioFile), "audio/3gpp");
		getApplicationContext().startActivity(intent);
	}
	
	private void playVideoActivity(File videoFile){
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(videoFile), "video/mp4");
		getApplicationContext().startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.localdata_menu, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.localdata_menu_delete:
				sqliteHelper = new MaritacaHelper(this);
				this.sqliteHelper.deleteAnswersAndAnswerGroup(answerGroupId);
				sqliteHelper.onClose();
				Log.i("DELETE LOCAL DATA", "local data");
				
				Intent intent = new Intent(this, ResultsActivity.class);
				intent.putExtra(Constants.USER_DATA, mUser);
				startActivity(intent);
				break;
			default:
				break;
		}
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		getListView().setBackgroundResource(R.drawable.mobile_background);
		Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(LocalDataActivity.this));
		this.mUser = (MaritacaUser) getIntent().getSerializableExtra(Constants.USER_DATA);
		if(mUser != null){
			answerGroupId = getIntent().getStringExtra(Constants.ANSWER_GROUP_ID);
			
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				new LoadView(answerGroupId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
			} else {
				new LoadView(answerGroupId).execute();
			}
		}
	}
	
	private void buildView(String answerGroupId){
		XMLFormParser xmlParser  = parseFormXml();
		questions  = fetchQuestionsList(xmlParser).toArray(new String[0]);
		//
		sqliteHelper = new MaritacaHelper(this);
    	answers = sqliteHelper.getAnswerByAnswerGroup(Integer.valueOf(answerGroupId));
    	sqliteHelper.onClose();
	}
	
	//
	private List<String> fetchQuestionsList(XMLFormParser xmlParser){			
		List<String> questions = new ArrayList<String>();
		for(Question q : xmlParser.getForm().getQuestions().getQuestions()){
			questions.add(q.getLabel());
		}
		return questions;
    }
	
	private XMLFormParser parseFormXml(){
		FileInputStream is;
		try {
			is = (FileInputStream) getResources().getAssets().open(Constants.FORM_FILENAME);
			return new XMLFormParser(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		    	
    }
	
	@Override
	protected void onDestroy() {
		if(bitmap != null){
			bitmap.recycle();
			System.gc();
		}
		super.onDestroy();
	}
	
	private class LoadView extends AsyncTask<Void, Integer, Void> {
		private String answerGroupId;

		public LoadView(String answerGroupId) {
			super();
			this.answerGroupId = answerGroupId;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(LocalDataActivity.this, getString(R.string.label_loading), getString(R.string.label_loading_data), false, false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				buildView(answerGroupId);				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			setListAdapter(new LocalDataAdapter(getApplicationContext(), R.layout.localdata_input, questions));
		}
	}
}