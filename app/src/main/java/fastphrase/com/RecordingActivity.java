package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;

import fastphrase.com.helpers.RecordingFileSystem;
import fastphrase.com.models.Recording;
import fastphrase.com.record.AmplitudeView;
import fastphrase.com.views.ElapsedTimeView;
import fastphrase.com.views.RecordButtonView;
import fastphrase.com.views.TitleBarView;

public class RecordingActivity extends AppCompatActivity
        implements RecordButtonView.IRecordButtonListener{

    private Recording mNewRecording;
    private ElapsedTimeView mElapsedTime;
    private AudioRecorder mAudioRecorder;
    private AmplitudeView mAmplitude;

    public static Intent newInstance(Context context){
        Intent intent = new Intent(context, RecordingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        TitleBarView titleBarView = (TitleBarView)findViewById(R.id.title);
        titleBarView.setRecordingTitleBar();

        mElapsedTime = (ElapsedTimeView) findViewById(R.id.elapsed_time);
        mAmplitude = (AmplitudeView) findViewById(R.id.amplitude);

        RecordButtonView recordButton = (RecordButtonView) findViewById(R.id.record_button);
        recordButton.setRecordButtonListener(this);

    }

    @Override
    public void onStartRecording() {
        mNewRecording = new Recording();

        RecordingFileSystem rfs = new RecordingFileSystem(mNewRecording);
        mAudioRecorder = AudioRecorderBuilder.with(this)
                .fileName(rfs.getFilenameAndPath())
                .config(AudioRecorder.MediaRecorderConfig.DEFAULT)
                .loggable()
                .build();


        mAudioRecorder.start(new AudioRecorder.OnStartListener() {
            @Override
            public void onStarted() {
                Log.d("AudioRecorder", "started");
            }

            @Override
            public void onException(Exception e) {
                Log.e("AudioRecorder", e.getLocalizedMessage());
            }
        });

        mElapsedTime.onStart();
        mAmplitude.onRecordingStarted();
    }

    @Override
    public void onStopRecording() {

        mAudioRecorder.pause(new AudioRecorder.OnPauseListener() {
            @Override
            public void onPaused(String activeRecordFileName) {
                Log.d("AudioRecorder", "paused, " + activeRecordFileName);
            }

            @Override
            public void onException(Exception e) {
                Log.e("AudioRecorder", e.getLocalizedMessage());
            }
        });

        // stop timer and get elapsed time
        mElapsedTime.onStop();
        mAmplitude.onRecordingStopped();
        mNewRecording.playbackLengthMs = mElapsedTime.getElapsedTimeInMilliseconds();

        Log.d("Recording Complete", mNewRecording.toJson());
    }
}
