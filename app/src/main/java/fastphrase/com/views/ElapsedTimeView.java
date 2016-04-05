package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.os.Handler;
import android.widget.TextView;

import fastphrase.com.R;

public class ElapsedTimeView extends FrameLayout{

    private ElapsedTimeTimer mElapsedTimeTimer;
    private TextView mText;

    public ElapsedTimeView(Context context) {
        this(context, null);
    }

    public ElapsedTimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElapsedTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_elapsed_time, this, true);

        mText = (TextView) view.findViewById(R.id.chronometer);
        mText.setText("00:00:00");

    }

    /**
     * Is the timer currently running?
     * @return boolean
     */
    public boolean isRunning(){
        if(mElapsedTimeTimer != null){
            return mElapsedTimeTimer.isRunning();
        }
        return false;
    }

    /**
     * Start the timer counting
     */
    public void onStart(){
        mElapsedTimeTimer = new ElapsedTimeTimer(new IElapsedTimeListener() {
            @Override
            public void onUpdate(String text) {
                mText.setText(text);
            }
        });
    }

    /**
     * Stop the timer from counting
     */
    public void onStop(){
        if(mElapsedTimeTimer != null){
            mElapsedTimeTimer.stop();
        }
    }

    public interface IElapsedTimeListener{
        void onUpdate(String text);
    }

    public long getElapsedTimeInMilliseconds(){
        if(mElapsedTimeTimer != null){
            return mElapsedTimeTimer.getElapsedTime();
        }
        return 0;
    }

    public class ElapsedTimeTimer{
        private static final int UPDATE_FREQUENCY_MS = 25;
        private long mTimeButtonPressed;
        private long mElapsedTime;
        private IElapsedTimeListener mListener;
        private boolean mIsRunning = true;
        private Handler mHandler = new Handler();

        public ElapsedTimeTimer(IElapsedTimeListener listener){
            mListener = listener;
            mTimeButtonPressed = System.currentTimeMillis();
            mHandler.postDelayed(mRunnable, UPDATE_FREQUENCY_MS);
        }

        private Runnable mRunnable = new Runnable() {
            @Override
            public void run() {

                mElapsedTime = System.currentTimeMillis() - mTimeButtonPressed;
                int h = (int) (mElapsedTime / 3600000);
                int m = (int) (mElapsedTime - h * 3600000) / 60000;
                int s = (int) (mElapsedTime - h * 3600000 - m * 60000) / 1000;
                long ms = (int) (mElapsedTime - h * 360000 - m * 60000 - s * 1000);
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                String mms = ms < 10 ? "0" + ms : ms + "";
                mms = mms.substring(0,2);
                mListener.onUpdate(mm + ":" + ss + ":" + mms);

                if(mIsRunning) {
                    mHandler.postDelayed(this, UPDATE_FREQUENCY_MS);
                }
            }
        };

        public boolean isRunning(){
            return mIsRunning;
        }

        public long getElapsedTime(){
            return mElapsedTime;
        }

        public void stop(){
            mIsRunning = false;
        }

    }

}
