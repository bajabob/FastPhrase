package fastphrase.com.record;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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

    private static final int UPDATE_FREQUENCY_MS = 25;

    private boolean mIsServiceBound = false;
    private RecordingService mService;
    private Handler mHandler = new Handler();

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

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mService = ((RecordingService.LocalBinder)service).getService();

            mHandler.postDelayed(mRunnable, UPDATE_FREQUENCY_MS);
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mService = null;

        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mIsServiceBound && mService != null){

                mAmplitude.onAmplitudeChange(mService.getAmplitude());

            }
        }
    };

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

        getActivity().bindService(
                RecordingService.newStartInstance(
                        mRecordingFileSystem.getFilenameAndPath(),
                        getActivity()),
                mConnection,
                Context.BIND_AUTO_CREATE);
        mIsServiceBound = true;
    }

    @Override
    public void onStopRecording() {

        if (mIsServiceBound) {
            mIsServiceBound = false;
            // Detach our existing connection.
            getActivity().unbindService(mConnection);
        }

        mRecordButton.removeRecordButtonListener();
        mRecordButton.onFadeOut();

        // stop timer and get elapsed time
        mElapsedTime.onStop();
        mNewRecording.playbackLengthMs = mElapsedTime.getElapsedTimeInMilliseconds();
        mAmplitude.onRecordingStopped();
        if(mCallback != null) {
//            mAudioRecorder = null;
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
