package fastphrase.com.record;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import fastphrase.com.R;
import fastphrase.com.helpers.RecordingFileSystem;
import fastphrase.com.models.Recording;
import fastphrase.com.record.audio.AudioRecorder;
import fastphrase.com.record.audio.AudioRecorderBuilder;

import java.util.Random;

/**
 * Simple custom view that reads input from the mic and displays the amplitude.
 */
public class AmplitudeView extends View {

    static final int MAX_DATA_POINTS = 500;
    static final int CURSOR_COLOUR = Color.GREEN;

    private boolean mIsRecording = false;
    private int mPos;
    private Paint mPaint;
    private int[] mAmplitudes = new int[MAX_DATA_POINTS];
    private Shader mShader;
    private Shader mGreyShader;

    private float mBandSize;
    private Rect mClipBounds = new Rect();

    private int mAmplitudePercentage = 0;
    private int mViewHeight = 0;

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
        mPos = 0;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        // Set up values that require the view to have been measured (i.e. require height and width values)
        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                try {
                    int viewHeight = AmplitudeView.this.getHeight();
                    int viewWidth = AmplitudeView.this.getWidth();
                    if (mViewHeight == 0) {
                        mViewHeight = viewHeight;
                    }
                    if (mBandSize == 0) {
                        mBandSize = viewWidth / (float) MAX_DATA_POINTS;
                    }
                    if (mShader == null) {
                        int[] gradientColours = new int[]{getResources().getColor(R.color.r500), Color.YELLOW, getResources().getColor(R.color.p500)};
                        mShader = new LinearGradient(0, 0, 0, mViewHeight, gradientColours, null, Shader.TileMode.MIRROR);
                    }
                    if (mGreyShader == null) {
                        int[] gradientColours = new int[]{Color.LTGRAY, Color.GRAY, Color.DKGRAY};
                        mGreyShader = new LinearGradient(0, 0, 0, mViewHeight, gradientColours, null, Shader.TileMode.MIRROR);
                    }
                    return true;
                } finally {
                    AmplitudeView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                }
            }
        });
    }

    public void onAmplitudeChange(int percentage){
        mAmplitudePercentage = percentage;
        mPos += 1;
        mPos %= MAX_DATA_POINTS;
        invalidate();
    }


    public void onRecordingStarted(){
        mIsRecording = true;
    }

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

            mAmplitudes[mPos] = (int)(((double)mAmplitudePercentage * 0.01) * (double)mViewHeight);

            mPaint.setShader( mIsRecording ? mShader : mGreyShader);

            for (int i=0; i<MAX_DATA_POINTS; i++) {
                canvas.drawRect(i* mBandSize, mViewHeight-mAmplitudes[i], i* mBandSize + mBandSize, mViewHeight, mPaint);
            }

            // draw cursor line
            mPaint.setColor(CURSOR_COLOUR);
            float cursorPos = mPos* mBandSize + mBandSize;
            canvas.drawLine(cursorPos, 0, cursorPos, mViewHeight, mPaint);
        }
    }

}
