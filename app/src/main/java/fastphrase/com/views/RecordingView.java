package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.lang.ref.WeakReference;

import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;

/**
 * Created by bob on 2/29/16.
 */
public class RecordingView extends LinearLayout{

    private FlowLayout mTagContainer;
    private TextView mRecordingName;
    private WeakReference<IRecordingListener> mListener;
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
        mPlayButton = (PlayButtonView)view.findViewById(R.id.play_button);
        mTagContainer = (FlowLayout)view.findViewById(R.id.tag_container);

        /*
            todo: setup onClickListener for this view, so that when it is pressed it calls the listener
            "onPlayRecording" to let parent know to play audio
         */

        // example callback (always check for null)
        if(mListener != null){
            // lets parent know which recording to play
            mListener.get().onPlayRecording(mRecording);
        }
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
    public void setRecordingListener(IRecordingListener listener){
        mListener = new WeakReference<IRecordingListener>(listener);
    }

    public interface IRecordingListener{
        void onPlayRecording(Recording recording);
    }
}
