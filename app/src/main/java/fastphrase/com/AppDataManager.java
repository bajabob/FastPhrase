package fastphrase.com;

import android.content.Context;

import fastphrase.com.models.AppData;
import fastphrase.com.models.Recording;

/**
 * Created by bob on 2/28/16.
 */
public class AppDataManager {

    private AppData mAppData;

    public AppDataManager(Context context){
        mAppData = AppDataStoreManager.load(context);
    }

    /**
     * Add a new recording to the system
     * @param recording Recording
     */
    public void addRecording(Recording recording){

        /**
         * TODO: add to list, make certain that no other recordings have the same 'hash'
         */

    }

    /**
     * get a recording by hash
     * @param hash long
     * @return Recording | null (if none is found)
     */
    public Recording getRecording(long hash){
        /**
         * TODO: find the recording with the matching hash and return it
         */
        return null;
    }

}
