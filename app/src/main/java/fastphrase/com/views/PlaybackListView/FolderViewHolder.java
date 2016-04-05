package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import fastphrase.com.IPlaybackController;
import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.views.FolderView;
import fastphrase.com.views.RecordingView;

/**
 * Created by bob on 2/28/16.
 */
public class FolderViewHolder extends RecyclerView.ViewHolder {

    private int mPosition;
    private FolderView mFolderView;
    private LinearLayout mRecordings;
    private IPlaybackController mListener;

    public FolderViewHolder(View view) {
        super(view);

        mFolderView = (FolderView)view.findViewById(R.id.folder);

        // lets us listen for folder events (onFolderOpen, etc)


        mRecordings = (LinearLayout)view.findViewById(R.id.recordings);
    }

    public void setPlaybackController(IPlaybackController listener) {
        mListener = listener;
    }

    public void setFolderListener(FolderView.IFolderListener listener){
        mFolderView.setFolderListener(listener);
    };

    public void onBindData(PlaybackViewHolderData data, int position, Context context) {
        mFolderView.setFolderName(data.tag.label);
        mFolderView.setFolderPostion(position);
        if(data.isFolderOpen){
            mFolderView.openFolder();
        }else{
            mFolderView.closeFolder();
        }

        mPosition = position;

        // remove all views from container
        mRecordings.removeAllViews();

        if(context != null) {
            for (Recording r : data.recordings) {
                RecordingView recordingView = new RecordingView(context);
                recordingView.setRecording(r, data.tag, context);
                recordingView.setPlaybackController(mListener);
                recordingView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                mRecordings.addView(recordingView);
            }
        }

    }

}
