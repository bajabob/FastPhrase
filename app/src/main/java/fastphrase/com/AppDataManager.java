package fastphrase.com;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import junit.framework.Assert;

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

    public AppDataManager(){

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
     * Add a new tag to the system. Does not allow duplicates.
     * @param tag Tag
     */
    public void addTag(Tag tag){
        this.removeTag(tag);
        mAppData.tags.add(tag);
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
     * Remove a tag from the system
     * @param tag Tag
     */
    public void removeTag(Tag tag){
        this.removeTag(tag.hash);
    }

    /**
     * Remove a tag from the system
     * @param tagHash long
     */
    public void removeTag(long tagHash){
        for(int i = 0; i < mAppData.tags.size(); i++){
            if(mAppData.tags.get(i).hash == tagHash){
                mAppData.tags.remove(i);
                break;
            }
        }
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
     * get a tag by its exact label (to lowercase)
     * @param label string
     * @return Tag | null (if none is found)
     */
    public Tag getTag(String label){
        String search = label.toLowerCase();
        for(Tag t : mAppData.tags){
            if(t.label.toLowerCase().equals(search)){
                return t;
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
            if(r.label.toLowerCase().equals(label.toLowerCase())){
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
     * Add a tag to a recording
     * @param recording Recording
     * @param tag Tag
     */
    public void addTagToRecording(Recording recording, Tag tag){
        for(Recording r : mAppData.recordings){
            if(r.hash == recording.hash){
                if(!r.tagHashes.contains(tag.hash)){
                    r.tagHashes.add(tag.hash);
                }
            }
        }
    }

    /**
     * Add a tag to a recording
     * @param recording Recording
     * @param tag Tag
     */
    public void removeTagFromRecording(Recording recording, Tag tag){
        for(Recording r : mAppData.recordings){
            if(r.hash == recording.hash){
                if(r.tagHashes.contains(tag.hash)){
                    r.tagHashes.remove(tag.hash);
                }
            }
        }
    }


    /**
     * Unit Tests
     */


    public void testDefaults(){
        Gson gson = new Gson();
        AppData expected = gson.fromJson(AppDataStoreManager.defaultJSONAppData(), AppData.class);

        if(expected.tags.size() != 1){
            Assert.fail("Default tags have changed");
        }
    }

    private void initAppData(){
        Gson gson = new Gson();
        mAppData = gson.fromJson(AppDataStoreManager.defaultJSONAppData(), AppData.class);
    }

    public void testRecordingsAreAdded(){
        initAppData();
        Tag t1 = new Tag("Greeting");
        Recording r1 = new Recording();
        r1.label = "Test";
        r1.tagHashes.add(t1.hash);
        mAppData.recordings.add(r1);
        Recording rTest = this.getRecording("test");
        Assert.assertEquals(r1, rTest);
    }

    public void testTagsAreAdded(){
        initAppData();
        Tag t1 = new Tag("Greeting");
        this.addTag(t1);
        Tag tTest = this.getTag("greeting");
        Assert.assertEquals(t1, tTest);
    }

    public void testRecordingsHasTags(){
        initAppData();
        Tag t1 = new Tag("Greeting");
        Recording r1 = new Recording();
        r1.label = "Test";

        this.addTag(t1);
        this.addRecording(r1);
        this.addTagToRecording(r1, t1);
        mAppData.afterLoad();

        List<Recording> found = this.getRecordings(t1);

        if(found.size() == 0){
            Assert.fail("Tag not added");
        }
    }

    public void testRemoveRecordingTag(){
        initAppData();
        Tag t1 = new Tag("Greeting");
        Recording r1 = new Recording();
        r1.label = "Test";

        this.addTag(t1);
        this.addRecording(r1);
        this.addTagToRecording(r1, t1);
        this.removeTagFromRecording(r1, t1);
        mAppData.afterLoad();

        List<Recording> found = this.getRecordings(t1);

        if(found.size() > 0){
            Assert.fail("Tag not removed");
        }
    }

    public void testHoldsTags(){
        initAppData();
        Tag t1 = new Tag("Greeting");

        this.addTag(t1);
        mAppData.afterLoad();

        List<Tag> found = this.getTags();

        if(found.size() == 0){
            Assert.fail("Tag not added");
        }
    }

    public void testGetRecordingsByLabel(){
        initAppData();
        Recording r1 = new Recording();
        r1.label = "Test";

        this.addRecording(r1);
        mAppData.afterLoad();

        List<Recording> found = this.getRecordings("test");

        if(found.size() == 0){
            Assert.fail("Recording not found");
        }
    }
}
