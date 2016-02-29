package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import fastphrase.com.R;
import fastphrase.com.models.AppData;

/**
 * Created by bob on 2/28/16.
 */
public class PlaybackListView extends LinearLayout implements IPlaybackViewHolderListener {

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

        LayoutInflater inflator = LayoutInflater.from(context);
        View view = (View) inflator.inflate(R.layout.view_playback_list, this, true);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.list);
        mAdapter = new PlaybackListAdapter();

    }

    public void onCreate(AppData appData){

    }

}
