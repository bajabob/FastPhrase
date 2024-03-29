package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import fastphrase.com.R;
import fastphrase.com.helpers.SettingsHelper;

/**
 * Created by bob on 2/29/16.
 */
public class FolderView extends LinearLayout{

    private TextView mFolderName;
    private ViewFlipper mFolderIcon;
    private IFolderListener mListener;
    private TextView mFolderItemCount;
    private int mPosition;

    public FolderView(Context context) {
        this(context, null);
    }

    public FolderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_folder, this, true);

        mFolderName = (TextView)view.findViewById(R.id.folder_name);
        mFolderItemCount = (TextView)view.findViewById(R.id.item_count);
        mFolderIcon = (ViewFlipper)view.findViewById(R.id.folder_flipper);
        mFolderIcon.setInAnimation(this.getContext(),R.anim.folder_open);
        mFolderIcon.setOutAnimation(this.getContext(), R.anim.folder_close);

        this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingsHelper.onClick(v.getContext(), v);
                if (mFolderIcon.getDisplayedChild() == 0) {
                    mFolderIcon.showNext();
                    mFolderIcon.setInAnimation(v.getContext(), R.anim.closed_folder_open);
                    mFolderIcon.setOutAnimation(v.getContext(), R.anim.closed_folder_close);
                    if (mListener != null) {
                        // lets parent know the folder has opened
                        mListener.onFolderOpened(mPosition);
                    }
                } else {
                    mFolderIcon.showPrevious();
                    mFolderIcon.setInAnimation(v.getContext(),R.anim.folder_open);
                    mFolderIcon.setOutAnimation(v.getContext(), R.anim.folder_close);
                    if (mListener != null) {
                        // lets parent know the folder has closed
                        mListener.onFolderClosed(mPosition);
                    }
                }
            }
        });
    }

    public void openFolder(){
        if(mFolderIcon.getDisplayedChild() == 0) {
            mFolderIcon.showNext();
        }
    }

    public void closeFolder(){
        if(mFolderIcon.getDisplayedChild() != 0) {
            mFolderIcon.showPrevious();
        }
    }

    /**
     * Set the name of the folder
     * @param name String
     */
    public void setFolderName(String name){
        mFolderName.setText(name);
    }


    public void setFolderPosition(int position){
        mPosition = position;
    }

    public void setItemCount(int count) { mFolderItemCount.setText(Integer.toString(count)); }

    /**
     * Sets who is listening for callbacks
     * @param listener IFolderListener
     */
    public void setFolderListener(IFolderListener listener){
        mListener = listener;
    }

    public interface IFolderListener{
        void onFolderOpened(int position);
        void onFolderClosed(int position);
    }
}
