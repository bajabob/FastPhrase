package fastphrase.com.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.Timer;

import fastphrase.com.R;
import fastphrase.com.helpers.SettingsHelper;
import fastphrase.com.views.PlaybackListView.IQueue;

/**
 * Created by bob on 2/29/16.
 */
public class PlayButtonView extends FrameLayout implements IQueue{

    private ProgressBar mProgress;
    private long mPlayLengthMs;
    private PlayButtonTimer mPlayButtonTimer;
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

        this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingsHelper.onClick(getContext(), PlayButtonView.this);

                // let parent know the play button has been pressed
                if(mListener != null){
                    mListener.onPlayButtonPressed(PlayButtonView.this);
                }
            }
        });
    }


    @Override
    public void onAnimate() {
        mPlayButtonTimer = new PlayButtonTimer(new IPlayButtonTimerListener() {
            @Override
            public void onUpdate(int percentFilled) {
                mProgress.setProgress(percentFilled);
            }

            @Override
            public void onComplete() {
                mProgress.setProgress( 100 );
                mPlayButtonTimer.onDestroy();
                mPlayButtonTimer = null;
            }

        }, mPlayLengthMs);
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
        void onPlayButtonPressed(IQueue queueListener);
    }

    public interface IPlayButtonTimerListener{
        void onUpdate(int percentFilled);
        void onComplete();
    }

    /**
     * This class is used to help manage the button timer logic and calculate the
     *  percentage to fill the progress bar
     */
    public class PlayButtonTimer{

        private static final int UPDATE_FREQUENCY_MS = 25;

        private long mPlayLengthMs;
        private long mTimeButtonPressed;
        private IPlayButtonTimerListener mListener;
        private Handler mHandler = new Handler();

        public PlayButtonTimer(IPlayButtonTimerListener listener, long playLengthMs){
            mPlayLengthMs = playLengthMs;
            mTimeButtonPressed = System.currentTimeMillis();
            mListener = listener;
            mHandler.postDelayed(mRunnable, UPDATE_FREQUENCY_MS);
        }

        /**
         * Found that runnable is better for memory and updating UI
         */
        private Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - mTimeButtonPressed;

                if(elapsedTime > mPlayLengthMs){
                    if(mListener != null) {
                        mListener.onComplete();
                    }
                }else{
                    int percentFilled = (int) (((float) elapsedTime / (float) mPlayLengthMs) * 100);
                    if(mListener != null) {
                        mListener.onUpdate(percentFilled);
                    }
                    if(mHandler != null) {
                        mHandler.postDelayed(this, UPDATE_FREQUENCY_MS);
                    }
                }
            }
        };

        public void onDestroy(){
            mListener = null;
            mHandler = null;
            mRunnable = null;
        }
    }
}
