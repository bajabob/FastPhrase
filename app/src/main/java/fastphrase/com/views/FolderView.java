package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fastphrase.com.R;

/**
 * Created by bob on 2/29/16.
 */
public class FolderView extends LinearLayout{

    private TextView mFolderName;
    private ImageView mFolderIcon;
    private IFolderListener mListener;

    public FolderView(Context context) {
        this(context, null);
    }

    public FolderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_folder, this, true);

        mFolderIcon = (ImageView)view.findViewById(R.id.folder_icon);
        mFolderName = (TextView)view.findViewById(R.id.folder_name);
        ;
        /*
            todo: setup onClickListener for this view, so that when it is pressed it opens and closes the folder icon
            As the folder opens and closes it should issue a callback that something has happened.
         */

        // example callback (always check for null)
        if(mListener != null){
            // lets parent know the folder has opened
            mListener.onFolderOpened();
        }

        // example (will be called by parent
        setFolderName("Arabic");
    }

    /**
     * Set the name of the folder
     * @param name String
     */
    public void setFolderName(String name){
        mFolderName.setText(name);
    }

    /**
     * Sets who is listening for callbacks
     * @param listener IFolderListener
     */
    public void setFolderListener(IFolderListener listener){
        mListener = listener;
    }

    public interface IFolderListener{
        void onFolderOpened();
        void onFolderClosed();
    }
}
