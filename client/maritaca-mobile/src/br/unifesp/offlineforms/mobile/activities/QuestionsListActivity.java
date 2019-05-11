package br.unifesp.offlineforms.mobile.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaAnswer;
import br.unifesp.offlineforms.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.offlineforms.mobile.exception.MaritacaException;
import br.unifesp.offlineforms.mobile.exception.MaritacaExceptionHandler;
import br.unifesp.offlineforms.mobile.model.Form;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.util.Constants;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class QuestionsListActivity extends SherlockListActivity {
	
	private MaritacaUser mUser;
	private List<Question> questions;
	
	private MaritacaHelper sqliteHelper;
	private Form form;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{		
			Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(QuestionsListActivity.this));
			getListView().setBackgroundResource(R.drawable.mobile_background);			
			this.mUser = (MaritacaUser) getIntent().getSerializableExtra(Constants.USER_DATA);
			if (mUser != null) {
				File file = new File(getFilesDir().getPath(), Constants.FORM_BIN);
				if(file.isFile()){
					this.form = (Form)loadClassFile(file);
					this.questions = form.getQuestions().getQuestions();
					if(!this.questions.isEmpty()) {
						setListAdapter(new QuestionsListAdapter(QuestionsListActivity.this, R.layout.row_question, getQuestions()));
					}
				}
			}
		} catch(Exception e){
			//TODO: Dialog
			throw new MaritacaException(e.getMessage());
		}		
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(QuestionsListActivity.this, MenuLoadFormActivity.class);
		intent.putExtra(Constants.USER_DATA, mUser);
		startActivity(intent);
		//finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.list_questions_menu, menu);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
		case R.id.list_questions_btn_back:
			showDialog(Constants.BACK_LIST_QUESTIONS);
			return true;
		case R.id.list_questions_btn_save:
			showDialog(Constants.SAVE_LIST_QUESTIONS);
			return true;
		case R.id.list_questions_btn_cancel:
			showDialog(Constants.CANCEL_LIST_QUESTIONS);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case Constants.BACK_LIST_QUESTIONS:
			dialog = showBackFromListQuestions();
			break;
		case Constants.SAVE_LIST_QUESTIONS:
			dialog = showSaveFromListQuestions();
			break;
		case Constants.CANCEL_LIST_QUESTIONS:
			dialog = showCancelFromListQuestions();
			break;
		default:
			dialog = null;
			break;
		}
		return dialog;
	}
	
	private Dialog showSaveFromListQuestions() {
		AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
		saveDialog.setTitle(R.string.label_confirmation);
		saveDialog.setMessage(R.string.msg_confirmation);
		saveDialog.setPositiveButton(R.string.button_save,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(answersCanBeSaved(getQuestions())){
							sqliteHelper = new MaritacaHelper(QuestionsListActivity.this);
							if(sqliteHelper.updateAnswerGroup(mUser.getId())){
								Toast.makeText(QuestionsListActivity.this, getString(R.string.msg_questions_data_saved), Toast.LENGTH_LONG).show();							
							} else {
								Toast.makeText(QuestionsListActivity.this, getString(R.string.msg_questions_data_not_saved), Toast.LENGTH_LONG).show();
							}
							sqliteHelper.close();
							Intent intent = new Intent(QuestionsListActivity.this, MenuLoadFormActivity.class);
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
	
	private boolean answersCanBeSaved(List<Question> questions){
		sqliteHelper = new MaritacaHelper(QuestionsListActivity.this);
		List<MaritacaAnswer> answers = sqliteHelper.getAnswersByGroupNoValid(mUser);
		sqliteHelper.onClose();
		if(answers.isEmpty()){
			Toast.makeText(QuestionsListActivity.this, getString(R.string.label_insert_data), Toast.LENGTH_LONG).show();	
			return false;
		}
		for(Question q : questions){
			if(q.getRequired()){
				int index = answers.indexOf(new MaritacaAnswer(q.getId()));
				if(index == -1){
					Toast.makeText(QuestionsListActivity.this, "Question "+(q.getId()+1)+" is required.", Toast.LENGTH_LONG).show();
					return false;
				}
				if("".equals(answers.get(index).getValue().trim())){
					Toast.makeText(QuestionsListActivity.this, "Question "+(q.getId()+1)+" is required.", Toast.LENGTH_LONG).show();
					return false;
				}
			}
		}		
		return true;
	}
	
	private Dialog showBackFromListQuestions() {
		AlertDialog.Builder cancelDialog = new AlertDialog.Builder(this);		
		cancelDialog.setTitle(R.string.label_confirmation);
		cancelDialog.setIcon(R.drawable.info);
		cancelDialog.setMessage(R.string.msg_back_to_menu);		
		cancelDialog.setPositiveButton(R.string.button_yes,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(QuestionsListActivity.this, MenuLoadFormActivity.class);
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
	
	private Dialog showCancelFromListQuestions() {
		AlertDialog.Builder cancelDialog = new AlertDialog.Builder(this);		
		cancelDialog.setTitle(R.string.label_confirmation);
		cancelDialog.setIcon(R.drawable.info);
		cancelDialog.setMessage(R.string.msg_form_cancel);		
		cancelDialog.setPositiveButton(R.string.button_yes,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						deleteGroupNoValidAndAnswers();				
						Intent intent = new Intent(QuestionsListActivity.this, MenuLoadFormActivity.class);
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
	private void deleteGroupNoValidAndAnswers(){
		sqliteHelper = new MaritacaHelper(QuestionsListActivity.this);
		sqliteHelper.deleteGroupNoValidAndAnswers(mUser.getId());
		sqliteHelper.close();
	}
	
	private Object loadClassFile(File f){
	    try{
	    	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
	    	Object o = ois.readObject();
	    	return o;
	    }
	    catch(Exception ex){
	    	Log.v("Error", ex.getMessage());
	    	ex.printStackTrace();
	    }
	    return null;
	}	
	
	public class QuestionsListAdapter extends ArrayAdapter<Question>{
		
		public QuestionsListAdapter(Context context, int resourceId, List<Question> questions) {
			super(context, resourceId, questions);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater=getLayoutInflater();
			View row = inflater.inflate(R.layout.row_question, parent, false);
			
			int index = position + 1;
			TextView id = (TextView)row.findViewById(R.id.idRowQuestion);
			id.setText(String.valueOf(index));
			if(getQuestions().get(position).getRequired()){
				id.setTextColor(getResources().getColor(R.color.red));
			}
			
			TextView label = (TextView)row.findViewById(R.id.textRowQuestion);
			label.setText(getQuestions().get(position).getLabel());
			
			//ImageView icon=(ImageView)row.findViewById(R.id.iconRowQuestion);
			//icon.setImageResource(R.drawable.error);
			
			return row;
		}		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//Question question = (Question) getListAdapter().getItem(position);
		Intent intent = new Intent(this, ControllerActivity.class);
		intent.putExtra(Constants.USER_DATA, mUser);
		intent.putExtra(Constants.IS_LIST_MODE_ACTIVE, true);
		intent.putExtra(Constants.QUESTION_ID, position);
		startActivity(intent);	    
	}

	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}