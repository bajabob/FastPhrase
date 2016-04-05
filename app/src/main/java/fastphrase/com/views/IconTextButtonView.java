package fastphrase.com.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.internal.app.AppCompatViewInflater;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fastphrase.com.R;

public class IconTextButtonView extends FrameLayout{

    private View mBackground;
    private View mListener;
    private TextView mText;
    private ImageView mIcon;

    public IconTextButtonView(Context context) {
        this(context, null);
    }

    public IconTextButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconTextButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_button_icon_text, this, true);

        mText = (TextView) view.findViewById(R.id.text);
        mIcon = (ImageView) view.findViewById(R.id.icon);
        mListener = (View) view.findViewById(R.id.listener);
        mBackground = (View) view.findViewById(R.id.container);
    }

    public void setLabel(String label) {
        mText.setText(label);
    }

    public void setIcon(Drawable drawable){
        mIcon.setImageDrawable(drawable);
    }

    public void setBackgroundColor(int color){
        mBackground.setBackgroundColor(color);
    }

    public void setOnClickListener(OnClickListener listener){
        mListener.setOnClickListener(listener);
    }
}
