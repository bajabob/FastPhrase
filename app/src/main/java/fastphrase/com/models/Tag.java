package fastphrase.com.models;

import com.google.gson.Gson;

import java.util.Random;

import fastphrase.com.views.PlaybackListView.PlaybackListAdapter;

/**
 * This model represents a single recording in the system.
 */
public class Tag implements Comparable<Tag>{

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

    @Override
    public boolean equals(Object o){
        if(o instanceof Tag){
            return ((Tag)o).hash == this.hash;
        }
        return false;
    }

    @Override
    public int compareTo(Tag another) {
        return this.label.compareTo(another.label);
    }
}
