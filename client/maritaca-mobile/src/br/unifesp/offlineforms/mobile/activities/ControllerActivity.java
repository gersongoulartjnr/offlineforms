package br.unifesp.offlineforms.mobile.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.unifesp.offlineforms.mobile.activities.SimpleGestureFilter.SimpleGestureListener;
import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaAnswer;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.offlineforms.mobile.exception.MaritacaException;
import br.unifesp.offlineforms.mobile.exception.MaritacaExceptionHandler;
import br.unifesp.offlineforms.mobile.model.Model;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.BarCodeInformation;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;
import br.unifesp.offlineforms.mobile.util.Constants;
import br.unifesp.offlineforms.mobile.view.Viewer;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ControllerActivity extends SherlockActivity implements SimpleGestureListener{

	private Viewer viewer;
	private Model model;
	private MaritacaHelper sqliteHelper;
	private MaritacaUser mUser;
	private ProgressDialog progressDialog;

	private SimpleGestureFilter detector;

	private ListView suggestionsList;
	private LocationManager locationManager = null;
	private LocationListener locationListener = null;
	private Bitmap bitmap;
	private Bitmap rotatedBitmap;
	
	//List Mode
	private boolean isFromListMode = false;
	private Integer questionId = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		try {
			detector = new SimpleGestureFilter(this, this);
			Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(ControllerActivity.this));
			//this.mUser = (MaritacaUser) getIntent().getSerializableExtra(Constants.USER_DATA);
			
			//if (mUser != null) {
			//	setFromListMode(getIntent().getExtras().getBoolean(Constants.IS_LIST_MODE_ACTIVE));
			//	setQuestionId(getIntent().getExtras().getInt(Constants.QUESTION_ID));
				
				if(savedInstanceState != null){//
					setModel((Model)savedInstanceState.getParcelable(Constants.MODEL_PARCELABLE));
					if(getQuestionId() != null)
						getModel().setCurrentIdQuestion(getQuestionId());
				} else{
				
					
					FileInputStream is = new FileInputStream(Constants.PATH_FORM);
					//InputStream is = getResources().getAssets().open(Constants.FORM_FILENAME);
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new LoadView(is, getQuestionId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
					} else {
						new LoadView(is, getQuestionId()).execute();
					}
				}
			//}
		} catch (Exception e) {
			// TODO: Dialog
			throw new MaritacaException(e.getMessage());
		}
	}
	
	private void createView() {
		if (getModel() != null) {
			viewer = new Viewer(this, getModel().getCurrentQuestion());
			setContentView(viewer.getView());
		}
	}
	
	//Navigation Buttons Listeners
	public void onPreviousClick(View v) {
		if(isFromListMode()){
			viewer.saveAnswer();
			saveAnswerFromListMode();
			previous();
		} else{
			viewer.saveAnswer();
			previous();
		}
	}
	
	public void onNextClick(View v) {
		if(isFromListMode()){
			viewer.saveAnswer();
			saveAnswerFromListMode();
			next();
		} else{
			viewer.saveAnswer();
			next();
		}
	}
	
	//Action Bar Methods
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getSupportMenuInflater();
		
		if(isFromListMode()){	
			inflater.inflate(R.menu.list_mode_menu, menu);
		} else { 
			inflater.inflate(R.menu.main_menu, menu);
		}
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}
	
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
		case R.id.btn_cancel:
			showDialog(Constants.CANCEL_QUESTION);
			return true;
		/*case R.id.btn_help:
			helpQuestion();
			return true;*/
		case R.id.btn_next:
			viewer.saveAnswer();
			next();
			return true;
		case R.id.btn_previous:
			viewer.saveAnswer();
			previous();
			return true;
			
		case R.id.list_mode_btn_back:
			viewer.saveAnswer();
			saveAnswerFromListMode();
			backFromAnswerInListMode();
			return true;
		case R.id.list_mode_btn_help:
			helpQuestion();
			return true;
		case R.id.list_mode_btn_cancel:
			showDialog(Constants.CANCEL_LIST_QUESTION);
			return true;
		case R.id.list_mode_btn_previous:
			viewer.saveAnswer();
			saveAnswerFromListMode();
			previous();
			return true;
		case R.id.list_mode_btn_next:
			viewer.saveAnswer();
			saveAnswerFromListMode();
			next();
			return true;
			
		case android.R.id.home:
		//app icon in action bar clicked; go home
			showDialog(Constants.CANCEL_QUESTION);
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}		
	}	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			if(isFromListMode()){
				viewer.saveAnswer();
				saveAnswerFromListMode();
				previous();
			} else{
				viewer.saveAnswer();
				previous();
			}
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			if(isFromListMode()){
				viewer.saveAnswer();
				saveAnswerFromListMode();
				next();
			} else{
				viewer.saveAnswer();
				next();
			}
			break;
		case SimpleGestureFilter.SWIPE_DOWN:
			break;
		case SimpleGestureFilter.SWIPE_UP:
			openOptionsMenu();
			break;
		}
	}

	@Override
	public void onDoubleTap() {}

	@Override
	protected void onPause() {
		super.onPause();
		//this.createView();
		this.stopLocationListener();				
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.createView();
	}

	@Override
	protected void onDestroy() {
		if (sqliteHelper != null) {
			sqliteHelper.onClose();
		}
		if (bitmap != null) {
			bitmap.recycle();
		}
		if (rotatedBitmap != null) {
			rotatedBitmap.recycle();
		}
		this.stopLocationListener();
		System.gc();
		super.onDestroy();
	}

	public void next() {
		// TODO validate!
		if (!getModel().getCurrentQuestion().validate()) {
			Toast.makeText(this, getString(R.string.label_enter_valid_value), Toast.LENGTH_SHORT).show();
		} else if (getModel().next()) {
			viewer = new Viewer(this, getModel().getCurrentQuestion());
			setContentView(viewer.getView());
		} else {
			if(isFromListMode()){
				showDialog(Constants.SAVE_LIST_QUESTIONS);
			} else{
				showDialog(Constants.SAVE_DIALOG);
			}
		}
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case Constants.SAVE_DIALOG:
			dialog = showSaveDialog();
			break;
		case Constants.CANCEL_QUESTION:
			dialog = showCancelQuestion();
			break;
			
		case Constants.SAVE_LIST_QUESTIONS:
			dialog = showSaveFromListQuestions();
			break;
		case Constants.CANCEL_LIST_QUESTION:
			dialog = showCancelFromListQuestions();
			break;
		default:
			dialog = null;
			break;
		}
		return dialog;
	}

	private Dialog showSaveDialog() {
		AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
		saveDialog.setTitle(R.string.label_confirmation);
		saveDialog.setMessage(R.string.msg_confirmation);
		saveDialog.setPositiveButton(R.string.button_save,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						List<Question> data = getModel().getQuestions();
						Constants.LIST_QUESTIONS = data;
						saveCollectData(Constants.FORM_ID, data);
						Intent intent = new Intent(ControllerActivity.this, MenuActivity.class);
						startActivity(intent);
						finish();
						dialog.cancel();
					}
				});
		saveDialog.setNegativeButton(R.string.button_cancel,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return saveDialog.create();
	}

	private void saveCollectData(String formId, List<Question> answers) {
		//if (mUser == null) {
			// TODO: Dialog
	//		return;
		//}
		try {
		//	mUser.setFormId(formId);
			sqliteHelper = new MaritacaHelper(this);
			sqliteHelper.saveAnswers(answers);
		} catch (Exception e) {
			// TODO: Dialog:
		} finally {
			sqliteHelper.onClose();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(Constants.MODEL_PARCELABLE, getModel());
	}
	
	private void goBack() {
		if (getModel().hasPrevious()) {
			getModel().previous();
			viewer = new Viewer(this, getModel().getCurrentQuestion());
			setContentView(viewer.getView());
		} else {
			showDialog(Constants.CANCEL_QUESTION);
		}
	}

	@Override
	public void onBackPressed() {
		if(isFromListMode()){
			Intent intent = new Intent(ControllerActivity.this, QuestionsListActivity.class);
			intent.putExtra(Constants.USER_DATA, mUser);
			startActivity(intent);
			//finish();
		} else{
			goBack();
		}
	}

	public void previous() {
		goBack();
	}

	public void helpQuestion() {
		String help = getModel().getCurrentQuestion().getHelp();
		if (help == null || "".equals(help))
			Toast.makeText(this, R.string.msg_help, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(this, getModel().getCurrentQuestion().getHelp(),
					Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Shows a Dialog to cancel from Wizard Mode
	 * @return
	 */
	private Dialog showCancelQuestion() {
		AlertDialog.Builder cancelDialog = new AlertDialog.Builder(this);		
		cancelDialog.setTitle(R.string.label_confirmation);
		cancelDialog.setIcon(R.drawable.info);
		cancelDialog.setMessage(R.string.msg_form_cancel);		
		cancelDialog.setPositiveButton(R.string.button_yes,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(ControllerActivity.this, MenuActivity.class);
						intent.putExtra(Constants.USER_DATA, "");
						startActivity(intent);
						finish();
						dialog.cancel();
					}
				});
		cancelDialog.setNegativeButton(R.string.button_no,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return cancelDialog.create();
	}

	private class LoadView extends AsyncTask<Void, Integer, Void> {
		private InputStream inputStream;
		private Integer questionIdX;

		public LoadView(InputStream inputStream, Integer questionId) {
			super();
			this.inputStream	= inputStream;
			this.questionIdX	= questionId;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(ControllerActivity.this, getString(R.string.label_loading), getString(R.string.label_loading_form), false, false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				setModel(new Model(inputStream, getFilesDir().getPath(), questionIdX));
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			createViewTemp();
		}
	}
	
	private void createViewTemp() {
		boolean isListModeActive = isCollectDataListMode();
		if(isListModeActive){
		//	if(isFromListMode()){
					sqliteHelper = new MaritacaHelper(ControllerActivity.this);
				//	MaritacaAnswer answer = sqliteHelper.getAnswerByUserWithGroupNoValid(mUser.getId(), getModel().getCurrentIdQuestion());
					MaritacaAnswer answer = sqliteHelper.getAnswer(getModel().getCurrentIdQuestion());
					sqliteHelper.close();
					if(answer != null){
						if (getModel() != null && !getModel().isQuestionsEmpty()) {
							if(getModel().getCurrentQuestion().getComponentType().equals(ComponentType.DATE)){//barcode
								//getModel().getCurrentQuestion().setValue(answer.getValue());
							} else {
								getModel().getCurrentQuestion().setValue(answer.getValue());
							}
							
							viewer = new Viewer(this, getModel().getCurrentQuestion());
							setContentView(viewer.getView());
						}					
				} else {
					if (getModel() != null && !getModel().isQuestionsEmpty()) {
						viewer = new Viewer(this, getModel().getCurrentQuestion());
						setContentView(viewer.getView());
					}
				}
			//} else {
			//	Intent intent = new Intent(this, QuestionsListActivity.class);
			//	intent.putExtra(Constants.USER_DATA, mUser);
		//		startActivity(intent);
		//	}				
		} else {			
			if (getModel() != null && !getModel().isQuestionsEmpty()) {
				viewer = new Viewer(this, getModel().getCurrentQuestion());
				setContentView(viewer.getView());
			}
		}
	}	

	private void setFilePath(Intent intent, String type, String extension){
		String timeStamp = new SimpleDateFormat(Constants.DATE_FILE_FORMAT).format(new Date());
		String sourcePath = getRealPathFromURI(intent.getData());
		String filePath = Environment.getExternalStorageDirectory().getPath()+ File.separator + Constants.FILES_DIR + File.separator + type + timeStamp+extension;
		if(sourcePath != null){
			File source = new File(sourcePath);
			try {
				source.renameTo(new File(filePath));
			} catch (Exception e) {
				Toast.makeText(ControllerActivity.this, getString(R.string.error_saving_file), 	Toast.LENGTH_LONG).show();
			}
			getModel().getCurrentQuestion().setValue(filePath);
		} else {
			Toast.makeText(ControllerActivity.this, getString(R.string.error_saving_file), 	Toast.LENGTH_LONG).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		/*if(requestCode == Constants.PICTURE_REQUEST && resultCode == RESULT_OK){
			setFilePath(intent, "pic_", ".jpg");//It does not work for Android 2.1, because intent is null
		}
		else*/ if(requestCode == Constants.VIDEO_REQUEST && resultCode == RESULT_OK){
			setFilePath(intent, "video_", ".mp4");
		}
		else if (requestCode == Constants.AUDIO_REQUEST && resultCode == RESULT_OK) {
			setFilePath(intent, "audio_", ".3gpp");
		}		
		else if (requestCode == Constants.BARCODE_REQUEST && resultCode == RESULT_OK) {
			IntentResult scan = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			if (scan != null) {
				//barCodeCode = scan.getContents();
				//barCodeType = scan.getFormatName();
				BarCodeInformation info = new BarCodeInformation(scan.getFormatName(), scan.getContents());
				getModel().getCurrentQuestion().setValue(info);
			}
		}
		else if(requestCode == Constants.VOICE_RECOGNITION_REQUEST && resultCode == RESULT_OK){
			ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			setSuggestionsList(new ListView(this));
			getSuggestionsList().setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
		}
	}

	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		try {
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(columnIndex);
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * List Mode methods
	 */
	
	private void saveAnswerFromListMode(){
//		if (mUser == null) {
			// TODO: Dialog
	//		return;
		//}
		sqliteHelper = new MaritacaHelper(this);
		mUser.setFormId(Constants.FORM_ID);
		if(sqliteHelper.saveAnswerItem2(mUser, getModel().getCurrentQuestion(), getModel().getQuestions())){
			Toast.makeText(this, getString(R.string.msg_data_saved), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, getString(R.string.msg_data_not_saved), Toast.LENGTH_LONG).show();			
		}			
		sqliteHelper.onClose();
	}
	private void backFromAnswerInListMode(){
		Intent intent = new Intent(ControllerActivity.this, QuestionsListActivity.class);
		intent.putExtra(Constants.USER_DATA, mUser);
		startActivity(intent);
	}	
	private boolean isCollectDataListMode(){
		sqliteHelper = new MaritacaHelper(ControllerActivity.this);
		//boolean isListMode = sqliteHelper.isListTheCollectDataMode(mUser.getId());		
		sqliteHelper.close();
		return true;
	}
	private boolean answersCanBeSaved(List<Question> questions){
		sqliteHelper = new MaritacaHelper(ControllerActivity.this);
		List<MaritacaAnswer> answers = sqliteHelper.getAnswersByGroupNoValid(mUser);
		sqliteHelper.onClose();
		if(answers.isEmpty()){
			Toast.makeText(ControllerActivity.this, getString(R.string.label_insert_data), Toast.LENGTH_LONG).show();	
			return false;
		}
		for(Question q : questions){
			if(q.getRequired()){
				int index = answers.indexOf(new MaritacaAnswer(q.getId()));
				if(index == -1){
					Toast.makeText(ControllerActivity.this, "Question "+(q.getId()+1)+" is required.", Toast.LENGTH_LONG).show();
					return false;
				}
				if("".equals(answers.get(index).getValue().trim())){
					Toast.makeText(ControllerActivity.this, "Question "+(q.getId()+1)+" is required.", Toast.LENGTH_LONG).show();
					return false;
				}
			}
		}		
		return true;
	}
	
	private Dialog showSaveFromListQuestions() {
		AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
		saveDialog.setTitle(R.string.label_confirmation);
		saveDialog.setMessage(R.string.msg_confirmation);
		saveDialog.setPositiveButton(R.string.button_save,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(answersCanBeSaved(getModel().getQuestions())){
							sqliteHelper = new MaritacaHelper(ControllerActivity.this);
							if(sqliteHelper.updateAnswerGroup(mUser.getId())){
								Toast.makeText(ControllerActivity.this, getString(R.string.msg_questions_data_saved), Toast.LENGTH_LONG).show();							
							} else {
								Toast.makeText(ControllerActivity.this, getString(R.string.msg_questions_data_not_saved), Toast.LENGTH_LONG).show();
							}
							sqliteHelper.close();
							Intent intent = new Intent(ControllerActivity.this, MenuLoadFormActivity.class);
							intent.putExtra(Constants.USER_DATA, mUser);
							startActivity(intent);
							finish();
						}
						dialog.cancel();
					}
				});
		saveDialog.setNegativeButton(R.string.button_cancel,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return saveDialog.create();
	}
	private Dialog showCancelFromListQuestions() {
		AlertDialog.Builder cancelDialog = new AlertDialog.Builder(this);		
		cancelDialog.setTitle(R.string.label_confirmation);
		cancelDialog.setIcon(R.drawable.info);
		cancelDialog.setMessage(R.string.msg_form_cancel);		
		cancelDialog.setPositiveButton(R.string.button_yes,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(ControllerActivity.this, QuestionsListActivity.class);
						intent.putExtra(Constants.USER_DATA, mUser);
						startActivity(intent);
						finish();
						dialog.cancel();
					}
				});
		cancelDialog.setNegativeButton(R.string.button_no,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return cancelDialog.create();
	}

	/*** Model ***/
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}

	/*** Location Question ***/
	public LocationManager getLocationManager() {
		return locationManager;
	}
	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}
	public LocationListener getLocationListener() {
		return locationListener;
	}
	public void setLocationListener(LocationListener locationListener) {
		this.locationListener = locationListener;
	}	
	public void stopLocationListener() {
		if(locationManager != null && locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
	}
	
	/*** Text Question ***/
	public ListView getSuggestionsList() {
		return suggestionsList;
	}
	public void setSuggestionsList(ListView suggestionsList) {
		this.suggestionsList = suggestionsList;
	}

	/*** Picture Question ***/
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getRotatedBitmap() {
		return rotatedBitmap;
	}
	public void setRotatedBitmap(Bitmap rotatedBitmap) {
		this.rotatedBitmap = rotatedBitmap;
	}
	
	/*** ***/
	public boolean isFromListMode() {
		return isFromListMode;
	}
	public void setFromListMode(boolean isFromListMode) {
		this.isFromListMode = isFromListMode;
	}

	public Integer getQuestionId() {
		if(this.questionId == null)
			return 0;
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	
	
}