package fastphrase.com.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fastphrase.com.R;
import fastphrase.com.helpers.RecordingFileSystem;
import fastphrase.com.models.Recording;
import fastphrase.com.views.ElapsedTimeView;
import fastphrase.com.views.RecordButtonView;

/**
 * Created by bobtimm on 3/31/2016.
 *
 *  This is a dialog that appears when a the original recorder fails to run. Known devices that
 * will initiate this prompt are:
 * - Samsung Note 4
 * - Samsung Note 3
 */
public class CompatRecorderDialog extends DialogFragment implements RecordButtonView.IRecordButtonListener{

    private Recording mNewRecording;
    private RecordingFileSystem mRecordingFileSystem;
    private RecordButtonView mRecordButton;
    private ElapsedTimeView mElapsedTime;

    public static CompatRecorderDialog newInstance(){
        CompatRecorderDialog f = new CompatRecorderDialog();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());

        // create a new recording (reserves new hash)
        mNewRecording = new Recording();
        // setup file system so that recording can be stored
        mRecordingFileSystem = new RecordingFileSystem(mNewRecording);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_compat_recorder, container, false);

        mRecordButton = (RecordButtonView) v.findViewById(R.id.record_button);
        mRecordButton.setRecordButtonListener(this);

        mElapsedTime = (ElapsedTimeView) v.findViewById(R.id.elapsed_time);

        return v;
    }


    @Override
    public void onStartRecording() {
        // the record button was pressed (start recording)

        mElapsedTime.onStart();

        /**
         * Todo: record audio and store to disk
         */
    }

    @Override
    public void onStopRecording() {
        // the record button was pressed again (stop recording)

        mElapsedTime.onStop();

        /**
         * Todo: ensure recorded audio is stored in folder
         */
    }

}
