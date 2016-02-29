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
}
