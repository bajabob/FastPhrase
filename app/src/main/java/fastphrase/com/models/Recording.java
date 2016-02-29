package fastphrase.com.models;

import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This model represents a single recording in the system.
 */
public class Recording implements Comparable<Recording>{

    public String label;
    public long createdAt;
    public List<Long> tagHashes;
    public long hash;

    public Recording(String label, List<Long> tagHashes){
        this.label = label;
        this.tagHashes = new ArrayList<Long>(tagHashes);
        this.createdAt = DateTime.now().getMillis();
        Random r = new Random();
        this.hash = r.nextLong();
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    @Override
    public int compareTo(Recording another) {
        return this.label.compareTo(another.label);
    }
}
