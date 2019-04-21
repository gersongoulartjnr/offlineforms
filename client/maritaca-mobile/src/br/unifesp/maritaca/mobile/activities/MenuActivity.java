package br.unifesp.maritaca.mobile.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import br.unifesp.maritaca.mobile.dataaccess.MaritacaHelper;
import br.unifesp.maritaca.mobile.exception.MaritacaExceptionHandler;

import com.actionbarsherlock.app.SherlockActivity;

public class MenuActivity extends SherlockActivity implements OnClickListener {

    private MaritacaHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(
                MenuActivity.this));
        this.initMenu();
        try {
            setContentView(R.layout.novomenucoleta);
        }  catch(Exception e){
        System.out.println(e.toString());
    }


        Button btnCollect = (Button) findViewById(R.id.btnCollect);
        btnCollect.setOnClickListener(this);

        Button btnSync = (Button) findViewById(R.id.btnSync);
        btnSync.setOnClickListener(this);

        Button btnReplace = (Button) findViewById(R.id.btnReplace);
        btnReplace.setOnClickListener(this);

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
            case R.id.btnCollect:
                intent = new Intent(this, ControllerActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSync:
                sendGS();
                //carregaGS();
                break;
            case R.id.btnReplace:
                intent = new Intent(this, MenuLoadFormActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void sendGS(){
        sqliteHelper = new MaritacaHelper(this);
        Object [] resp = sqliteHelper.getAnswersGSTeste();
        Intent intent = new Intent(this, SendResponses.class);
        intent.putExtra("r1", resp);
        startActivity(intent);
        limpaQuestions();
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
        finish();
    }

    @Override
    protected void onDestroy() {
        if (sqliteHelper != null) {
            sqliteHelper.onClose();
        }
        super.onDestroy();
    }



}