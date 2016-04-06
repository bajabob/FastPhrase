package fastphrase.com.models;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This model represents a single recording in the system.
 */
public class Recording implements Comparable<Recording>{

    private static final String FILENAME_PREFIX = "Recording_";
    private static final String FILENAME_EXTENSION = ".mp4";
    private static final String FOLDER = "FastPhrase";

    public String label;
    public long createdAt;
    public List<Long> tagHashes;
    public long hash;
    public long playbackLengthMs;

    /**
     * The following private data members will never be saved
     */
    private List<Tag> mTags;

    public Recording(){
        this("", null, 0);
    }

    public Recording(String label, List<Long> tagHashes, long playbackLengthMs){
        this.label = label;
        if(tagHashes != null) {
            this.tagHashes = new ArrayList<Long>(tagHashes);
        }else{
            this.tagHashes = new ArrayList<Long>();
        }
        this.createdAt = DateTime.now().getMillis();
        Random r = new Random();
        this.hash = r.nextLong();
        this.playbackLengthMs = playbackLengthMs;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    /**
     * Returns the path
     * @return String
     */
    public String getPath(){
        return Environment.getExternalStorageDirectory() + File.separator + FOLDER + File.separator;
    }

    /**
     * Returns the path and filename
     * @return String
     */
    public String getFilenameAndPath(){
        return this.getPath() + this.getFilename();
    }

    /**
     * Get just the filename
     * @return String
     */
    public String getFilename(){
        return FILENAME_PREFIX + Long.toString(this.hash) + FILENAME_EXTENSION;
    }

    @Override
    public int compareTo(Recording another) {
        return this.label.compareTo(another.label);
    }

    /**
     * get the list of affiliated tags
     * @return List<Tag>
     */
    public List<Tag> getTags(){
        return mTags;
    }

    /**
     * Called after load from disk to get all information this object currently points too
     * @param tags List<Tag></Tag>
     */
    public void afterLoad(List<Tag> tags){
        Log.d("Test", "e");
        mTags = new ArrayList<Tag>();
        for(long tagId : tagHashes) {
            for (Tag t : tags) {
                if(t.hash == tagId){
                    mTags.add(t);

                    Log.d("Tag", label + ": "+t.label);
                }
            }
        }
    }
}
