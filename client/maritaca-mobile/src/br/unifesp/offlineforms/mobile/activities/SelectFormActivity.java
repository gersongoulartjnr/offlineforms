package br.unifesp.offlineforms.mobile.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import java.util.ArrayList;
import java.util.List;

import br.unifesp.offlineforms.mobile.dataaccess.MaritacaHelper;
import br.unifesp.offlineforms.mobile.exception.MaritacaExceptionHandler;

public class SelectFormActivity extends SherlockActivity  {

    private MaritacaHelper sqliteHelper;
    ArrayList<String> listaNomeForms = new ArrayList<String>();
    ArrayList<String> listaUrlForms = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new MaritacaExceptionHandler(
                SelectFormActivity.this));
        this.initMenu();
        try {
            setContentView(R.layout.activity_select_form);
        }  catch(Exception e){
        System.out.println(e.toString());
    }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        listaNomeForms = (ArrayList<String>) getIntent().getSerializableExtra("nomeForms");
        listaUrlForms =  (ArrayList<String>) getIntent().getSerializableExtra("urlForms");

        RelativeLayout rl=(RelativeLayout)findViewById(R.id.rl);

        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT));
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        //final RadioGroup radioGroupForms = new RadioGroup(this);

        for(int i = 0; i < listaUrlForms.size(); i++)
        {
            RadioButton b = new RadioButton(this);
            b.setText(listaNomeForms.get(i));
            b.setId(i);
            b.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    setNomeForm(v.getId());
                    carregaForm(v.getId());
                }
            });

            ll.addView(b);
        }

     /*   radioGroupForms.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton)findViewById(checkedId);
                int index = radioGroupForms.indexOfChild(rb);
                for(int i=0;i<listaUrlForms.size();i++){
                    //verifica o indice e pega a URL
                }
            }
        });*/

        rl.addView(sv);


    }

    private void initMenu() {
        sqliteHelper = new MaritacaHelper(this);
        setContentView(R.layout.novomenu);
    }

    public void onBackPressed() {
        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        //	finish();
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



    public void carregaForm(int index){
        limpaQuestions();
        String url = listaUrlForms.get(index).toString();
        Intent intent = new Intent(this, LoadForm.class);
        url = url.substring(0,url.length()-13);
        intent.putExtra("url",url);
        sqliteHelper.insertURL(url);
        startActivity(intent);
    }


    public void limpaQuestions(){
        sqliteHelper = new MaritacaHelper(this);
        sqliteHelper.deleteAnswers();

    }

    public void setNomeForm(int index){
        sqliteHelper = new MaritacaHelper(this);
        sqliteHelper.insertNameForm(listaNomeForms.get(index).toString());
    }



}