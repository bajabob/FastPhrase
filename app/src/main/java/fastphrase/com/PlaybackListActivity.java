package fastphrase.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import fastphrase.com.dialog.EditRecordingDialog;
import fastphrase.com.models.Recording;
import fastphrase.com.views.PlaybackListView.PlaybackListView;
import fastphrase.com.views.RecordFABView;

public class PlaybackListActivity extends AppCompatActivity implements
        RecordFABView.IRecordFABListener,
        IPlaybackController {

    private PlaybackListView mPlaybackList;
    private RecordFABView mRecordFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_list);

        mRecordFAB = (RecordFABView) findViewById(R.id.record_fab);
        mRecordFAB.setRecordFABListener( this );

        mPlaybackList = (PlaybackListView)findViewById(R.id.playback_list_view);
        mPlaybackList.setPlaybackController(this);
    }

    @Override
    public void onRecordActivityRequested() {
        Intent intent = RecordingActivity.newInstance(this);
        startActivity(intent);
    }

    @Override
    public void onRequestEditRecordingDialog(long recordingHash) {
        EditRecordingDialog dialog = EditRecordingDialog.newInstance(recordingHash);
        dialog.setDialogListener(new EditRecordingDialog.DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onEdit(long recordingHash) {

            }

            @Override
            public void onDelete(long recordingHash) {

            }
        });
        dialog.show(getSupportFragmentManager(), "edit recording dialog");
    }

    @Override
    public void onFolderOpened(int position) {
        Log.d("Folder", "Opened");
    }

    @Override
    public void onFolderClosed(int position) {
        Log.d("Folder", "Closed");
    }

    @Override
    public void onPlayRecording(Recording recording) {
        Log.d("Recording", "Play: "+recording.label);
    }
}
