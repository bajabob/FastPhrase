package fastphrase.com.models;

import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This model represents a single recording in the system.
 */
public class Recording {

    public String label;
    public long createdAt;
    public List<Tag> tags;
    public long hash;

    public Recording(String label, List<Tag> tags){
        this.label = label;
        this.tags = new ArrayList<Tag>(tags);
        this.createdAt = DateTime.now().getMillis();
        Random r = new Random();
        this.hash = r.nextLong();
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
}
