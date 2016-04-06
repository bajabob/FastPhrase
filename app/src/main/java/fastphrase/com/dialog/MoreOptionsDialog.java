package fastphrase.com.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fastphrase.com.R;
import fastphrase.com.views.IconTextButtonView;

/**
 * Created by bobtimm on 3/31/2016.
 */
public class MoreOptionsDialog extends DialogFragment{

    private static final String  BUNDLE_RECORDING_HASH = "recording_hash";

    private DialogListener mListener;
    private long mRecordingHash;

    public static MoreOptionsDialog newInstance(long recordingHash){
        MoreOptionsDialog f = new MoreOptionsDialog();
        Bundle args = new Bundle();
        args.putLong(BUNDLE_RECORDING_HASH, recordingHash);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
        mRecordingHash = getArguments().getLong(BUNDLE_RECORDING_HASH);
    }

    public void setDialogListener(DialogListener listener){
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_more_options, container, false);

        // edit button
        IconTextButtonView edit = (IconTextButtonView) v.findViewById(R.id.edit);
        edit.setLabel(getString(R.string.edit_uc));
        edit.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mode_edit_black_24dp));
        edit.setBackgroundColor(getResources().getColor(R.color.white_80));
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onEdit(mRecordingHash);
                    MoreOptionsDialog.this.delayedDismiss();
                }else{
                    throw new RuntimeException("listener is null, not expected");
                }
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
                if (mListener != null) {
                    mListener.onDelete(mRecordingHash);
                    MoreOptionsDialog.this.delayedDismiss();
                } else {
                    throw new RuntimeException("listener is null, not expected");
                }
            }
        });

        // cancel button
        Button cancel = (Button) v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCancel();
                    MoreOptionsDialog.this.delayedDismiss();
                } else {
                    throw new RuntimeException("listener is null, not expected");
                }
            }
        });

        return v;
    }

    /**
     * Delay dismissing the dialog so that ripple effects can show
     */
    private void delayedDismiss(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dismiss();
            }
        }, 400);
    }

    public interface DialogListener{
        void onCancel();
        void onEdit(long recordingHash);
        void onDelete(long recordingHash);
    }
}
