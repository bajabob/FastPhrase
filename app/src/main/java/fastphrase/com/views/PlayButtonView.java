package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import fastphrase.com.R;

/**
 * Created by bob on 2/29/16.
 */
public class PlayButtonView extends FrameLayout{

    private long mPlayLengthMs;
    private long ellapsedPlayTimeMs = 0;
    private IPlayButtonListener mListener;

    public PlayButtonView(Context context) {
        this(context, null);
    }

    public PlayButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_play_button, this, true);
        this.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

//              NEED TO GET THE BUTTON: CALCULATE AMOUNT OF TIME REMAINING AND DISPLAY PERCENTAGE OF THAT.
//              I BELIVE I CAN USE THIS METHOD: public void addArc (RectF oval, float startAngle, float sweepAngle);
                long timeButtonPush = System.currentTimeMillis();
//              PLAY THE CLIP HERE
//              ANNIMATE HERE
                mPlayLengthMs = 5000;
                float percentFilled = 0;
                while(ellapsedPlayTimeMs < mPlayLengthMs){

//                  DIFFERNCE BETWEEN CURRENT TIME AND WHEN THE BUTTON WAS PUSHED
                    long currentTime = System.currentTimeMillis();
                    ellapsedPlayTimeMs = currentTime - timeButtonPush;

//                  Calculates percent the pie should be filled: Between 0 and 100
                    percentFilled = ((ellapsedPlayTimeMs/(float)mPlayLengthMs));
//                  ANNIMATE BUTTON BASED OFF percentFilled: CREATE CANVAS?
//                  http://stackoverflow.com/questions/12458476/how-to-create-circular-progress-barpie-chart-like-indicator-android

                    

                    Log.d("Wes", "Current Ellapsed Time: " + String.valueOf(ellapsedPlayTimeMs) + " mPlayLengthMS: " + String.valueOf(mPlayLengthMs) + " Percent Filled: " + String.valueOf(percentFilled));
                }
                Log.d("Wes", String.valueOf(timeButtonPush));
            }
        });
    }

    /**
     * How long does the clip play for in MS?
     * @param playLengthMs long
     */
    public void setPlayLengthMs(long playLengthMs){
        mPlayLengthMs = playLengthMs;
    }

    /**
     * Sets who is listening for callbacks
     * @param listener IPlayButtonListener
     */
    public void setPlayButtonListener(IPlayButtonListener listener){
        mListener = listener;
    }

    public interface IPlayButtonListener{
    }
}
