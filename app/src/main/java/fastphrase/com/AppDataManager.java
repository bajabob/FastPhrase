package fastphrase.com;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fastphrase.com.models.AppData;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;

/**
 * Created by bob on 2/28/16.
 */
public class AppDataManager {

    private AppData mAppData;

    public AppDataManager(Context context){
        mAppData = AppDataStoreManager.load(context);

        // put all of it in alphabetical order
        Collections.sort(mAppData.tags);
        Collections.sort(mAppData.recordings);
    }

    /**
     * Add a new recording to the system
     * @param recording Recording
     * @return ADMResponse
     */
    public ADMResponse addRecording(Recording recording, Context context){

        /**
         * look for duplicate label
         */
        if(this.getRecording(recording.label) != null){
            return new ADMResponse(context.getString(R.string.error_duplicate_label_exists), true);
        }

        mAppData.recordings.add(recording);

        return new ADMResponse("", false);
    }

    /**
     * get a recording by its hash
     * @param hash long
     * @return Recording | null (if none is found)
     */
    public Recording getRecording(long hash){
        for(Recording r : mAppData.recordings){
            if(r.hash == hash){
                return r;
            }
        }
        return null;
    }

    /**
     * get a recording by its exact label
     * @param label string
     * @return Recording | null (if none is found)
     */
    public Recording getRecording(String label){
        for(Recording r : mAppData.recordings){
            if(r.label.toLowerCase() == label.toLowerCase()){
                return r;
            }
        }
        return null;
    }

    /**
     * get a recording by its exact label
     * @param tag Tag
     * @return List<Recording>
     */
    public List<Recording> getRecordings(Tag tag){
        List<Recording> recordings = new ArrayList<Recording>();
        for(Recording r : mAppData.recordings){
            for(long hash : r.tagHashes){
                if(hash == tag.hash){
                    recordings.add(r);
                }
            }
        }
        return recordings;
    }

    /**
     * get all recordings that dont have a tag
     * @return List<Recording>
     */
    public List<Recording> getRecordingsWithoutTags(){
        List<Recording> recordings = new ArrayList<Recording>();
        for(Recording r : mAppData.recordings){
            if(r.tagHashes.size() == 0){
                recordings.add(r);
            }
        }
        return recordings;
    }

    /**
     * get all tags the user has created
     * @return List<Tag>
     */
    public List<Tag> getTags(){
        return mAppData.tags;
    }

    /**
     * get all recordings the user has created
     * @return List<Recording>
     */
    public List<Recording> getRecordings(){
        return mAppData.recordings;
    }

    /**
     * simple class for providing errors
     */
    public class ADMResponse{
        public String message;
        public boolean hadError = false;

        public ADMResponse(String message, boolean hadError){
            this.message = message;
            this.hadError = hadError;
        }
    }
}
