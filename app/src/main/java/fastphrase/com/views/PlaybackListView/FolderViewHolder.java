package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import fastphrase.com.IPlaybackController;
import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.views.FolderView;
import fastphrase.com.views.RecordingView;

/**
 * Created by bob on 2/28/16.
 */
public class FolderViewHolder extends RecyclerView.ViewHolder
        implements FolderView.IFolderListener {

    private int mPosition;
    private FolderView mFolderView;
    private LinearLayout mRecordings;
    private CardView mContents;
    private IPlaybackController mListener;
    private FolderView.IFolderListener mFolderListener;

    public FolderViewHolder(View view) {
        super(view);

        mFolderView = (FolderView)view.findViewById(R.id.folder);
        mContents = (CardView)view.findViewById(R.id.contents);
        mRecordings = (LinearLayout)view.findViewById(R.id.recordings);

        mFolderView.setFolderListener(this);
    }

    public void setPlaybackController(IPlaybackController listener) {
        mListener = listener;
    }

    public void setFolderListener(FolderView.IFolderListener listener){
        mFolderListener = listener;
    }

    @Override
    public void onFolderOpened(int position) {
        if(mFolderListener != null){
            mFolderListener.onFolderOpened(position);
        }
        expand(mContents);
    }

    @Override
    public void onFolderClosed(int position) {
        if(mFolderListener != null){
            mFolderListener.onFolderClosed(position);
        }
        collapse(mContents);
    }

    public void onBindData(PlaybackViewHolderData data, int position, Context context) {
        mFolderView.setFolderName(data.tag.label);
        mFolderView.setFolderPosition(position);
        if(data.isFolderOpen){
            mFolderView.openFolder();
            expand(mContents);
        }else{
            mFolderView.closeFolder();
            collapse(mContents);
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

    private static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
