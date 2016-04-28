package fastphrase.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import fastphrase.com.dialog.AreYouSureDialog;
import fastphrase.com.dialog.MoreOptionsDialog;
import fastphrase.com.models.Recording;
import fastphrase.com.views.EmptyStateView;
import fastphrase.com.views.PlaybackListView.IQueue;
import fastphrase.com.views.PlaybackListView.PlaybackListView;
import fastphrase.com.views.PlaybackListView.PlaybackQueueManager;
import fastphrase.com.views.RecordFABView;

public class PlaybackListActivity extends AppCompatActivity implements
        RecordFABView.IRecordFABListener,
        IPlaybackController {

    private static final String TAG = "PlaybackListActivity";

    private PlaybackListView mPlaybackList;
    private RecordFABView mRecordFAB;
    private EmptyStateView mEmptyState;
    private PlaybackQueueManager mPlaybackQueueManager;

    private boolean mHasOpenedRecordActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_list);

        mRecordFAB = (RecordFABView) findViewById(R.id.record_fab);
        mRecordFAB.setRecordFABListener(this);

        mPlaybackList = (PlaybackListView)findViewById(R.id.playback_list_view);
        mPlaybackList.setPlaybackController(this);
        mPlaybackList.setCallback(new PlaybackListView.ICallback() {
            @Override
            public void onShowFAB() {
                if(mRecordFAB != null){
                    mRecordFAB.onFadeIn();
                }
            }
            @Override
            public void onHideFAB() {
                if(mRecordFAB != null){
                    mRecordFAB.onFadeOut();
                }
            }
        });

        mEmptyState = (EmptyStateView)findViewById(R.id.empty_state);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHasOpenedRecordActivity = false;

        mPlaybackQueueManager = new PlaybackQueueManager(this);

        mRecordFAB.onFadeIn();
        if(mPlaybackList != null){
            mPlaybackList.onRefreshData(this);
            if(mPlaybackList.hasElements() && mPlaybackList.getVisibility() == View.GONE){
                onPlaybackListHasElements();
            }else if(!mPlaybackList.hasElements()){
                onPlaybackListEmpty();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlaybackQueueManager.onDestroy();
        mPlaybackQueueManager = null;
    }

    @Override
    public void onPlaybackListEmpty() {
        mEmptyState.setVisibility(View.VISIBLE);
        mPlaybackList.setVisibility(View.GONE);
    }

    @Override
    public void onPlaybackListHasElements() {
        mEmptyState.setVisibility(View.GONE);
        mPlaybackList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRecordActivityRequested() {
        if(!mHasOpenedRecordActivity) {
            mHasOpenedRecordActivity = true;
            Intent intent = RecordingActivity.newInstance(this);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestEditRecordingDialog(long recordingHash) {
        MoreOptionsDialog dialog = MoreOptionsDialog.newInstance(recordingHash);
        dialog.setDialogListener(new MoreOptionsDialog.DialogListener() {
            @Override
            public void onCancel() {
                Log.d("Dialog", "User closed dialog.");
            }

            @Override
            public void onEdit(long recordingHash) {
                Log.d("Dialog", "User would like to edit a recording (hash): "+recordingHash);
                // open recording
                Intent intent = EditRecordingActivity.newInstance(PlaybackListActivity.this, recordingHash);
                startActivity(intent);
            }

            @Override
            public void onDelete(final long recordingHash) {
                Log.d("Dialog", "User would like to delete a recording (hash): "+recordingHash);

                AreYouSureDialog dialog = AreYouSureDialog.newInstance();
                dialog.setDialogListener(new AreYouSureDialog.DialogListener() {
                    @Override
                    public void onYes() {
                        AppDataManager appData = new AppDataManager(PlaybackListActivity.this);
                        appData.removeRecording(recordingHash);
                        appData.save(PlaybackListActivity.this);
                        mPlaybackList.onRefreshData(PlaybackListActivity.this);
                    }

                    @Override
                    public void onCancel() {
                        // do nothing!
                    }
                });
                dialog.show(getSupportFragmentManager(), "are you sure dialog");

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
    public void onPlayRecording(Recording recording, IQueue queueListener) {
        mPlaybackQueueManager.onPlay(recording, queueListener);
    }
}
