package fastphrase.com.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import fastphrase.com.R;

/**
 * Created by bob on 2/29/16.
 */
public class EmptyStateView extends LinearLayout{

    private ImageView mExpandingCircle;
    private Animation mAnimation;

    public EmptyStateView(Context context) {
        this(context, null);
    }

    public EmptyStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_empty_state, this, true);
        //expanding circle animation
        mExpandingCircle = (ImageView)view.findViewById(R.id.empty_view_circle);
        RecordFABView fab = (RecordFABView)findViewById(R.id.record_fab);
        mAnimation = AnimationUtils.loadAnimation(this.getContext(),R.anim.fab_select);
        mExpandingCircle.startAnimation(mAnimation);
    }
}
