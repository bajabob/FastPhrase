package fastphrase.com.record;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import fastphrase.com.models.Recording;

/**
 * Created by bob on 4/21/16.
 */
public class RecordingService extends Service {

    private static final String BUNDLE_RECORDING_FILENAME_AND_PATH = "bundle_recording_filename_and_path";

    private AudioRecorder mAudioRecorder;

    /** interface for clients that bind */
    IBinder mBinder;


    public static Intent newStartInstance(String recordingFilenameAndPath, Context context){
        Intent intent = new Intent(context, RecordingService.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_RECORDING_FILENAME_AND_PATH, recordingFilenameAndPath);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newStopInstance(Context context){
        Intent intent = new Intent(context, RecordingService.class);
        return intent;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String filename = intent.getExtras().getString(BUNDLE_RECORDING_FILENAME_AND_PATH);
        if(filename != null){
            throw new RuntimeException("Filename must be specified.");
        }

        mAudioRecorder = AudioRecorderBuilder.with()
                .fileName(filename)
                .config(AudioRecorder.MediaRecorderConfig.DEFAULT)
                .loggable()
                .build();

        /**
         * start listening
         */
        mAudioRecorder.start(new AudioRecorder.OnStartListener() {
            @Override
            public void onStarted() {
                Log.d("AudioRecorder", "started");
            }

            @Override
            public void onAmplitudeChange(int percentage) {
                mAmplitude.onAmplitudeChange(percentage);
            }

            @Override
            public void onException(Exception e) {
                if(e != null && e.getMessage() != null) {
                    Log.e("AudioRecorder", e.getMessage());
                }
                if(mCallback != null) {
                    mCallback.onRecordingException(true);
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mAudioRecorder.pause(new AudioRecorder.OnPauseListener() {
            @Override
            public void onPaused(String activeRecordFileName) {
                Log.d("AudioRecorder", "paused, " + activeRecordFileName);
            }

            @Override
            public void onException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Log.e("AudioRecorder", e.getMessage());
                }
                if (mCallback != null) {
                    mCallback.onRecordingException(false);
                }
            }
        });
    }

}
