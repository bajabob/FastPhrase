package fastphrase.com.models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * All application data that needs persisted
 */
public class AppData {

    public List<Recording> recordings;
    public List<Tag> tags;


    public AppData(){
        recordings = new ArrayList<Recording>();
        tags = new ArrayList<Tag>();
    }

    public String toJson(){
        return new Gson().toJson(AppData.this);
    }

    /**
     * Called directly after a load to form any additional data needed
     */
    public void afterLoad(){

        // let each recording know what kind of tags it has
        for(Recording r : recordings){
            r.afterLoad(tags);
        }
    }
}
