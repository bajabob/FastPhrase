package fastphrase.com.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fastphrase.com.AppDataManager;
import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.views.IconTextButtonView;

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

        // save button
        IconTextButtonView save = (IconTextButtonView) v.findViewById(R.id.save);
        save.setLabel(getString(R.string.save_uc));
        save.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_save_black_24dp));
        save.setBackgroundColor(getResources().getColor(R.color.p100));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // delete button
        IconTextButtonView delete = (IconTextButtonView) v.findViewById(R.id.delete);
        delete.setLabel(getString(R.string.delete_uc));
        delete.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete_forever_black_24dp));
        delete.setBackgroundColor(getResources().getColor(R.color.r100));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        Log.d(TAG, mRecording.toJson());

        return v;
    }



}
