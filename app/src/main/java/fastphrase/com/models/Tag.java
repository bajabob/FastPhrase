package fastphrase.com.models;

import com.google.gson.Gson;

import fastphrase.com.helpers.Hash;

/**
 * This model represents a single recording in the system.
 */
public class Tag implements Comparable<Tag>{

    public String label;
    public long hash;

    public Tag(String label){
        this.label = label;
        this.hash = Hash.generate();
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
