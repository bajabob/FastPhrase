package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import fastphrase.com.models.Recording;
import fastphrase.com.record.IRecord;
import fastphrase.com.record.RecordErrorFragment;
import fastphrase.com.record.RecordFragment;
import fastphrase.com.views.TitleBarView;

public class RecordingActivity extends AppCompatActivity implements IRecord{

    private boolean mCanFinish = true;

    public static Intent newInstance(Context context){
        Intent intent = new Intent(context, RecordingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
    }

    private void initView() {
        TitleBarView titleBarView = (TitleBarView)findViewById(R.id.title);
        titleBarView.setRecordingTitleBar();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(ft.isEmpty()){
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
        if(mCanFinish) {
            finish();
        }
    }

    @Override
    public void onRecordingComplete(Recording newRecording) {
        // recording is less than 500ms
        if(newRecording.playbackLengthMs < 500){
            Toast.makeText(this, getString(R.string.error_short_recording), Toast.LENGTH_SHORT).show();
            finish();
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

    @Override
    public void onRecordingStarted() {
        mCanFinish = false;
    }
}
