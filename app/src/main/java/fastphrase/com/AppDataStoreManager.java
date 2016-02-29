package fastphrase.com;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

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
            return defaultAppData();
        }
        Gson gson = new Gson();
        return gson.fromJson(json, AppData.class);
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
     * App data to use the first time the application is loaded
     * @return AppData
     */
    private static AppData defaultAppData(){
        AppData ad = new AppData();

        ad.tags.add(new Tag("Arabic"));
        ad.tags.add(new Tag("Greek"));

        ad.recordings = new ArrayList<Recording>();
        ad.recordings.add(new Recording("Sit Down", ad.tags));
        ad.recordings.add(new Recording("20 Degrees Port", ad.tags));
        ad.recordings.add(new Recording("Stay in the boat", ad.tags));

        return ad;
    }

}
