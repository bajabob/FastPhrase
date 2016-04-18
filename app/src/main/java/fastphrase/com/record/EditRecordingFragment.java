package fastphrase.com.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import fastphrase.com.AppDataManager;
import fastphrase.com.R;
import fastphrase.com.helpers.SettingsHelper;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;
import fastphrase.com.views.IconTextButtonView;
import fastphrase.com.views.TagView;

/**
 * Created by bob on 4/8/16.
 */
public class EditRecordingFragment extends Fragment{

    private static final String TAG = "EditRecordingFragment";
    private static final String BUNDLE_RECORDING_HASH = "bundle_recording_hash";

    private Recording mRecording;
    private EditText mLabel;
    private ICallback mCallback;
    private TextView mMessage;
    private FlowLayout mTagContainer;

    private AppDataManager mAppData;

    public static EditRecordingFragment newInstance(long recordingHash){
        EditRecordingFragment f = new EditRecordingFragment();
        Bundle args = new Bundle();
        args.putLong(BUNDLE_RECORDING_HASH, recordingHash);
        f.setArguments(args);
        return f;
    }

    public void setCallback(ICallback callback){mCallback = callback;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_recording, container, false);

        Bundle args = getArguments();
        long recordingHash = args.getLong(BUNDLE_RECORDING_HASH, -1);
        if(recordingHash == -1){
            throw new RuntimeException("No recording hash specified. Please use newInstance() method pattern.");
        }

        mAppData = new AppDataManager(getActivity());
        mRecording = mAppData.getRecording(recordingHash);

        mLabel = (EditText) v.findViewById(R.id.label);
        mTagContainer = (FlowLayout) v.findViewById(R.id.tag_container);

        mMessage = (TextView) v.findViewById(R.id.message);

        // save button
        IconTextButtonView save = (IconTextButtonView) v.findViewById(R.id.save);
        save.setLabel(getString(R.string.save_uc));
        save.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_save_black_24dp));
        save.setBackgroundColor(getResources().getColor(R.color.p100));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsHelper.onClick(view.getContext(), view);
                String newLabel = mLabel.getText().toString();

                Recording possibleDuplicate = mAppData.getRecording(newLabel);

                if(newLabel.length() == 0){
                    mMessage.setVisibility(View.VISIBLE);
                    mMessage.setText(R.string.error_empty_label);
                    return;
                }else if(possibleDuplicate != null && possibleDuplicate.hash != mRecording.hash){
                    mMessage.setVisibility(View.VISIBLE);
                    mMessage.setText(R.string.error_duplicate_label_exists);
                    return;
                }else{
                    mMessage.setVisibility(View.GONE);
                }

                mRecording.label = newLabel;

                if(mCallback != null){
                    if(canSave()) {
                        mRecording.label = mLabel.getText().toString();
                        mCallback.onBackAllowed();
                        mCallback.onSaveRecording(mRecording);
                    }
                }else{
                    throw new RuntimeException("Callback is null. Not expected.");
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
                SettingsHelper.onClick(view.getContext(),view);
                if(mCallback != null){
                    mCallback.onDeleteRecording(mRecording);
                }else{
                    throw new RuntimeException("Callback is null. Not expected.");
                }
            }
        });

        // add tag button
        IconTextButtonView addTag = (IconTextButtonView) v.findViewById(R.id.add_tag);
        addTag.setLabel(getString(R.string.tag_editor_uc));
        addTag.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black_24dp));
        addTag.setBackgroundColor(getResources().getColor(R.color.white_80));
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCallback != null){
                    if(canSave()) {
                        mCallback.onEditAndAssignTags(mRecording);
                        mCallback.onBackAllowed();
                    }
                }else{
                    throw new RuntimeException("Callback is null. Not expected.");
                }
            }
        });

        return v;
    }

    /**
     * verifies all user inserted data
     * @return boolean
     */
    private boolean canSave(){
        String newLabel = mLabel.getText().toString();

        Recording possibleDuplicate = mAppData.getRecording(newLabel);

        if(newLabel.length() == 0){
            mMessage.setVisibility(View.VISIBLE);
            mMessage.setText(R.string.error_empty_label);
            return false;
        }else if(possibleDuplicate != null && possibleDuplicate.hash != mRecording.hash){
            mMessage.setVisibility(View.VISIBLE);
            mMessage.setText(R.string.error_duplicate_label_exists);
            return false;
        }else{
            mMessage.setVisibility(View.GONE);
        }

        return true;
    }


    public void onStart(){
        super.onStart();
        invalidateData();
    }


    private void invalidateData(){
        // set label
        if(mRecording.label != null && mRecording.label.length() > 0) {
            mLabel.setText(mRecording.label);
            if(mCallback != null){
                mCallback.onBackAllowed();
            }else{
                throw new RuntimeException("Listener null, not expected");
            }
        }else{
            if(mCallback != null){
                mCallback.onBackNotAllowed();
            }else{
                throw new RuntimeException("Listener null, not expected");
            }
        }

        // add tags
        for(Tag tag : mRecording.getTags()){

            // add tags to list
            TagView tagView = new TagView(getActivity());
            tagView.setLabel(tag.label);
            tagView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mTagContainer.addView(tagView);
        }
    }

    public interface ICallback {
        void onSaveRecording(Recording recording);
        void onDeleteRecording(Recording recording);
        void onEditAndAssignTags(Recording recording);
        void onBackAllowed();
        void onBackNotAllowed();
    }

}
