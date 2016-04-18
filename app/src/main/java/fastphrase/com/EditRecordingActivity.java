package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import fastphrase.com.dialog.AreYouSureDialog;
import fastphrase.com.models.Recording;
import fastphrase.com.record.EditRecordingFragment;
import fastphrase.com.record.TagEditorFragment;
import fastphrase.com.views.TitleBarView;

public class EditRecordingActivity extends AppCompatActivity {

    private static final String TAG = "EditRecordingActivity";
    private static final String BUNDLE_RECORDING_HASH = "bundle_recording_hash";

    private long mRecordingHash;
    private boolean mCanGoBack = false;

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
        mRecordingHash = bundle.getLong(BUNDLE_RECORDING_HASH, -1);
        if(mRecordingHash == -1){
            throw new RuntimeException("No recording hash specified. Please use newInstance() method pattern.");
        }

        TitleBarView titleBarView = (TitleBarView)findViewById(R.id.title);
        titleBarView.setRecordingTitleBar();

        attachEditRecordingFragment(mRecordingHash);
    }

    private void attachEditRecordingFragment(final long recordingHash){
        Log.d(TAG, "Starting EditRecordingFragment with " + recordingHash);
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
                onRequestAreYouSureDialog(recording.hash);
            }

            @Override
            public void onEditAndAssignTags(Recording recording) {
                AppDataManager appData = new AppDataManager(EditRecordingActivity.this);
                appData.addRecording(recording);
                appData.save(EditRecordingActivity.this);

                attachEditTagsFragment(recording.hash);
            }

            @Override
            public void onBackAllowed() {
                mCanGoBack = true;
            }

            @Override
            public void onBackNotAllowed() {
                mCanGoBack = false;
            }
        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment).commit();
    }

    private void attachEditTagsFragment(final long recordingHash){
        TagEditorFragment tagEditorFrag = TagEditorFragment.newInstance(recordingHash);
        tagEditorFrag.setCallback(new TagEditorFragment.ICallback() {
        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("edit-tags-fragment");
        ft.replace(R.id.fragment_container, tagEditorFrag).commit();
    }

    private void onRequestAreYouSureDialog(final long recordingHash){
        AreYouSureDialog dialog = AreYouSureDialog.newInstance();
        dialog.setDialogListener(new AreYouSureDialog.DialogListener() {
            @Override
            public void onYes() {
                AppDataManager appData = new AppDataManager(EditRecordingActivity.this);
                appData.removeRecording(recordingHash);
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

    public void onBackPressed(){
        if(mCanGoBack){
            super.onBackPressed();
        }
    }
}
