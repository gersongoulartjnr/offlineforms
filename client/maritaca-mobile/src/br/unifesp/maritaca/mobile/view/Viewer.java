package br.unifesp.maritaca.mobile.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.unifesp.maritaca.mobile.activities.ControllerActivity;
import br.unifesp.maritaca.mobile.activities.R;
import br.unifesp.maritaca.mobile.model.Question;


public class Viewer {

	private ControllerActivity activity;
	private LinearLayout baseView;
	
	private LinearLayout bodyLayout;
	private RelativeLayout questionTop;
	private LinearLayout questionCenter;
	private RelativeLayout questionNext;
	private RelativeLayout questionPrevious;
	
	private View answer;
	private Question current;

	public Viewer(ControllerActivity maritacaActivity, Question current) {
		activity = maritacaActivity;
		this.current = current;

		baseView = new LinearLayout(activity);
		baseView.setOrientation(LinearLayout.VERTICAL);
		// In Android 2.2 : fill_parent is replaced by match_parent
		baseView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		questionTop = (RelativeLayout) inflater.inflate( R.layout.question, null );
		questionCenter = (LinearLayout) questionTop.findViewById(R.id.QuestionCenter);
		createProgressBarLayoutFromXML();
		createQuestionLayout();
		createAnswerLayout();
		baseView.addView(questionTop);
	}

	private void createProgressBarLayoutFromXML() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 10);
		ProgressBar pBarXML = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
		pBarXML.setLayoutParams(params);
		pBarXML.setMinimumHeight(10);
		pBarXML.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.progressbar_drawable));
		pBarXML.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progressbar_drawable));
		pBarXML.setProgress(activity.getModel().getProgress());
		questionTop.addView(pBarXML);
	}

	/**
	 * This method creates the question layout
	 */
	private void createQuestionLayout() {
		LinearLayout questionLayout = new LinearLayout(activity);
		questionLayout.setOrientation(LinearLayout.VERTICAL);
	
		//Parametros para margem
		LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        labelParams.setMargins(20, 3, 0, 0);
        LinearLayout.LayoutParams QuestionParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        QuestionParams.setMargins(20, 0, 20, 10);
		
		TextView tvLabelQuestion = new TextView(activity);
		tvLabelQuestion.setTypeface(null, Typeface.BOLD);
		tvLabelQuestion.setText((current.getId() + 1) + ": ");
		
		TextView tvQuestion = new TextView(activity);
		tvQuestion.setTypeface(null, Typeface.ITALIC);
		tvQuestion.setText(current.getLabel());
		
		//labelLayout.addView(tvLabelQuestion, labelParams);
		//descriptionLayout.addView(tvQuestion, QuestionParams);
		questionLayout.addView(tvLabelQuestion, labelParams);
		questionLayout.addView(tvQuestion, QuestionParams);
		
		questionCenter.addView(questionLayout);
	}	
	
	public void saveAnswer(){
		current.save(answer);
	}
	
	/**
	 * This method creates the answer layout that contains 
	 * the answer 
	 */
	private void createAnswerLayout(){
		LinearLayout answerLayout = new LinearLayout(activity);
		answerLayout.setOrientation(LinearLayout.HORIZONTAL);
		answerLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT));
		
		answer = current.getLayout(activity);
		
		answerLayout.addView(answer);		
		questionCenter.addView(answerLayout);
	}
	
	public LinearLayout getView(){
		return baseView;
	}
}