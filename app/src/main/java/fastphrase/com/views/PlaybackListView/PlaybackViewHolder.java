package fastphrase.com.views.PlaybackListView;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by bob on 2/28/16.
 */
public abstract class PlaybackViewHolder extends RecyclerView.ViewHolder {

    public PlaybackViewHolder(View view) {
        super(view);
    }

    abstract public void onBindData(PlaybackViewHolderData data, int position);
    abstract public void setPlaybackViewHolderListener(IPlaybackViewHolderListener listener);

}
