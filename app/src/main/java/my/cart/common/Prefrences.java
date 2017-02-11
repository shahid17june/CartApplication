package my.cart.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shahid Akhtar on 10-02-2017.
 */

public class Prefrences extends Application {

    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "user_name";
    Prefrences paybykissInstanec;

    @Override
    public void onCreate() {
        super.onCreate();
        paybykissInstanec = this;

    }

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static void setUserID(Context ctx, String user_id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_ID, user_id);
        editor.commit();
    }

    public static String getUserId(Context ctx) {
        String visibility = getSharedPreferences(ctx).getString(USER_ID, null);
        return visibility;
    }

}

