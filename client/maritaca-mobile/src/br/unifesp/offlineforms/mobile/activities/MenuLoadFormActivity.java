package br.unifesp.offlineforms.mobile.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;



import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.exception.MaritacaExceptionHandler;

import com.actionbarsherlock.app.SherlockActivity;

import org.w3c.dom.Text;

public class MenuLoadFormActivity extends SherlockActivity implements OnClickListener {

    private MaritacaHelper sqliteHelper;

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
                MenuLoadFormActivity.this));
        this.initMenu();
        setContentView(R.layout.novomenu);

        /*
        Button btnCarregaGS = (Button) findViewById(R.id.btnCarregaGS);
        btnCarregaGS.setEnabled(false);
        btnCarregaGS.setBackgroundColor(Color.GRAY);
        btnCarregaGS.setOnClickListener(this);
        */

        Button btnCarregaGSDrive = (Button) findViewById(R.id.btnCarregaGSDrive);
        btnCarregaGSDrive.setOnClickListener(this);

        /*
        EditText txtIdForm = (EditText) findViewById(R.id.txtIdForm);

        txtIdForm.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                KeyEvent x2 = event;
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    carregaGS();
                }

            //    int t=actionId;

                // TODO Auto-generated method stub
            //    if (actionId == EditorInfo.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ) {
             //       carregaGS();
              //  }
                return false;
            }
        });
        */


    }

    public void onBackPressed() {
        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        //	finish();
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
           /* case R.id.btnCarregaGS:
                carregaGS();
                break;*/
            case R.id.btnCarregaGSDrive:
                carregaGSDrive();
                break;
        }
    }

    /*
    public void carregaGS(){
        limpaQuestions();
        TextView txtIdForm = (TextView) findViewById(R.id.txtIdForm);
        String url = txtIdForm.getText().toString();
        Intent intent = new Intent(this, LoadForm.class);
        intent.putExtra("url",url);
        sqliteHelper.insertURL(url);
        startActivity(intent);
    }
    */
    public void carregaGSDrive(){
        limpaQuestions();
        Intent intent = new Intent(this, LoadFormDrive.class);
        startActivity(intent);
        //finish();
    }

    public void limpaQuestions(){
        sqliteHelper = new MaritacaHelper(this);
        sqliteHelper.deleteAnswers();

    }


    private void initMenu() {
        sqliteHelper = new MaritacaHelper(this);
        setContentView(R.layout.novomenu);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    @Override
    protected void onDestroy() {
        if (sqliteHelper != null) {
            sqliteHelper.onClose();
        }
        super.onDestroy();
    }



}