package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import fastphrase.com.IPlaybackController;
import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;

/**
 * Created by bob on 2/29/16.
 */
public class RecordingView extends LinearLayout{

    private LinearLayout mTagContainer;
    private TextView mRecordingName;
    private WeakReference<IPlaybackController> mListener;
    private Recording mRecording;
    private PlayButtonView mPlayButton;

    public RecordingView(Context context) {
        this(context, null);
    }

    public RecordingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_recording, this, true);

        mRecordingName = (TextView)view.findViewById(R.id.recording_name);
        mTagContainer = (LinearLayout)view.findViewById(R.id.tag_container);

        mPlayButton = (PlayButtonView)view.findViewById(R.id.play_button);
        mPlayButton.setPlayButtonListener(new PlayButtonView.IPlayButtonListener() {
            @Override
            public void onPlayButtonPressed() {
                if(mListener != null){
                    // lets parent know which recording to play
                    mListener.get().onPlayRecording(mRecording);
                }
            }
        });
    }

    /**
     * Set the recording object to be used for this view
     * @param recording Recording
     * @param parentTag Tag - the tag being used to display this folder
     * @param context Context
     */
    public void setRecording(Recording recording, Tag parentTag, Context context){
        mRecording = recording;
        mRecordingName.setText(recording.label);
        mPlayButton.setPlayLengthMs(recording.playbackLengthMs);

        for(Tag tag : recording.getTags()){

            // skip adding parent tag
            if(tag.hash == parentTag.hash){
                continue;
            }

            // add tags to list
            TagView tagView = new TagView(context);
            tagView.setLabel(tag.label);
            tagView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mTagContainer.addView(tagView);
        }
    }

    /**
     * Sets who is listening for callbacks
     * @param listener IFolderListener
     */
    public void setPlaybackController(IPlaybackController listener){
        mListener = new WeakReference<IPlaybackController>(listener);
    }
}
