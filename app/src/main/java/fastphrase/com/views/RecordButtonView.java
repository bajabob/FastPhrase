package fastphrase.com.views;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

import fastphrase.com.R;
import fastphrase.com.helpers.SettingsHelper;

public class RecordButtonView extends FrameLayout{

    private ViewFlipper mRecordIcon;
    private IRecordButtonListener mListener;

    public RecordButtonView(Context context) {
        this(context, null);
    }

    public RecordButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_record_button, this, true);
        mRecordIcon = (ViewFlipper) view.findViewById(R.id.record_button_flipper);
        mRecordIcon.setInAnimation(this.getContext(), R.anim.recording_button_in);
        mRecordIcon.setOutAnimation(this.getContext(), R.anim.recording_button_out);

        this.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingsHelper.onClick(v.getContext(), v);
                if (mRecordIcon.getDisplayedChild() == 0) {
                    mRecordIcon.showNext();
                    if (mListener != null) {
                        mListener.onStartRecording();
                    }
                } else {
                    mRecordIcon.showPrevious();
                    if (mListener != null) {
                        mListener.onStopRecording();
                    }
                }
            }
        });
    }
    /**
     * Who is listening for the button press?
     * @param listener IRecordButtonListener
     */
    public void setRecordButtonListener(IRecordButtonListener listener){
        mListener = listener;
    }

    public void removeRecordButtonListener(){
        mListener = null;
    }

    public void onFadeOut(){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        this.setAnimation(fadeOut);
    }

    public interface IRecordButtonListener{
        void onStartRecording();
        void onStopRecording();
    }
}
