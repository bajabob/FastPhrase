package fastphrase.com.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fastphrase.com.AppDataManager;
import fastphrase.com.R;
import fastphrase.com.models.Recording;

/**
 * Created by bob on 4/8/16.
 */
public class EditRecordingFragment extends Fragment{

    private static final String TAG = "EditRecordingFragment";
    private static final String BUNDLE_RECORDING_HASH = "bundle_recording_hash";

    private Recording mRecording;

    public static EditRecordingFragment newInstance(long recordingHash){
        EditRecordingFragment f = new EditRecordingFragment();
        Bundle args = new Bundle();
        args.putLong(BUNDLE_RECORDING_HASH, recordingHash);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_recording, container, false);

        Bundle args = getArguments();
        long recordingHash = args.getLong(BUNDLE_RECORDING_HASH, -1);
        if(recordingHash == -1){
            throw new RuntimeException("No recording hash specified. Please use newInstance() method pattern.");
        }

        AppDataManager appData = new AppDataManager(getActivity());
        mRecording = appData.getRecording(recordingHash);

        Log.d(TAG, mRecording.toJson());

        return v;
    }



}
