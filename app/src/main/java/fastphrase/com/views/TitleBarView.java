package fastphrase.com.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import fastphrase.com.AboutActivity;
import fastphrase.com.R;

/**
 * Created by bob on 2/29/16.
 */
public class TitleBarView extends FrameLayout{

    private FrameLayout mBackground;
    private ImageView mLogo;
    private ImageButton mAboutButton;

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
        mAboutButton = (ImageButton)view.findViewById(R.id.about_button);

        mAboutButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onAboutActivityRequested();
            }
        });
    }

    public void setPlaybackTitleBar() {
        mLogo.setImageResource(R.drawable.logo_playback_white);
        mBackground.setBackgroundColor(getResources().getColor(R.color.p500));
        mAboutButton.setVisibility(View.VISIBLE);
    }

    public void setRecordingTitleBar() {
        mLogo.setImageResource(R.drawable.logo_recording_white);
        mBackground.setBackgroundColor(getResources().getColor(R.color.r500));
        mAboutButton.setVisibility(View.GONE);
    }

    public void onAboutActivityRequested(){
        Intent intent = AboutActivity.newInstance(this.getContext());
        this.getContext().startActivity(intent);
    }

}
