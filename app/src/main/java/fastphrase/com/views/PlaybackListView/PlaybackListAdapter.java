package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import fastphrase.com.AppDataManager;

/**
 * Created by bob on 2/28/16.
 */
public class PlaybackListAdapter extends RecyclerView.Adapter<PlaybackViewHolder> {

    private IPlaybackViewHolderListener mListener;
    private AppDataManager mAppData;

    public PlaybackListAdapter(Context context, IPlaybackViewHolderListener listener){
        mAppData = new AppDataManager(context);
        mListener = listener;
    }

    @Override
    public PlaybackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PlaybackViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
