package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import fastphrase.com.R;
import fastphrase.com.models.Tag;

public class EditTagView extends FrameLayout{

    private TextView mLabel;
    private EditText mLabelEdit;
    private CheckBox mCheckbox;
    private ViewFlipper mEditFlipper;

    private Tag mTag;
    private boolean mIsSelected;

    private ImageButton mEdit, mSave, mDelete;

    private ICallback mListener;

    public EditTagView(Context context) {
        this(context, null);
    }

    public EditTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_edit_tag, this, true);

        mCheckbox = (CheckBox) view.findViewById(R.id.checkbox);
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsSelected = isChecked;
                if(mListener != null){
                    if(mIsSelected){
                        mListener.onTagSelected(mTag);
                    }else {
                        mListener.onTagDeselected(mTag);
                    }
                }else{
                    throw new RuntimeException("Listener is null, not expected");
                }
            }
        });

        mLabel = (TextView) view.findViewById(R.id.tag_label);
        mLabelEdit = (EditText) view.findViewById(R.id.tag_label_edit);

        mDelete = (ImageButton) view.findViewById(R.id.delete);
        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onAreYouSureDialogRequested(mTag);
                }else{
                    throw new RuntimeException("Listener is null, not expected");
                }
            }
        });

        mEditFlipper = (ViewFlipper) view.findViewById(R.id.edit_flipper);
        mEditFlipper.setInAnimation(this.getContext(),R.anim.fade_in);
        mEditFlipper.setOutAnimation(this.getContext(),R.anim.fade_out);

        mSave = (ImageButton) view.findViewById(R.id.save);
        mSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mTag.label = mLabelEdit.getText().toString();
                    mLabel.setText(mTag.label);
                    mListener.onTagEdited(mTag);
                    mListener.onClearFocus();
                    mLabelEdit.setVisibility(GONE);
                    mLabel.setVisibility(VISIBLE);
                    mEditFlipper.showPrevious();
                }else{
                    throw new RuntimeException("Listener is null, not expected");
                }
            }
        });

        mEdit = (ImageButton) view.findViewById(R.id.edit);
        mEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLabelEdit.setText(mTag.label);
                mLabelEdit.setVisibility(VISIBLE);
                mLabel.setVisibility(GONE);
                mEditFlipper.showNext();
            }
        });

    }

    public void setTagAndState(Tag tag, boolean isSelected){
        mIsSelected = isSelected;
        mTag = tag;
        mLabel.setText(tag.label);
        mLabelEdit.setText(tag.label);
        mCheckbox.setChecked(isSelected);
    }

    public void setICallbackListener(ICallback listener){
        mListener = listener;
    }


    public interface ICallback{
        void onAreYouSureDialogRequested(Tag tag);
        void onTagEdited(Tag tag);
        void onTagSelected(Tag tag);
        void onTagDeselected(Tag tag);
        void onClearFocus();
    }

}
