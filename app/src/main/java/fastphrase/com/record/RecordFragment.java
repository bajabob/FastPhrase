package fastphrase.com.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fastphrase.com.R;
import fastphrase.com.helpers.RecordingFileSystem;
import fastphrase.com.models.Recording;
import fastphrase.com.views.AmplitudeView;
import fastphrase.com.views.ElapsedTimeView;
import fastphrase.com.views.RecordButtonView;

/**
 * Created by bob on 4/8/16.
 */
public class RecordFragment extends Fragment
        implements RecordButtonView.IRecordButtonListener{

    private Recording mNewRecording;
    private RecordingFileSystem mRecordingFileSystem;
    private ElapsedTimeView mElapsedTime;

    private AmplitudeView mAmplitude;
    private RecordButtonView mRecordButton;
    private IRecord mCallback;


    public static RecordFragment newInstance(){
        RecordFragment f = new RecordFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_record, container, false);

        mElapsedTime = (ElapsedTimeView) v.findViewById(R.id.elapsed_time);
        mAmplitude = (AmplitudeView) v.findViewById(R.id.amplitude);

        mRecordButton = (RecordButtonView) v.findViewById(R.id.record_button);
        mRecordButton.setRecordButtonListener(this);

        mNewRecording = new Recording();
        mRecordingFileSystem = new RecordingFileSystem(mNewRecording);

        return v;
    }

    /**
     * Set the parent callback for this fragment
     * @param callback
     */
    public void setIRecordCallback(IRecord callback){
        mCallback = callback;
    }

    @Override
    public void onStartRecording() {

        mAmplitude.onRecordingStarted();
        mElapsedTime.onStart();


        getActivity().startService(RecordingService.newStartInstance(mRecordingFileSystem.getFilenameAndPath(), getActivity()));
    }

    @Override
    public void onStopRecording() {

        getActivity().stopService(RecordingService.newStopInstance(getActivity()));

        mRecordButton.removeRecordButtonListener();
        mRecordButton.onFadeOut();

        // stop timer and get elapsed time
        mElapsedTime.onStop();
        mNewRecording.playbackLengthMs = mElapsedTime.getElapsedTimeInMilliseconds();
        mAmplitude.onRecordingStopped();
        if(mCallback != null) {
            mAudioRecorder = null;
            mCallback.onRecordingComplete(mNewRecording);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

}
