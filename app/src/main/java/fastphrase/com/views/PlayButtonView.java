package fastphrase.com.views;

import android.content.Context;
import android.graphics.Canvas;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.Timer;

import fastphrase.com.R;

/**
 * Created by bob on 2/29/16.
 */
public class PlayButtonView extends FrameLayout{

    private ProgressBar mProgress;

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

        mProgress = (ProgressBar)view.findViewById(R.id.progress);

        this.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

//              NEED TO GET THE BUTTON: CALCULATE AMOUNT OF TIME REMAINING AND DISPLAY PERCENTAGE OF THAT.
//              I BELIEVE I CAN USE THIS METHOD: public void addArc (RectF oval, float startAngle, float sweepAngle);

                final long timeButtonPush = System.currentTimeMillis();
                mPlayLengthMs = 5000;

//              Handler that calls myself every 10 miliseconds with exit condition
                final Timer drawTimer = new Timer();
                drawTimer.scheduleAtFixedRate(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        long currentTime = System.currentTimeMillis();
                        ellapsedPlayTimeMs = currentTime - timeButtonPush;

                        float percentFilled = ((ellapsedPlayTimeMs / (float) mPlayLengthMs) * 100);

                        mProgress.setProgress((int)percentFilled);
//                      DRAW HERE
                        if (ellapsedPlayTimeMs > mPlayLengthMs) {
                            drawTimer.cancel();
                        }
                    }
                }, 0, 50);//put here time 1000 milliseconds=1 second

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
