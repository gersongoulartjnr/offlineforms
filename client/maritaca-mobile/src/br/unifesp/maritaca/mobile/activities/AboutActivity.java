	package br.unifesp.maritaca.mobile.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.unifesp.maritaca.mobile.activities.SimpleGestureFilter.SimpleGestureListener;
import br.unifesp.maritaca.mobile.dataaccess.MaritacaHelper;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.maritaca.mobile.exception.MaritacaExceptionHandler;
import br.unifesp.maritaca.mobile.util.Constants;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

    public class AboutActivity extends SherlockActivity implements SimpleGestureListener,OnClickListener {

	private MaritacaUser mUser;
	private MaritacaHelper sqliteHelper;
	private Activity activity;
	private Context context;

    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;
    private static final String[] SCOPES = { "https://www.googleapis.com/auth/drive" };
    private static final String PREF_ACCOUNT_NAME = "accountName";


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(AboutActivity.this));
		//this.mUser = (MaritacaUser) getIntent().getSerializableExtra(Constants.USER_DATA);		
		setContentView(R.layout.about);
		TextView projectUrl = (TextView)findViewById(R.id.about_project_url);
		TextView txtIdForm = (TextView) findViewById(R.id.txtIdForm);
		Button btnLoadForm = (Button) findViewById(R.id.btnCarregaForm);
		Button btnLoadGS = (Button) findViewById(R.id.btnCarregaGS);
        btnLoadGS.setOnClickListener(this);
		btnLoadForm.setOnClickListener(this);
		projectUrl.setText(Constants.URI_PROJECT);
		projectUrl.setOnClickListener(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MenuLoadFormActivity.class);
			intent.putExtra(Constants.USER_DATA, "");
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onSwipe(int direction) {}

	@Override
	public void onDoubleTap() {}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.about_project_url:
			openSiteProject();
			break;
			case R.id.btnCarregaForm:
				loadForm();
				break;
			case R.id.btnCarregaGS:
				testeGS();
				break;
		}		
	}
	
	private void openSiteProject() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URI_PROJECT));
		startActivity(intent);
	}

	public void testeGS(){
		limpaQuestions();
		Intent intent = new Intent(this, AboutActivity2.class);
        startActivity(intent);
	}


			 
	public void loadForm(){
	  
		TextView txtIdForm = (TextView) findViewById(R.id.txtIdForm);
		
		try{
		String url = Constants.URL_WEB_SERVICE +  txtIdForm.getText().toString() + ".xml";
		downloadFile(url,Constants.PATH_FORM);
		Intent intent = new Intent(this, ControllerActivity.class);
		intent.putExtra(Constants.USER_DATA, "");
		startActivity(intent);

		}catch(Exception e){
			
		}
		
	}
	
	public void limpaQuestions(){
		
		sqliteHelper = new MaritacaHelper(this);
		sqliteHelper.deleteAnswers();
		
	}
	
	

    private void downloadFile(String urlDownload, String path) {  
    	
    	limpaQuestions();
    	if (android.os.Build.VERSION.SDK_INT > 9) 
    	{
    	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	    StrictMode.setThreadPolicy(policy);
    	
    	try{
        URL url = new URL(urlDownload);  
        HttpURLConnection c = (HttpURLConnection) url.openConnection();  
        c.setRequestMethod("GET");  
        c.setDoOutput(true);  
        c.connect();  
        FileOutputStream fos = new FileOutputStream(new File(path));  
  
        String command = "chmod 777 " + path;  
  
        Runtime.getRuntime().exec(command);  
//          
        InputStream is = c.getInputStream();  
  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while ((len = is.read(buffer)) != -1) {  
            fos.write(buffer, 0, len);  
        }  
        fos.close();  
        is.close();  
        
        c.disconnect();
        
    	}
    	catch(Exception e){
    		System.out.println(e.toString());
    	}
    	}
    	
    	
    	
    } 
    
    public String convertXMLFileToString(String fileName)
    {
      try{
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        InputStream inputStream = new FileInputStream(new File(fileName));
        org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
        StringWriter stw = new StringWriter();
        Transformer serializer = TransformerFactory.newInstance().newTransformer();
        serializer.transform(new DOMSource(doc), new StreamResult(stw));
        return stw.toString();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
        return null;
    }
    
   
    
    
}