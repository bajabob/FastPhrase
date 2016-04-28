package fastphrase.com.record;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import fastphrase.com.AppDataManager;
import fastphrase.com.EditRecordingActivity;
import fastphrase.com.R;
import fastphrase.com.dialog.AddTagDialog;
import fastphrase.com.dialog.AreYouSureDialog;
import fastphrase.com.helpers.SettingsHelper;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;
import fastphrase.com.views.EditTagView;
import fastphrase.com.views.IconTextButtonView;

/**
 * Created by bob on 4/8/16.
 */
public class TagEditorFragment extends Fragment implements EditTagView.ICallback{

    private static final String TAG = "TagFragment";
    private static final String BUNDLE_RECORDING_HASH = "bundle_recording_hash";

    private ICallback mCallback;

    private AppDataManager mAppData;
    private Recording mRecording;
    private LinearLayout mTagViews;


    public static TagEditorFragment newInstance(long recordingHash){
        TagEditorFragment f = new TagEditorFragment();
        Bundle args = new Bundle();
        args.putLong(BUNDLE_RECORDING_HASH, recordingHash);
        f.setArguments(args);
        return f;
    }

    public void setCallback(ICallback callback){mCallback = callback;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tag_editor, container, false);

        Bundle args = getArguments();
        long recordingHash = args.getLong(BUNDLE_RECORDING_HASH, -1);
        if(recordingHash == -1){
            throw new RuntimeException("No recording hash specified. Please use newInstance() method pattern.");
        }

        mAppData = new AppDataManager(getActivity());
        mRecording = mAppData.getRecording(recordingHash);

        mTagViews = (LinearLayout) v.findViewById(R.id.tag_container);

        IconTextButtonView addTag = (IconTextButtonView) v.findViewById(R.id.add);
        addTag.setLabel(getString(R.string.add_tag));
        addTag.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black_24dp));
        addTag.setBackgroundColor(getResources().getColor(R.color.white_80));
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsHelper.onClick(v.getContext(), v);
                AddTagDialog dialog = AddTagDialog.newInstance();
                dialog.setDialogListener(new AddTagDialog.DialogListener() {
                    @Override
                    public void onAddTag(Tag tag) {
                        mAppData.addTag(tag);
                        mAppData.save(getActivity());
                        clearFocus();
                        invalidateTagList();
                    }

                    @Override
                    public void onCancel() {
                        // do nothing
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "add recording dialog");
            }
        });

        Button doneButton = (Button) v.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditRecordingActivity)v.getContext()).onBackPressed();
            }
        });

        invalidateTagList();

        return v;
    }

    /**
     * Rebuild the tag list
     */
    private void invalidateTagList(){
        mTagViews.removeAllViews();
        for(Tag tag : mAppData.getTags()){
            EditTagView editTagView = new EditTagView(getActivity());
            editTagView.setICallbackListener(this);

            boolean isSelected = mRecording.tagHashes.contains(tag.hash);
            editTagView.setTagAndState(tag, isSelected);

            editTagView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mTagViews.addView(editTagView);
        }
    }

    /**
     * hide keyboard, reset edit text
     */
    private void clearFocus(){
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClearFocus() {
        this.clearFocus();
    }

    @Override
    public void onAreYouSureDialogRequested(final Tag tag) {
        AreYouSureDialog dialog = AreYouSureDialog.newInstance();
        dialog.setDialogListener(new AreYouSureDialog.DialogListener() {
            @Override
            public void onYes() {
                mAppData.removeTag(tag);
                mAppData.save(getActivity());
                clearFocus();
                invalidateTagList();
            }

            @Override
            public void onCancel() {
                // do nothing!
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), "are you sure dialog");
    }

    @Override
    public void onTagEdited(Tag tag) {
        mAppData.addTag(tag);
        mAppData.save(getActivity());
    }

    @Override
    public void onTagSelected(Tag tag) {
        mAppData.addTagToRecording(mRecording, tag);
        mAppData.save(getActivity());
    }

    @Override
    public void onTagDeselected(Tag tag) {
        mAppData.removeTagFromRecording(mRecording, tag);
        mAppData.save(getActivity());
    }

    public interface ICallback {
//        void onTags
    }

}
