package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import fastphrase.com.models.Recording;
import fastphrase.com.views.ElapsedTimeView;
import fastphrase.com.views.RecordButtonView;
import fastphrase.com.views.TitleBarView;

public class RecordingActivity extends AppCompatActivity
        implements RecordButtonView.IRecordButtonListener{

    private Recording mNewRecording;
    private ElapsedTimeView mElapsedTime;

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


        RecordButtonView recordButton = (RecordButtonView) findViewById(R.id.record_button);
        recordButton.setRecordButtonListener(this);

    }

    @Override
    public void onStartRecording() {
        mNewRecording = new Recording();

        mElapsedTime.onStart();
    }

    @Override
    public void onStopRecording() {

        // stop timer and get elapsed time
        mElapsedTime.onStop();
        mNewRecording.playbackLengthMs = mElapsedTime.getElapsedTimeInMilliseconds();

        Log.d("Recording Complete", mNewRecording.toJson());
    }
}
