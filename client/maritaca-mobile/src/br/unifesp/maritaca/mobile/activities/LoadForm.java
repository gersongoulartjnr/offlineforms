package br.unifesp.maritaca.mobile.activities;

import android.os.Bundle;
import android.app.Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import java.io.*;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.services.script.model.*;
import java.util.Map;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.unifesp.maritaca.mobile.dataaccess.MaritacaHelper;
import br.unifesp.maritaca.mobile.util.Constants;

public class LoadForm extends Activity implements View.OnClickListener{

    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button btnVoltar;
    ProgressDialog mProgress;
    Context context;
    String url;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { "https://www.googleapis.com/auth/drive.readonly","https://www.googleapis.com/auth/forms","https://www.googleapis.com/auth/script.external_request" };
    int cont=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        url = getIntent().getSerializableExtra("url").toString();


        setContentView(R.layout.activity_load_form);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        activityLayout.addView(mOutputText);

        btnVoltar = new Button(this);
        btnVoltar.setVisibility(View.INVISIBLE);
        btnVoltar.setText("Voltar");
        btnVoltar.setBackgroundColor(getResources().getColor(R.color.azul_offline_forms));
        btnVoltar.setTextColor(getResources().getColor(R.color.white));
        activityLayout.addView(btnVoltar);

        btnVoltar.setOnClickListener(this);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Carregando formulário");

        setContentView(activityLayout);

        // Initialize credentials and service object.
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());


    }

    public void onClick(View view) {
        if(btnVoltar.isPressed()){
            String path = Constants.PATH_FORM;
            File myFile = new File(path);
            if (myFile.exists()) {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, MenuLoadFormActivity.class);
                startActivity(intent);
            }
        }
    }


    private static HttpRequestInitializer setHttpTimeout(
            final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest)
                    throws java.io.IOException {
                requestInitializer.initialize(httpRequest);
                // This allows the API to call (and avoid timing out on)
                // functions that take up to 6 minutes to complete (the maximum
                // allowed script run time), plus a little overhead.
                httpRequest.setReadTimeout(380000);
            }
        };
    }


    /**
     * Called whenever this activity is pushed to the foreground, such as after
     * a call to onCreate().
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(cont==0) {
            if (isGooglePlayServicesAvailable()) {
                refreshResults();
            } else {
                mOutputText.setText("Google Play Services required: " +
                        "after installing, close and relaunch this app.");
            }
        }else if(cont==1){
            mOutputText.setText("Requisição cancelada.");
        }
    }



    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    mOutputText.setText("Account unspecified.");
                    cont=1;
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }


        // super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshResults() {
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                    new MakeRequestTask(mCredential).execute();

            } else {
                mOutputText.setText("Sem conexão com a Internet");
            }
        }
    }


    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                LoadForm.this,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }





    //////////////////////////////////////////////


    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.script.Script mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.script.Script.Builder(
                    transport, jsonFactory, setHttpTimeout(credential))
                    .setApplicationName("Google Apps Script Execution API Android Quickstart")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }


        private List<String> getDataFromApi()
                throws IOException, GoogleAuthException {

                String scriptId = "MqppBDJm7aTKWjN41OXCUbZxLbWWIB-1a";

                List<String> folderList = new ArrayList<String>();

                List<Object> listParameters = new ArrayList<Object>();

                listParameters.add(url);

                // Create an execution request object.
                ExecutionRequest request = new ExecutionRequest()
                        .setFunction("setXML")
                        .setParameters(listParameters);

                // Make the request.
                Operation op =
                        mService.scripts().run(scriptId, request).execute();

                // Print results of request.
                if (op.getError() != null) {
                    throw new IOException(getScriptError(op));
                }
                if (op.getResponse() != null &&
                        op.getResponse().get("result") != null) {

                    folderList.add(op.getResponse().get("result").toString());
                    String xml = op.getResponse().get("result").toString();

                    String path = Constants.PATH_FORM;
                    File myFile = new File(path);
                    PrintWriter textFileWriter = new PrintWriter(new FileWriter(myFile));
                    textFileWriter.write(xml);
                    textFileWriter.close();

                }

                return folderList;
        }



        /**
         * Interpret an error response returned by the API and return a String
         * summary.
         *
         * @param op the Operation returning an error response
         * @return summary of error response, or null if Operation returned no
         *     error
         */
        private String getScriptError(Operation op) {
            if (op.getError() == null) {
                return null;
            }

            // Extract the first (and only) set of error details and cast as a Map.
            // The values of this map are the script's 'errorMessage' and
            // 'errorType', and an array of stack trace elements (which also need to
            // be cast as Maps).
            Map<String, Object> detail = op.getError().getDetails().get(0);
            List<Map<String, Object>> stacktrace =
                    (List<Map<String, Object>>)detail.get("scriptStackTraceElements");

            java.lang.StringBuilder sb =
                    new StringBuilder("\nScript error message: ");
            sb.append(detail.get("errorMessage"));

            if (stacktrace != null) {
                // There may not be a stacktrace if the script didn't start
                // executing.
                sb.append("\nScript error stacktrace:");
                for (Map<String, Object> elem : stacktrace) {
                    sb.append("\n  ");
                    sb.append(elem.get("function"));
                    sb.append(":");
                    sb.append(elem.get("lineNumber"));
                }
            }
            sb.append("\n");
            return sb.toString();
        }


        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                mOutputText.setText("Sem resultados retornados.");
                for(int i=0; i<5; i++) {
                    try {
                        Thread.sleep(50000);
                    } catch (InterruptedException e) {
                        btnVoltar.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(context, MenuLoadFormActivity.class);
                startActivity(intent);
                finish();

            } else {
                //  output.add(0, "Formulário importado com sucesso!");
                //  mOutputText.setText(TextUtils.join("\n", output));
                mOutputText.setText("Formulário importado com sucesso");
                    Intent intent = new Intent(context, MenuActivity.class);
                    startActivity(intent);
                finish();

            }
        }


        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            LoadForm.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("Erro:\n"
                            + mLastError.getClass() + " " + mLastError.getStackTrace().toString() + " " + mLastError.getMessage() + mLastError.getLocalizedMessage() + " " + mLastError.getCause().toString());
                }
            } else {
                mOutputText.setText("Requisição cancelada");
            }
        }


    }



}
