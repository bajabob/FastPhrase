package fastphrase.com.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

/**
 * Created by Justin on 4/13/2016.
 */
public class SettingsHelper {

    private static final String HAPTIC_KEY = "haptic_key";
    private static final String PREF_NAME = "haptic_preferences";

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;

    public static void onClick(Context context, View view){
        if(getHapticPreference(context)){
            HapticClickHelper.onClick(view);
        }
    }

    public static boolean getHapticPreference(Context context){
        mSharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        if(mSharedPreferences.contains(HAPTIC_KEY)){
            return mSharedPreferences.getBoolean(HAPTIC_KEY,false);
        }
        else {
            mEditor = mSharedPreferences.edit();
            mEditor.putBoolean(HAPTIC_KEY, false);
            mEditor.commit();
            return false;
        }
    }

    public static void setHapticPreference(Context context, boolean state){
        mSharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(HAPTIC_KEY,state);
        mEditor.commit();
    }

}
