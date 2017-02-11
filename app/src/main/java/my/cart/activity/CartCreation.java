package my.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import my.cart.R;
import my.cart.common.ConnectAsynchronously;
import my.cart.common.Prefrences;
import my.cart.common.Utility;

public class CartCreation extends AppCompatActivity {
    private boolean isDataSaved;
    private EditText name_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_creation_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);
        name_editText = (EditText) findViewById(R.id.name_editText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_creation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save:
                String name = name_editText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    name_editText.setError(getResources().getString(R.string.input_field_not_empty));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("ownerId", Prefrences.getUserId(CartCreation.this));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new CreateCartItemTask(jsonObject, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Utility.BASE_URL + "carts/");

                }
                break;
        }
        return true;
    }

    class CreateCartItemTask extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        private Context mContext;
        private ProgressBar progressBar;

        public CreateCartItemTask(JSONObject jsonObject, Context mContext) {
            this.jsonObject = jsonObject;
            this.mContext = mContext;
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

            if (requestCode == HttpURLConnection.HTTP_OK || requestCode == HttpURLConnection.HTTP_CREATED) {
                isDataSaved = true;
                name_editText.setText("");
            }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {
        if (isDataSaved) {
            Intent data = new Intent();
            data.putExtra("isDataSaved", isDataSaved);
            setResult(CartsActivity.REQUEST_CODE, data);
        }
        super.onBackPressed();
    }
}
