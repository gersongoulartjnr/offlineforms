package br.unifesp.offlineforms.mobile.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import br.unifesp.offlineforms.mobile.util.Constants;
import br.unifesp.offlineforms.mobile.util.XMLFormParser;

/**
 * @author Arlindo
 */
public class Model implements Parcelable {

	private Integer currentIdQuestion;	
	private List<Question> questions;
	
	public Model(InputStream is, String filesDirPath, Integer currentIdQuestion) {
		XMLFormParser xmlParser;
		try {
			this.currentIdQuestion 	= currentIdQuestion;
			File file = new File(filesDirPath, Constants.FORM_BIN);
		//	if(file.isFile()){
			//	Form form = (Form)loadClassFile(file);
			//	this.questions = form.getQuestions().getQuestions();
		//	}
			//else{
				xmlParser = new XMLFormParser(is);
				this.setQuestions(xmlParser.getForm().getQuestions().getQuestions());
				List<Question> data = xmlParser.getForm().getQuestions().getQuestions();
				Constants.LIST_QUESTIONS = data;
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(filesDirPath, Constants.FORM_BIN)));
				oos.writeObject(xmlParser.getForm());
				oos.flush();
				oos.close();
			//}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());		
		}
	}
	
	public Model(List<Question> questions) {
		super();
		this.questions = questions;
	}

	public int getProgress() {
		int idQuestion = currentIdQuestion;
		if(idQuestion == 0) {
			return 0;
		}
		return (currentIdQuestion*100)/getQuestions().size();
	}
	
	public Question getCurrentQuestion(){
		return getQuestions().get(currentIdQuestion);
	}
	
	public boolean next(){
		int currentId = currentIdQuestion;		
		int nextId = getQuestions().get(currentIdQuestion).getNext();
		
		if (nextId == -1){
			return false;
		}
		else{
			currentIdQuestion = nextId;
			getQuestions().get(currentIdQuestion).setPrevious(currentId);
			return true;
		}
	}
	
	public void previous(){
		currentIdQuestion = getQuestions().get(currentIdQuestion).getPrevious();
	}
	
	public boolean validate(){
		return getQuestions().get(currentIdQuestion).validate();
	}

	public boolean hasPrevious() {
		return getQuestions().get(currentIdQuestion).getPrevious() != null ?  true : false;
	}

	public boolean isQuestionsEmpty() {		
		return getQuestions() != null ? (getQuestions().size() > 0 ? false : true) : true;
	}
	
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
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

	public Integer getCurrentIdQuestion() {
		return currentIdQuestion;
	}
	public void setCurrentIdQuestion(Integer currentIdQuestion) {
		this.currentIdQuestion = currentIdQuestion;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {}
}