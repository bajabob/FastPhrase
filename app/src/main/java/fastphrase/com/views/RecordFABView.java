package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import fastphrase.com.R;

public class RecordFABView extends FrameLayout{

    private IRecordFABListener mListener;

    public RecordFABView(Context context) {
        this(context, null);
    }

    public RecordFABView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordFABView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_record_fab, this, true);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onRecordActivityRequested();
                }
            }
        });
    }

    /**
     * Who is listening for the button press?
     * @param listener IRecordFABListener
     */
    public void setRecordFABListener(IRecordFABListener listener){
        mListener = listener;
    }

    /**
     * Fade out this button from view
     */
    public void onFadeOut(){
        // todo
    }

    /**
     * Fade in this button from view
     */
    public void onFadeIn(){
        // todo
    }


    public interface IRecordFABListener{
        void onRecordActivityRequested();
    }
}
