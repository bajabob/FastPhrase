package fastphrase.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import fastphrase.com.R;

public class RecordButtonView extends FrameLayout{

    public RecordButtonView(Context context) {
        this(context, null);
    }

    public RecordButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View) inflater.inflate(R.layout.view_record_button, this, true);

    }

    /**
     * Who is listening for the button press?
     * @param listener IRecordButtonListener
     */
    public void setRecordButtonListener(IRecordButtonListener listener){
        // todo
    }



    public interface IRecordButtonListener{
        void onStartRecording();
        void onStopRecording();
    }
}
