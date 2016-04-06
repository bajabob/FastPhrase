package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import fastphrase.com.R;

public class TagView extends FrameLayout{
    private TextView mTag;

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_tag, this, true);
        mTag = (TextView) view.findViewById(R.id.tag);
    }

    public void setLabel(String label) {
        if(label.length() > 12){
            mTag.setText(label.substring(0,12) + "...");
        } else{
            mTag.setText(label);
        }
    }

}
