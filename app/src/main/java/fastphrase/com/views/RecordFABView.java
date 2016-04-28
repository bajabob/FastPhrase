package fastphrase.com.views;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import fastphrase.com.R;
import fastphrase.com.helpers.SettingsHelper;

public class RecordFABView extends FrameLayout{

    private IRecordFABListener mListener;
    private FloatingActionButton mRecordButton;

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

        mRecordButton = (FloatingActionButton)view.findViewById(R.id.record_button);
        mRecordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsHelper.onClick(v.getContext(), v);
                if (mListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mListener.onRecordActivityRequested();
                        }
                    }, 250);
                    onFadeOut();
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
        mRecordButton.hide();
    }

    /**
     * Fade in this button from view
     */
    public void onFadeIn(){
        mRecordButton.show();
    }


    public interface IRecordFABListener{
        void onRecordActivityRequested();
    }
}
