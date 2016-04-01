package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import fastphrase.com.R;

public class ElapsedTimeView extends FrameLayout{

    private boolean mIsRunning = false;

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

    }

    /**
     * Is the timer currently running?
     * @return boolean
     */
    public boolean isRunning(){
        return mIsRunning;
    }

    /**
     * Start the timer counting
     */
    public void onStart(){
        // todo, start counting up!
    }

    /**
     * Stop the timer from counting
     */
    public void onStop(){
        // todo, stop counting!
    }

    /**
     * Get the total amount of milliseconds this timer has been active. If timer has not started,
     * return 0.
     * @return long
     */
    public long getElapsedTimeInMilliseconds(){
        return 0;
    }
}
