package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import fastphrase.com.helpers.RecordingFileSystem;
import fastphrase.com.models.Recording;
import fastphrase.com.views.AmplitudeView;
import fastphrase.com.record.AudioRecorder;
import fastphrase.com.record.AudioRecorderBuilder;
import fastphrase.com.views.ElapsedTimeView;
import fastphrase.com.views.RecordButtonView;
import fastphrase.com.views.TitleBarView;

public class RecordingActivity extends AppCompatActivity
        implements RecordButtonView.IRecordButtonListener, AudioRecorder.OnStartListener{

    private Recording mNewRecording;
    private RecordingFileSystem mRecordingFileSystem;
    private ElapsedTimeView mElapsedTime;
    private AudioRecorder mAudioRecorder;
    private AmplitudeView mAmplitude;
    private RecordButtonView mRecordButton;



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

        mRecordButton = (RecordButtonView) findViewById(R.id.record_button);
        mRecordButton.setRecordButtonListener(this);

        mNewRecording = new Recording();
        mRecordingFileSystem = new RecordingFileSystem(mNewRecording);
    }

    @Override
    public void onStartRecording() {

        /**
         * restart listening system and delete previous recording
         */
        mAudioRecorder.cancel();
        mRecordingFileSystem.deleteCurrent();
        mAudioRecorder.start(this);

        mAmplitude.onRecordingStarted();
        mElapsedTime.onStart();
    }

    @Override
    public void onStopRecording() {

        mRecordButton.removeRecordButtonListener();
        mRecordButton.onFadeOut();

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
        mNewRecording.playbackLengthMs = mElapsedTime.getElapsedTimeInMilliseconds();
        mAmplitude.onRecordingStopped();
        Log.d("Recording Complete", mNewRecording.toJson());
    }


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
        Log.e("AudioRecorder", e.getLocalizedMessage());
    }

    @Override
    public void onBackPressed() {
        /**
         * do not keep recording if user exits (by pressing back)
         */
        mAudioRecorder.cancel();
        mRecordingFileSystem.deleteCurrent();
        finish();
    }

    protected void onResume(){
        super.onResume();
        mAudioRecorder = AudioRecorderBuilder.with()
                .fileName(mRecordingFileSystem.getFilenameAndPath())
                .config(AudioRecorder.MediaRecorderConfig.DEFAULT)
                .loggable()
                .build();
        mAudioRecorder.start(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mAudioRecorder.cancel();
    }
}
