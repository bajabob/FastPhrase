package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import fastphrase.com.models.Recording;
import fastphrase.com.record.IRecord;
import fastphrase.com.record.RecordErrorFragment;
import fastphrase.com.record.RecordFragment;
import fastphrase.com.views.TitleBarView;

public class RecordingActivity extends AppCompatActivity implements IRecord{

    public static Intent newInstance(Context context){
        Intent intent = new Intent(context, RecordingActivity.class);
        return intent;
    }

    private void ResetRecordingActivity() {
        setContentView(R.layout.activity_recording);

        TitleBarView titleBarView = (TitleBarView)findViewById(R.id.title);
        titleBarView.setRecordingTitleBar();

        attachRecordFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        TitleBarView titleBarView = (TitleBarView)findViewById(R.id.title);
        titleBarView.setRecordingTitleBar();

        if (savedInstanceState == null) {
            attachRecordFragment();
        }

    }

    private void attachRecordFragment(){
        RecordFragment fragment = RecordFragment.newInstance();
        fragment.setIRecordCallback(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment).commit();
    }


    @Override
    public void onBackPressed() {
        /**
         * Todo, tell fragments to discard recording
         */
        finish();
    }

    @Override
    public void onRecordingComplete(Recording newRecording) {
        // recording is less than 500ms
        if(newRecording.playbackLengthMs < 500){
            Context context = getApplicationContext();
            CharSequence text = "Recordings need to be longer than half a second";
            int duration = Toast.LENGTH_SHORT;

//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
            Toast.makeText(context, text, duration).show();

            ResetRecordingActivity();
        }
        else {
            Log.d("Recording Complete", newRecording.toJson());

            // save recording to disk
            AppDataManager appData = new AppDataManager(this);
            appData.addRecording(newRecording);
            appData.save(this);

            // open recording
            Intent intent = EditRecordingActivity.newInstance(this, newRecording.hash);
            finish(); // we do not need to come back to this current activity
            startActivity(intent);
        }
    }

    @Override
    public void onRecordingException(boolean canStartRecordErrorFragment) {
        // view requested compat recorder fragment
        if(canStartRecordErrorFragment){
            RecordErrorFragment fragment = RecordErrorFragment.newInstance();
            fragment.setIRecordCallback(new RecordErrorFragment.ICallback() {
                @Override
                public void onRetry() {
                    attachRecordFragment();
                }
            });
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment).commit();
        }
    }
}
