package my.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import my.cart.R;
import my.cart.adapter.CartsAdapter;
import my.cart.common.ConnectAsynchronously;
import my.cart.common.Prefrences;
import my.cart.common.Utility;
import my.cart.model.CartModel;

public class CartsActivity extends AppCompatActivity {
    private CartsAdapter adapter;
    private List<CartModel> cartModelList;
    public static final int REQUEST_CODE=9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carts_activity);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cartModelList=new LinkedList<>();

        RecyclerView cart_recycler =(RecyclerView)findViewById(R.id.cart_recycler);
        cart_recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter =new CartsAdapter(this,cartModelList);
        cart_recycler.setAdapter(adapter);

        hitOnApi();
    }

    private void hitOnApi(){
        String uid =Prefrences.getUserId(CartsActivity.this);
        new MyCartData(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Utility.BASE_URL+"members/"+uid+"/carts/?page=1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.carts_menu,menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.add){
            Intent new_activity =new Intent(CartsActivity.this,CartCreation.class);
            new_activity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(new_activity,REQUEST_CODE);
        }

        return true;
    }

    class MyCartData extends AsyncTask<String, Void,String> {
        private Context mContext;
        private ProgressBar progressBar;

        public MyCartData(Context mContext) {
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
            return ConnectAsynchronously.connectAsynchronously(strings[0],mContext);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int requestCode =ConnectAsynchronously.getRequestCode();
            String message =ConnectAsynchronously.getRequestMessage();
            if(requestCode== HttpURLConnection.HTTP_OK){
                JSONArray jsonArray=null;

                try {
                    jsonArray =new JSONObject(s).getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonArray!=null){
                    if(cartModelList!=null){
                        cartModelList.clear();
                    }
                    for (int i =0; i<jsonArray.length(); i++){
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String  name =jsonObject.getString("name");
                            if(!TextUtils.isEmpty(name)){
                                CartModel model =new CartModel(name);
                                cartModelList.add(model);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(cartModelList!=null && cartModelList.size()<0){
                        (findViewById(R.id.placeHolder)).setVisibility(View.VISIBLE);
                    }else {
                        (findViewById(R.id.placeHolder)).setVisibility(View.GONE);
                    }
                }
            }else {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==requestCode){
            if(data!=null){
                //refresh list
                hitOnApi();
            }

        }
    }
}
