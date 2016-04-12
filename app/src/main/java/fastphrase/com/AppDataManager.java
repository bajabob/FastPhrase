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
     * Save data to disk
     * @param context Context
     */
    public void save(Context context){
        AppDataStoreManager.save(context, mAppData);
    }

    /**
     * Add a new recording to the system. Does not allow duplicates.
     * @param recording Recording
     */
    public void addRecording(Recording recording){
        this.removeRecording(recording);
        mAppData.recordings.add(recording);
    }

    /**
     * Remove a recording from the system
     * @param recording Recording
     */
    public void removeRecording(Recording recording){
        this.removeRecording(recording.hash);
    }

    /**
     * Remove a recording from the system
     * @param recordingHash long
     */
    public void removeRecording(long recordingHash){
        for(int i = 0; i < mAppData.recordings.size(); i++){
            if(mAppData.recordings.get(i).hash == recordingHash){
                mAppData.recordings.remove(i);
                break;
            }
        }
    }

    /**
     * Check if a label exists in the store
     * @param label String
     * @return boolean
     */
    public boolean hasLabel(String label){
        return getRecording(label) != null;
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
     * get a recording by its exact label (to lowercase)
     * @param label string
     * @return Recording | null (if none is found)
     */
    public Recording getRecording(String label){
        String search = label.toLowerCase();
        for(Recording r : mAppData.recordings){
            if(r.label.toLowerCase().equals(search)){
                return r;
            }
        }
        return null;
    }

    /**
     * get a list of recordings by their exact label
     * @param label string
     * @return List<Recording> | empty list
     */
    public List<Recording> getRecordings(String label){
        List<Recording> list = new ArrayList<>();
        for(Recording r : mAppData.recordings){
            if(r.label.toLowerCase() == label.toLowerCase()){
                list.add(r);
            }
        }
        return list;
    }

    /**
     * get a recording by its exact label
     * @param tag Tag
     * @return List<Recording>
     */
    public List<Recording> getRecordings(Tag tag){
        List<Recording> recordings = new ArrayList<Recording>();
        for(Recording r : mAppData.recordings){
            for(Tag t : r.getTags()){
                if(t.hash == tag.hash){
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
//    public class ADMResponse{
//        public String message;
//        public boolean hadError = false;
//
//        public ADMResponse(String message, boolean hadError){
//            this.message = message;
//            this.hadError = hadError;
//        }
//    }
}
