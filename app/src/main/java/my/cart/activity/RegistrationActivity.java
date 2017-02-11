package my.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
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

public class RegistrationActivity extends AppCompatActivity {
    private TextInputEditText countryEditText, mobileEditText;
    String input_field_not_empty;
    private boolean isValidCountryCode, isValidPhoneNumber;
    private String countryCode, phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activty);
        input_field_not_empty=getResources().getString(R.string.input_field_not_empty);
        countryEditText =(TextInputEditText)findViewById(R.id.countryEditText);
        mobileEditText =(TextInputEditText)findViewById(R.id.mobileEditText);

        countryEditText.addTextChangedListener(new MyTextWatcher(countryEditText));
        mobileEditText.addTextChangedListener(new MyTextWatcher(mobileEditText));
    }

    public void doRegistration(View view){
        countryCode =countryEditText.getText().toString().trim();
        phoneNumber =mobileEditText.getText().toString().trim();

        if(!isValidCountryCode){
            validateCoutryCode();

        }else if(!isValidPhoneNumber){
            validatePhoneNumber();

        } else {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            JSONObject jsonObject =new JSONObject();
            try {
                jsonObject.put("phone",countryCode+phoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            findViewById(R.id.registerBtn).setClickable(false);
            new RegistrationAsynkTask(jsonObject,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Utility.BASE_URL+"registration/");
        }

    }
    private void navigateToHomePage(String phoneNumber){
        Intent next_activity =new Intent(RegistrationActivity.this,VerificationActivity.class);
        next_activity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        next_activity.putExtra(Utility.PHONE_NUMBER,phoneNumber);
        startActivity(next_activity);
    }


    class RegistrationAsynkTask extends AsyncTask<String, Void,String>{
        private JSONObject jsonObject;
        private Context mContext;
        private ProgressBar progressBar;

        public RegistrationAsynkTask(JSONObject jsonObject, Context mContext) {
            this.jsonObject = jsonObject;
            this.mContext = mContext;
            progressBar =(ProgressBar)findViewById(R.id.progressBar);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return ConnectAsynchronously.connectAsynchronously(strings[0],jsonObject,mContext);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int requestCode =ConnectAsynchronously.getRequestCode();
            findViewById(R.id.registerBtn).setClickable(true);
            String message =ConnectAsynchronously.getRequestMessage();
            if(requestCode== HttpURLConnection.HTTP_OK || requestCode== HttpURLConnection.HTTP_CREATED){
                navigateToHomePage(countryCode+phoneNumber);

            }else {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);

        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.countryEditText:
                    validateCoutryCode();
                    break;
                case R.id.mobileEditText:
                    validatePhoneNumber();
                    break;

            }
        }
    }

    private boolean validateCoutryCode() {
        String countryCode =countryEditText.getText().toString().trim();
        TextInputLayout  countryInputLayout = (TextInputLayout)findViewById(R.id.countryInputLayout);

        if(TextUtils.isEmpty(countryCode)){
            countryInputLayout.setError(input_field_not_empty);
            countryEditText.requestFocus();
            isValidCountryCode=false;
        } else {
            countryInputLayout.setError(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                countryEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent,getTheme()), PorterDuff.Mode.SRC_ATOP);
            }else {
                //noinspection deprecation
                countryEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
            isValidCountryCode=true;
        }
        return isValidCountryCode;
    }


    private boolean validatePhoneNumber() {
        String phoneNumber =mobileEditText.getText().toString().trim();
        TextInputLayout mobileInputLayout = (TextInputLayout)findViewById(R.id.mobileInputLayout);

        if(TextUtils.isEmpty(phoneNumber)){
            mobileInputLayout.setError(input_field_not_empty);
            mobileEditText.requestFocus();
            isValidPhoneNumber=false;

        }else if(!Utility.isValidMobile(phoneNumber) || phoneNumber.length()<7 || phoneNumber.length()>12){
            mobileInputLayout.setError("Invalid phone number.");
            mobileEditText.requestFocus();
            isValidPhoneNumber=false;

        }else {
            mobileInputLayout.setError(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent,getTheme()), PorterDuff.Mode.SRC_ATOP);
            }else {
                //noinspection deprecation
                mobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
            isValidPhoneNumber=true;
        }
        return isValidPhoneNumber;
    }


}
