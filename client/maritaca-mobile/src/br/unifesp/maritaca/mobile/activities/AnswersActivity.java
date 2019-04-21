package br.unifesp.maritaca.mobile.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.smartam.leeloo.common.OAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.maritaca.mobile.exception.MaritacaExceptionHandler;
import br.unifesp.maritaca.mobile.model.Question;
import br.unifesp.maritaca.mobile.util.Constants;
import br.unifesp.maritaca.mobile.util.XMLAnswerListParser;
import br.unifesp.maritaca.mobile.util.XMLFormParser;

import com.actionbarsherlock.app.SherlockActivity;

public class AnswersActivity extends SherlockActivity {

	private MaritacaUser mUser;
	private ProgressDialog progressDialog;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(AnswersActivity.this));
		this.mUser = (MaritacaUser) getIntent().getSerializableExtra(Constants.USER_DATA);
		new LoadView().execute();
    }
	
	private class LoadView extends AsyncTask<Void, Integer, Void> {
		String[] questions = null;
		String[][] questionAnswers = null;
		String[] users = null;

		public LoadView() {}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(AnswersActivity.this, getString(R.string.label_loading), getString(R.string.label_loading_answers), false, false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				String uri = Constants.URI_WS_LIST_ANSWER + Constants.FORM_ID + "?" + OAuth.OAUTH_TOKEN+ "=" + mUser.getAccessToken();
		    	try{
		    		HttpGet httpGet = new HttpGet(uri);
		    		httpGet.getParams().setParameter(OAuth.OAUTH_TOKEN, mUser.getAccessToken());
		    		DefaultHttpClient httpClient = new DefaultHttpClient();
		    		HttpResponse response = httpClient.execute(httpGet);
		    		HttpEntity   entity   = response.getEntity();
		    		if(response.getStatusLine().getStatusCode() == 200 && entity != null) {
		    			String answersXml = EntityUtils.toString(entity);
						if(answersXml != null){							
							// TODO get questions from answer xml
					        XMLFormParser xmlParser  = parseFormXml();
					        questions  = fetchQuestionsList(xmlParser).toArray(new String[0]);
					        XMLAnswerListParser answerListParser = new XMLAnswerListParser();
					        answerListParser.parse(answersXml);					        
					        questionAnswers = answerListParser.getQuestionAnswers();
					        users = answerListParser.getUsers();   
						}
					}
		    		progressDialog.dismiss();
		    	} catch(Exception e) {
		    		Log.e("Error", e.getMessage());
		    	}		    	
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			if(questions != null && questions.length > 0 && 
					questionAnswers != null && questionAnswers.length > 0 && 
					users != null && users.length > 0){
				buildingTable(questions, questionAnswers, users, AnswersActivity.this);
			}
			else {
				buildingEmptyTable(AnswersActivity.this);
			}
		}
	}
    
    @Override
    protected void onPause() {
    	super.onPause();
		finish();
    }
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	private List<String> fetchQuestionsList(XMLFormParser xmlParser){			
		List<String> questions = new ArrayList<String>();
		for(Question q : xmlParser.getForm().getQuestions().getQuestions()){
			questions.add(q.getLabel());
		}
		return questions;
    }
    
	//TODO Isolate the opening of the form file in a method elsewhere
    private XMLFormParser parseFormXml(){
		InputStream is;
		try {
			is = getResources().getAssets().open(Constants.FORM_FILENAME);
			return new XMLFormParser(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		    	
    }
    
    private void buildingEmptyTable(Context controller){
    	setContentView(R.layout.answers);
    	TableLayout tableLayout = (TableLayout) findViewById(R.id.answers_table);
    	TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.setMargins(2, 2, 2, 2);
        TableRow row = new TableRow(controller);
        TextView text = new TextView(controller);
        text.setText(getString(R.string.lbl_no_results));        
        row.addView(text);        
        tableLayout.addView(row);
    }
    
    private void buildingTable(String[] questions, String[][] answers,String[] users, Context controller){
    	//Creating scrollview and table layouts    	 
    	setContentView(R.layout.answers);
    	TableLayout tableLayout = (TableLayout) findViewById(R.id.answers_table);
    	
    	//Creating and configuring TableRows 
    	//- Configuring
    	TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.setMargins(2, 2, 2, 2);
        //- Creating
        //	First Row:
        tableLayout.addView(buildingFirstRow(questions, answers, layoutParams, controller));
        //	Rest of the rows:
        buildingRemainingRows(questions, answers, layoutParams, controller, tableLayout, users);        
    }
    
    private TableRow buildingFirstRow(String[] questions, String[][] answers,TableRow.LayoutParams layoutParams, Context controller){
    	TableRow row1 = new TableRow(controller);
    	row1.setBackgroundColor(Color.LTGRAY);
    	row1.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	
        TextView userText = new TextView(controller);
        userText.setText("User");
        userText.setTextColor(Color.WHITE);
        userText.setBackgroundColor(Color.DKGRAY);
        row1.addView(userText,layoutParams);
        
        for(int i=0; i<questions.length;i++){
        	TextView questionText = new TextView(controller);
        	questionText.setText(questions[i]);
        	questionText.setTextColor(Color.WHITE);
        	questionText.setBackgroundColor(Color.DKGRAY);
        	row1.addView(questionText,layoutParams);
        }
        return row1;
    }
    
    private void buildingRemainingRows(String[] questions, String[][] answers,TableRow.LayoutParams layoutParams, Context controller,TableLayout tableLayout, String[] users){
    	for(int i =0;i<answers.length;i++){
    		TableRow dynamicRow = new TableRow(controller);
    		dynamicRow.setBackgroundColor(Color.LTGRAY);
    		dynamicRow.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		
    		TextView userName = new TextView(controller);
    		userName.setText(users[i]);
    		userName.setTextColor(Color.WHITE);
        	userName.setBackgroundColor(Color.DKGRAY);
        	dynamicRow.addView(userName,layoutParams);
        	
        	for(int j=0;j<answers[i].length;j++){
        		TextView userAnswer = new TextView(controller);
        		userAnswer.setText(answers[i][j]);
        		userAnswer.setTextColor(Color.BLACK);
        		userAnswer.setBackgroundColor(Color.GRAY);
            	dynamicRow.addView(userAnswer,layoutParams);
        	}
        	tableLayout.addView(dynamicRow);
    	}    	
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MenuLoadFormActivity.class);
			intent.putExtra(Constants.USER_DATA, mUser);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}