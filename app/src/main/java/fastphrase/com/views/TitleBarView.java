package fastphrase.com.views;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import fastphrase.com.R;

/**
 * Created by bob on 2/29/16.
 */
public class TitleBarView extends FrameLayout{

    private FrameLayout mBackground;
    private ImageView mLogo;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_title_bar, this, true);

        mBackground = (FrameLayout)view.findViewById(R.id.background);
        mLogo = (ImageView)view.findViewById(R.id.logo);
    }

    /**
     * TODO: need method to control whether view is "green" or "red"
     *  Should call these, initPlaybackMode, initRecordingMode.
     *  Make sure these methods update the colors and use the correct colors
     */

    public void setPlaybackTitleBar() {
        mLogo.setImageResource(R.drawable.logo_playback_white);
        mBackground.setBackgroundColor(getResources().getColor(R.color.p500));
    }

    public void setRecordingTitleBar() {
        mLogo.setImageResource(R.drawable.logo_recording_white);
        mBackground.setBackgroundColor(getResources().getColor(R.color.r500));
    }

}
