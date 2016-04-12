package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import fastphrase.com.dialog.AreYouSureDialog;
import fastphrase.com.dialog.MoreOptionsDialog;
import fastphrase.com.models.Recording;
import fastphrase.com.record.EditRecordingFragment;
import fastphrase.com.views.TitleBarView;

public class EditRecordingActivity extends AppCompatActivity {

    private static final String TAG = "EditRecordingActivity";
    private static final String BUNDLE_RECORDING_HASH = "bundle_recording_hash";

    public static Intent newInstance(Context context, long recordingHash){
        Intent intent = new Intent(context, EditRecordingActivity.class);
        Bundle args = new Bundle();
        args.putLong(BUNDLE_RECORDING_HASH, recordingHash);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recording);

        Bundle bundle = getIntent().getExtras();
        long recordingHash = bundle.getLong(BUNDLE_RECORDING_HASH, -1);
        if(recordingHash == -1){
            throw new RuntimeException("No recording hash specified. Please use newInstance() method pattern.");
        }

        TitleBarView titleBarView = (TitleBarView)findViewById(R.id.title);
        titleBarView.setRecordingTitleBar();

        attachEditRecordingFragment(recordingHash);
    }

    private void attachEditRecordingFragment(long recordingHash){
        Log.d(TAG, "Starting EditRecordingFragment with "+recordingHash);
        EditRecordingFragment fragment = EditRecordingFragment.newInstance(recordingHash);
        fragment.setCallback(new EditRecordingFragment.ICallback() {
            @Override
            public void onSaveRecording(Recording recording) {
                AppDataManager appData = new AppDataManager(EditRecordingActivity.this);
                appData.addRecording(recording);
                appData.save(EditRecordingActivity.this);
                finish();
            }

            @Override
            public void onDeleteRecording(final Recording recording) {

                AreYouSureDialog dialog = AreYouSureDialog.newInstance();
                dialog.setDialogListener(new AreYouSureDialog.DialogListener() {
                    @Override
                    public void onYes() {
                        AppDataManager appData = new AppDataManager(EditRecordingActivity.this);
                        appData.removeRecording(recording);
                        appData.save(EditRecordingActivity.this);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // do nothing!
                    }
                });
                dialog.show(getSupportFragmentManager(), "are you sure dialog");
            }
        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment).commit();
    }

}
