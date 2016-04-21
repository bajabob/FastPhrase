package fastphrase.com.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fastphrase.com.AppDataManager;
import fastphrase.com.R;
import fastphrase.com.helpers.SettingsHelper;
import fastphrase.com.models.Tag;

/**
 * Created by bobtimm on 3/31/2016.
 */
public class AddTagDialog extends DialogFragment{


    private TextView mMessage;
    private EditText mNewTagLabel;
    private DialogListener mListener;
    private AppDataManager mAppData;

    public static AddTagDialog newInstance(){
        AddTagDialog f = new AddTagDialog();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    public void setDialogListener(DialogListener listener){
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_add_tag, container, false);

        mNewTagLabel = (EditText) v.findViewById(R.id.label);
        mMessage = (TextView) v.findViewById(R.id.message);
        mAppData = new AppDataManager(getActivity());

        // cancel button
        Button cancel = (Button) v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsHelper.onClick(view.getContext(),view);
                if (mListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dismiss();
                            mListener.onCancel();
                        }
                    }, 400);
                } else {
                    throw new RuntimeException("listener is null, not expected");
                }
            }
        });

        // yes button
        Button addTag = (Button) v.findViewById(R.id.add_tag);
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsHelper.onClick(view.getContext(), view);
                if (mListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (mNewTagLabel.getText().length() == 0) {
                                mMessage.setVisibility(View.VISIBLE);
                                mMessage.setText(getString(R.string.error_empty_tag));
                                return;
                            } else if (mAppData.getTag(mNewTagLabel.getText().toString()) != null) {
                                mMessage.setVisibility(View.VISIBLE);
                                mMessage.setText(getString(R.string.error_duplicate_tag_exists));
                                return;
                            } else {
                                mMessage.setVisibility(View.GONE);
                                Tag t = new Tag(mNewTagLabel.getText().toString());
                                mListener.onAddTag(t);
                                dismiss();
                            }
                        }
                    }, 400);
                } else {
                    throw new RuntimeException("listener is null, not expected");
                }
            }
        });

        return v;
    }

    /**
     * hide keyboard, reset edit text
     */
    private void clearFocus(){
        mNewTagLabel.clearFocus();
        mNewTagLabel.setText("");
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public interface DialogListener{
        void onAddTag(Tag tag);
        void onCancel();
    }
}
