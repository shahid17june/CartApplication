package my.cart.common;

import android.content.Context;
import android.util.Base64;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import my.cart.R;


/**
 * Created by shahid Akhtar on 10-02-2017.
 */

public class ConnectAsynchronously {

   private static int requestCode;
   private static String myMessage;

    public static String connectAsynchronously(String uri, Context mContext) {
        String result ="";
        String userName =mContext.getResources().getString(R.string.db_name);
        String password =mContext.getResources().getString(R.string.db_pass);
        try {
            //Connect
            HttpURLConnection urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty ("Authorization", "Basic " + new String(Base64.encode((userName+":"+password).getBytes(),Base64.URL_SAFE)));
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            requestCode=urlConnection.getResponseCode();
            myMessage = urlConnection.getResponseMessage();
            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            result = sb.toString();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String connectAsynchronously(String uri, JSONObject jobj,Context mContext) {
        String result ="";
        String userName =mContext.getResources().getString(R.string.db_name);
        String password =mContext.getResources().getString(R.string.db_pass);
        try {
            //Connect
            HttpURLConnection urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty ("Authorization", "Basic " + new String(Base64.encode((userName+":"+password).getBytes(),1)));
            urlConnection.connect();

            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jobj.toString());
            writer.close();
            outputStream.close();
            requestCode=urlConnection.getResponseCode();
            myMessage = urlConnection.getResponseMessage();

            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String line ;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            result = sb.toString();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    public static int getRequestCode(){

        return requestCode;
    }

    public static String getRequestMessage(){

        return myMessage;
    }


}
