package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import fastphrase.com.IPlaybackController;
import fastphrase.com.R;
import fastphrase.com.models.Recording;

/**
 * Created by bob on 2/28/16.
 */
public class PlaybackListView extends LinearLayout {

    private PlaybackListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    public PlaybackListView(Context context) {
        this(context, null);
    }

    public PlaybackListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaybackListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_playback_list, this, true);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.list);
        mAdapter = new PlaybackListAdapter(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setPlaybackController(IPlaybackController listener){
        if(mAdapter != null){
            mAdapter.setPlaybackController(listener);
        }else{
            throw new RuntimeException("Playback List Adapter is null, trying to set required listener");
        }
    }

}
