package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import fastphrase.com.R;
import fastphrase.com.models.Recording;

/**
 * Created by bob on 2/29/16.
 */
public class RecordingView extends LinearLayout{

    private TextView mRecordingName;
    private WeakReference<IRecordingListener> mListener;
    private Recording mRecording;

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
     */
    public void setRecording(Recording recording){
        mRecording = recording;
        mRecordingName.setText(recording.label);
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
