package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import fastphrase.com.R;

/**
 * Created by bob on 2/29/16.
 */
public class PlayButtonView extends FrameLayout{

    private long mPlayLengthMs;
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
