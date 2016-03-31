package fastphrase.com.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fastphrase.com.R;

/**
 * Created by bobtimm on 3/31/2016.
 */
public class EditRecordingDialog extends DialogFragment{

    private static final String  BUNDLE_RECORDING_HASH = "recording_hash";

    public static EditRecordingDialog newInstance(long recordingHash){
        EditRecordingDialog f = new EditRecordingDialog();
        Bundle args = new Bundle();
        args.putLong(BUNDLE_RECORDING_HASH, recordingHash);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());

//        mLayoutIndex = getArguments().getInt(BUNDLE_LAYOUT_INDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_recording_dialog, container, false);
        
        return v;
    }
}
