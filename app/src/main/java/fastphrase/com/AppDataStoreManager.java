package fastphrase.com;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import fastphrase.com.models.AppData;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;

/**
 * Created by bob on 2/28/16.
 */
public class AppDataStoreManager {

    private static final String SHARED_FILE = "AppData";

    /**
     * Load AppData
     * @param context Context
     * @return AppData
     */
    public static AppData load(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_FILE, context.MODE_PRIVATE);
        String json = prefs.getString(SHARED_FILE, null);
        if(json == null){
            json = defaultJSONAppData();
        }

        Log.d("AppDataStoreManager", "Loading JSON: "+json);

        Gson gson = new Gson();
        AppData data = gson.fromJson(json, AppData.class);

        /**
         *  load complete, form any additional data here
         */
        data.afterLoad();

        return data;
    }

    /**
     * Save AppData
     * @param context Context
     * @param appData AppData
     */
    public static void save(Context context, AppData appData){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_FILE, context.MODE_PRIVATE).edit();
        editor.putString(SHARED_FILE, appData.toJson());
        editor.apply();
    }

    /**
     * App data to use the first time the application is loaded (for testing purposes)
     * @return AppData
     */
    private static String defaultJSONAppData(){
        AppData ad = new AppData();

        ad.tags.add(new Tag("Sample Tag"));

        Gson gson = new Gson();
        return gson.toJson(ad, AppData.class);
    }

}
