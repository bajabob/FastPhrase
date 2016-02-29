package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.views.FolderView;
import fastphrase.com.views.RecordingView;

/**
 * Created by bob on 2/28/16.
 */
public class FolderViewHolder extends RecyclerView.ViewHolder implements
        FolderView.IFolderListener,
        RecordingView.IRecordingListener{

    private int mPosition;
    private FolderView mFolderView;
    private LinearLayout mRecordings;
    private IPlaybackViewHolderListener mListener;

    public FolderViewHolder(View view) {
        super(view);

        mFolderView = (FolderView)view.findViewById(R.id.folder);

        // lets us listen for folder events (onFolderOpen, etc)
        mFolderView.setFolderListener(this);

        mRecordings = (LinearLayout)view.findViewById(R.id.recordings);
    }

    public void setPlaybackViewHolderListener(IPlaybackViewHolderListener listener) {
        mListener = listener;
    }

    public void onBindData(PlaybackViewHolderData data, int position, Context context) {
        mFolderView.setFolderName(data.tag.label);
        mPosition = position;

        if(data.isFolderOpen){
            // todo: update icon to open/close
        }

        // remove all views from container
        mRecordings.removeAllViews();

        if(context != null) {
            for (Recording r : data.recordings) {
                RecordingView recordingView = new RecordingView(context);
                recordingView.setRecording(r);
                recordingView.setRecordingListener(this);
                recordingView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                mRecordings.addView(recordingView);
            }
        }

    }

    @Override
    public void onPlayRecording(Recording recording) {

    }

    @Override
    public void onFolderOpened() {
        if(mListener != null){
            mListener.onFolderOpened(mPosition);
        }
    }

    @Override
    public void onFolderClosed() {
        if(mListener != null){
            mListener.onFolderClosed(mPosition);
        }
    }
}
