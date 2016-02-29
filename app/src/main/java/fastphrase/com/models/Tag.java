package fastphrase.com.models;

import com.google.gson.Gson;

import java.util.Random;

/**
 * This model represents a single recording in the system.
 */
public class Tag {

    public String label;
    public long hash;

    public Tag(String label){
        this.label = label;
        Random r = new Random();
        this.hash = r.nextLong();
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
}
