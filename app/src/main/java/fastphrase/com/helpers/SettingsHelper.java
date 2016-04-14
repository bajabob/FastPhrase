package fastphrase.com.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

/**
 * Created by Justin on 4/13/2016.
 */
public class SettingsHelper {

    private static final String HAPTIC_KEY = "haptic_key";
    private static final String AUDIO_KEY = "audio_key";
    private static final String PREF_NAME = "audio_haptic_preferences";

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;

    public static void onClick(Context context, View view){
        if(getAudioPreference(context)){
            AudioClickHelper.onClick(view);
        }
        if(getHapticPreference(context)){
            HapticClickHelper.onClick(view);
        }
    }
    //Returns the audio preference to set inital state of checkbox
    public static boolean getAudioPreference(Context context){
        mSharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        if(mSharedPreferences.contains(AUDIO_KEY)){
            return mSharedPreferences.getBoolean(AUDIO_KEY,false);
        }
        //If the preference doesn't exist yet, add it
        else{
            mEditor = mSharedPreferences.edit();
            mEditor.putBoolean(AUDIO_KEY, false);
            mEditor.commit();
            return false;
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
    //Sets the preference
    public static void setAudioPreference(Context context, boolean state){
        mSharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(AUDIO_KEY,state);
        mEditor.commit();
    }

    public static void setHapticPreference(Context context, boolean state){
        mSharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(HAPTIC_KEY,state);
        mEditor.commit();
    }

}
