package fastphrase.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import fastphrase.com.views.RecordFABView;

public class PlaybackListActivity extends AppCompatActivity implements RecordFABView.IRecordFABListener{

    private RecordFABView mRecordFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_list);

        mRecordFAB = (RecordFABView) findViewById(R.id.record_fab);
        // lets fab know we are listening for clicks
        mRecordFAB.setRecordFABListener( this );

        /**
         * TODO: If audio is playing, fade out FAB. Fade back in when recording is complete.
         */
    }

    @Override
    public void onRecordActivityRequested() {
        Intent intent = RecordingActivity.newInstance(this);
        startActivity(intent);
    }
}
