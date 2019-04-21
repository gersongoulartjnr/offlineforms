package br.unifesp.maritaca.mobile.activities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.smartam.leeloo.common.OAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;
import br.unifesp.maritaca.mobile.dataaccess.MaritacaHelper;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaGroup;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.maritaca.mobile.exception.MaritacaExceptionHandler;
import br.unifesp.maritaca.mobile.model.adaptors.Child;
import br.unifesp.maritaca.mobile.model.adaptors.Parent;
import br.unifesp.maritaca.mobile.model.adaptors.ResultsAdapter;
import br.unifesp.maritaca.mobile.thread.CheckServerUpThread;
import br.unifesp.maritaca.mobile.thread.RestRequestThread;
import br.unifesp.maritaca.mobile.util.Constants;
import br.unifesp.maritaca.mobile.util.HttpMethod;

import com.actionbarsherlock.app.SherlockActivity;

public class ResultsActivity extends SherlockActivity {
	
	private MaritacaHelper sqliteHelper;
    private MaritacaUser mUser;
    private ExpandableListView resultList;
    private ArrayList<Parent> results;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(ResultsActivity.this));
        initActivity();        
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
		finish();
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
    
    private void initActivity(){
    //	this.mUser = (MaritacaUser) getIntent().getSerializableExtra(Constants.USER_DATA);
       // if(mUser != null) {
        	ArrayList<Child> reportsList = new ArrayList<Child>();
        	ArrayList<Child> localAnswersGroupList = fillAnswersGroupList(mUser);
        	String reportsXml = retrieveReportsDataXml();
        	
        	if(reportsXml != null){
        		reportsList = getReportsData(reportsXml);
        	}
        	
        	////if(reportsList.isEmpty()){
        		////loadAnswersGathered();
        	////}
        	//
        	
        	resultList = (ExpandableListView)findViewById(R.id.expandable_list);
            resultList.setAdapter(new ResultsAdapter(ResultsActivity.this, setExpandableList(reportsList, localAnswersGroupList)));
            resultList.setOnGroupClickListener(new OnGroupClickListener() {
                public boolean onGroupClick(ExpandableListView arg0,View arg1, int groupPosition, long arg3) {
                	Parent p = (Parent)results.get(groupPosition);
                	if(p.getTitle().equals(getString(R.string.lbl_answers))){
                		loadAnswersGathered();
                	}            	
                	return false;
                }
            });
            resultList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                public boolean onChildClick(ExpandableListView parent,View v, int groupPosition, int childPosition, long id) {
                	Parent p = (Parent)results.get(groupPosition);
                	Child c = (Child)p.getChildren().get(childPosition);
                	if(p.getTitle().equals(getString(R.string.lbl_local_data))){
                		showCertainAnswer(c.getUrl());
                	}
                	else if(p.getTitle().equals(getString(R.string.lbl_reports))){
                		openWebViewForReports(c.getUrl());
                	}
                	return false;
                }
            });
       // }    	
    }    
    
    private void loadAnswersGathered(){
		Intent intent = new Intent(ResultsActivity.this, AnswersActivity.class);
		intent.putExtra(Constants.USER_DATA, mUser);
        startActivity(intent);
    }
    
    private void openWebViewForReports(String formUrl) {
    	Intent intent = new Intent(ResultsActivity.this, WebViewActivity.class);
    	intent.putExtra(Constants.USER_DATA, mUser);
    	intent.putExtra(Constants.FORM_URL_DATA, formUrl);
    	startActivity(intent);
	}
    
    private String retrieveReportsDataXml(){
    	String res = null;
    	
    	try {
    		if ((new CheckServerUpThread()).execute().get()) {
	    	String uri = Constants.URI_WS_LIST_REPORT + Constants.FORM_ID + "?" + OAuth.OAUTH_TOKEN+ "=" + mUser.getAccessToken();
	    	String token = mUser.getAccessToken(); 
	    		HttpResponse response = (HttpResponse)(new RestRequestThread()).execute(uri, token , HttpMethod.GET.toString(), "", "").get();
				HttpEntity   entity   = response.getEntity();
				
				if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
					res = EntityUtils.toString(entity);
				}
				/*String errorMessage = null;
				if (entity != null) {
					errorMessage = Utils.getMessageFromErrorResponse(EntityUtils.toString(entity));
				}
				throw new RuntimeException(errorMessage != null ? errorMessage : "");*/
    		}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			return null;
		}
    	return res;
    }
    
    private ArrayList<Child> getReportsData(String reportsXml){
    	ArrayList<Child> reportsList = new ArrayList<Child>();        
        try {
        	InputStream is = new ByteArrayInputStream(reportsXml.getBytes(Constants.M_ENCODING));
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document doc = dBuilder.parse(is);
        	doc.getDocumentElement().normalize();
        	NodeList nList = doc.getElementsByTagName("report");
        	for (int temp = 0; temp < nList.getLength(); temp++) { 
        		Node nNode = nList.item(temp); 
        		if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
        			Element eElement = (Element) nNode; 
        			Child reportData = new Child(eElement.getAttribute("reportName"), eElement.getAttribute("reportId"));
        			reportsList.add(reportData);
        		}
        	}
        	return reportsList;
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }        
    }
    
    private ArrayList<Parent> setExpandableList(ArrayList<Child> reportsList, ArrayList<Child> localAnswersGroupList){
    	results = new ArrayList<Parent>();
    	
    	Parent localData = new Parent();
    	localData.setTitle(getString(R.string.lbl_local_data));
    	localData.setChildren(localAnswersGroupList);
    	results.add(localData);
    	    	
        Parent answers = new Parent();
        answers.setTitle(getString(R.string.lbl_answers));
        answers.setChildren(new ArrayList<Child>());
        results.add(answers);
        
        Parent reports = new Parent();
        reports.setTitle(getString(R.string.lbl_reports));
        reports.setChildren(reportsList);
        results.add(reports);
        
        return results;
    }
    
    /**
     * 
     * @param mUser
     * @return
     */
    private ArrayList<Child> fillAnswersGroupList(MaritacaUser mUser){
    	sqliteHelper = new MaritacaHelper(this);
    	List<MaritacaGroup> groups = sqliteHelper.getAnswersGroupsByUser(mUser);
    	ArrayList<Child> children = new ArrayList<Child>(groups.size());
    	for(MaritacaGroup g : groups){
    		Date date = new Date(g.getTimestamp());
    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    		Child child = new Child(sdf.format(date), String.valueOf(g.getGroupId()));
    		children.add(child);
    	}    	
    	sqliteHelper.onClose();    	
    	return children;
    }
    
    
    private void showCertainAnswer(String answerGroupId){    	
    	Intent intent = new Intent(ResultsActivity.this, LocalDataActivity.class);    	
    	intent.putExtra(Constants.USER_DATA, mUser);    	
    	intent.putExtra(Constants.ANSWER_GROUP_ID, answerGroupId);
    	startActivity(intent);    	
    }    
}