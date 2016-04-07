package fastphrase.com.record;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import fastphrase.com.R;

/**
 * Simple custom view that reads input from the mic and displays the amplitude.
 */
public class AmplitudeView extends View {

    static final int MAX_DATA_POINTS = 500;

    private boolean mIsRecording = false;
    private Paint mPaint;
    private List<Amplitude> mAmplitudes = new ArrayList<>();
    private Shader mShader;
    private Shader mGreyShader;
    private float mViewHeightDp = 0;
    private float mViewWidthDp = 0;
    private Rect mClipBounds = new Rect();

    public AmplitudeView(Context context) {
        super(context);
        init();
    }

    public AmplitudeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmplitudeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        // create MAX_DATA_POINTS (zero'ed) entries
        // makes view fill right to left
        Amplitude a = new Amplitude();
        for( int i = 0; i < MAX_DATA_POINTS; i++ ){
            mAmplitudes.add(a);
        }

        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                try {
                    int viewHeight = AmplitudeView.this.getHeight();
                    int viewWidth = AmplitudeView.this.getWidth();
                    if (mViewHeightDp == 0) {
                        mViewHeightDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, viewHeight, getResources().getDisplayMetrics());
                    }
                    if (mViewWidthDp == 0) {
                        mViewWidthDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, viewWidth, getResources().getDisplayMetrics());
                    }
                    if (mShader == null) {
                        int[] gradientColours = new int[]{getResources().getColor(R.color.r500), Color.YELLOW, getResources().getColor(R.color.p500)};
                        mShader = new LinearGradient(0, 0, 0, mViewHeightDp, gradientColours, null, Shader.TileMode.MIRROR);
                    }
                    if (mGreyShader == null) {
                        int[] gradientColours = new int[]{Color.LTGRAY, Color.GRAY, Color.DKGRAY};
                        mGreyShader = new LinearGradient(0, 0, 0, mViewHeightDp, gradientColours, null, Shader.TileMode.MIRROR);
                    }
                    return true;
                } finally {
                    AmplitudeView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                }
            }
        });
    }

    public void onAmplitudeChange(int percentage){

        if(percentage <= 3){
            percentage = (int)Math.round(Math.random() * 5);
        }

        // add new amplitude to array
        Amplitude a = new Amplitude();
        a.isRecording = mIsRecording;
        a.height = (int)(((double)percentage * 0.01) * (double)mViewHeightDp);
        mAmplitudes.add(a);

        // redraw view
        invalidate();
    }

    /**
     * Call to make view change to "active"
     */
    public void onRecordingStarted(){
        mIsRecording = true;
    }

    /**
     * Call to make view change to "non-active"
     */
    public void onRecordingStopped(){
        mIsRecording = false;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.getClipBounds(mClipBounds);

        if (!mClipBounds.isEmpty()) {

            if(mAmplitudes.size() > 0) {

                float bandWidth = mViewWidthDp / mAmplitudes.size();

                float position = 0;
                Iterator<Amplitude> itr = mAmplitudes.iterator();
                while (itr.hasNext()) {
                    Amplitude a = itr.next();
                    mPaint.setShader(a.isRecording ? mShader : mGreyShader);
                    canvas.drawRect(position, mViewHeightDp - a.height, position + bandWidth, mViewHeightDp, mPaint);
                    position += bandWidth;
                }

                while (mAmplitudes.size() > MAX_DATA_POINTS) {
                    mAmplitudes.remove(0);
                }
            }
        }
    }

    public class Amplitude{
        boolean isRecording = false;
        int height = 0;
    }
}
