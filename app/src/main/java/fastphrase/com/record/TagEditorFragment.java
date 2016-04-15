package fastphrase.com.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import fastphrase.com.AppDataManager;
import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;
import fastphrase.com.views.EditTagView;

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

        for(Tag tag : mAppData.getTags()){
            EditTagView editTagView = new EditTagView(getActivity());
            editTagView.setICallbackListener(this);
            editTagView.setTagAndState(tag, false);
            editTagView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mTagViews.addView(editTagView);
        }

        return v;
    }

    @Override
    public void onAreYouSureDialogRequested(Tag tag) {

    }

    @Override
    public void onTagEdited(Tag tag) {

    }

    @Override
    public void onTagSelected(Tag tag) {

    }

    @Override
    public void onTagDeselected(Tag tag) {

    }

    public interface ICallback {
//        void onTags
    }

}
