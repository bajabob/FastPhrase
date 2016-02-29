package fastphrase.com.views.PlaybackListView;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;

/**
 * Created by bob on 2/28/16.
 */
public abstract class PlaybackViewHolder extends RecyclerView.ViewHolder {

    public PlaybackViewHolder(View view) {
        super(view);
    }

    abstract public void onBindData(Recording recording, Activity activity, int position);
    abstract public void onBindData(Tag tag, Activity activity, int position);
    abstract public void setPlaybackViewHolderListener(IPlaybackViewHolderListener listener);

}
