package fastphrase.com.views.PlaybackListView;

import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;

/**
 * Created by bob on 2/28/16.
 */
public class PlaybackViewHolderData {

    public Tag tag;
    public Recording recording = null;

    public boolean isFolder(){
        return recording == null;
    }

}
