package my.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import my.cart.R;
import my.cart.common.ConnectAsynchronously;
import my.cart.common.Prefrences;
import my.cart.common.Utility;

/**
 * Created by shahid Akhtar on 10-02-2017.
 */

public class VerificationActivity extends AppCompatActivity {
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_activity);

        /*get phone number from previous activity*/
        Intent data = getIntent();
        phoneNumber = data.getStringExtra(Utility.PHONE_NUMBER);
        if (!TextUtils.isEmpty(phoneNumber)) {
            ((TextView) findViewById(R.id.phone_number)).setText(phoneNumber);
        }
    }

    private void isClickable(boolean isClickable){
        findViewById(R.id.editNumberBtn).setClickable(isClickable);
        findViewById(R.id.verifyBtn).setClickable(isClickable);
        findViewById(R.id.reSendBtn).setClickable(isClickable);
    }


    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.editNumberBtn:
                finish();
                break;

            case R.id.verifyBtn:
                EditText codeEdiText = (EditText) findViewById(R.id.codeEdiText);
                String code = codeEdiText.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    codeEdiText.setError(getResources().getString(R.string.input_field_not_empty));
                    codeEdiText.requestFocus();
                } else {
                    isClickable(false);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("phone", phoneNumber);
                        jsonObject.put("code", code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new MyAsynkTask(jsonObject, this, "VERIFY").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Utility.BASE_URL +"verification/");
                }
                break;

            case R.id.reSendBtn:
                isClickable(false);
                JSONObject jsonObject =new JSONObject();
                try {
                    jsonObject.put("phone",phoneNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new MyAsynkTask(jsonObject,this,"REGISTRARION").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Utility.BASE_URL+"registration/");
                break;
        }
    }

    private void navigateToCart() {
        Intent next_activity = new Intent(VerificationActivity.this, CartsActivity.class);
        next_activity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(next_activity);
    }

    class MyAsynkTask extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        private Context mContext;
        private String taskIdetifier;
        private ProgressBar progressBar;

        public MyAsynkTask(JSONObject jsonObject, Context mContext, String taskIdetifier) {
            this.jsonObject = jsonObject;
            this.mContext = mContext;
            this.taskIdetifier = taskIdetifier;
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return ConnectAsynchronously.connectAsynchronously(strings[0], jsonObject, mContext);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int requestCode = ConnectAsynchronously.getRequestCode();
            String message = ConnectAsynchronously.getRequestMessage();
            isClickable(true);
            if (requestCode == HttpURLConnection.HTTP_OK || requestCode == HttpURLConnection.HTTP_CREATED) {
                if (taskIdetifier.equals("VERIFY")) {
                    try {
                        Prefrences.setUserID(VerificationActivity.this,new JSONObject(s).getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    navigateToCart();
                }

            }else if(requestCode==HttpURLConnection.HTTP_BAD_REQUEST){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.code_not_matched), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);

        }
    }

}
