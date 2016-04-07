package fastphrase.com.record;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Created by bobtimm on 4/7/2016.
 */
public class AudioRecorder {
    private static final String TAG = "AudioRecorder";

    private static final int MAX_AMPLITUDE = 32767;
    private static final int AMPLITUDE_SAMPLE_RATE_MILLISECONDS = 50;

    public enum Status {
        STATUS_UNKNOWN,
        STATUS_READY_TO_RECORD,
        STATUS_RECORDING,
        STATUS_RECORD_PAUSED
    }

    public interface OnException {
        void onException(Exception e);
    }

    public interface OnStartListener extends OnException {
        void onStarted();
        void onAmplitudeChange(int percentage);
    }

    public interface OnPauseListener extends OnException {
        void onPaused(String activeRecordFileName);
    }

    /**
     * @author lassana
     * @since 10/06/2013
     */
    public static class MediaRecorderConfig {
        private final int mAudioEncodingBitRate;
        private final int mAudioChannels;
        private final int mAudioSource;
        private final int mAudioEncoder;

        public static final MediaRecorderConfig DEFAULT =
                new MediaRecorderConfig(
                        /* 128 Kib per second            */
                        128 * 1024,
                        /* Stereo                       */
                        2,
                        /* Default audio source (usually, device microphone)  */
                        MediaRecorder.AudioSource.DEFAULT,
                        /* Default encoder for the target Android version   */
                        ApiHelper.DEFAULT_AUDIO_ENCODER);

        /**
         * Constructor.
         *
         * @param audioEncodingBitRate Used for {@link android.media.MediaRecorder#setAudioEncodingBitRate}
         * @param audioChannels        Used for {@link android.media.MediaRecorder#setAudioChannels}
         * @param audioSource          Used for {@link android.media.MediaRecorder#setAudioSource}
         * @param audioEncoder         Used for {@link android.media.MediaRecorder#setAudioEncoder}
         */
        public MediaRecorderConfig(int audioEncodingBitRate, int audioChannels, int audioSource, int audioEncoder) {
            mAudioEncodingBitRate = audioEncodingBitRate;
            mAudioChannels = audioChannels;
            mAudioSource = audioSource;
            mAudioEncoder = audioEncoder;
        }

    }

    class StartRecordTask extends AsyncTask<OnStartListener, Void, Exception> {

        private OnStartListener mOnStartListener;

        @Override
        protected Exception doInBackground(OnStartListener... params) {
            this.mOnStartListener = params[0];
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioEncodingBitRate(mMediaRecorderConfig.mAudioEncodingBitRate);
            mMediaRecorder.setAudioChannels(mMediaRecorderConfig.mAudioChannels);
            mMediaRecorder.setAudioSource(mMediaRecorderConfig.mAudioSource);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setOutputFile(mTargetRecordFileName);
            mMediaRecorder.setAudioEncoder(mMediaRecorderConfig.mAudioEncoder);

            Exception exception = null;
            try {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
            } catch (IOException e) {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);
            if (e == null) {
                setStatus(AudioRecorder.Status.STATUS_RECORDING);
                mOnStartListener.onStarted();

                mAmpHandler = new Handler();
                mAmpRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if(mMediaRecorder != null && isRecording()) {
                            // convert to percentage
                            mOnStartListener.onAmplitudeChange(((getMaxAmplitude() * 100) / MAX_AMPLITUDE));
                            mAmpHandler.postDelayed(this, AMPLITUDE_SAMPLE_RATE_MILLISECONDS);
                        }
                    }
                };
                mAmpHandler.postDelayed(mAmpRunnable, AMPLITUDE_SAMPLE_RATE_MILLISECONDS);
            } else {
                setStatus(AudioRecorder.Status.STATUS_READY_TO_RECORD);
                mOnStartListener.onException(e);
            }
        }
    }

    class PauseRecordTask extends AsyncTask<OnPauseListener, Void, Exception> {
        private OnPauseListener mOnPauseListener;

        @Override
        protected Exception doInBackground(OnPauseListener... params) {
            mOnPauseListener = params[0];
            Exception exception = null;
            try {
                mMediaRecorder.stop();
                mMediaRecorder.release();
            } catch (Exception e) {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);
            if (e == null) {
                setStatus(AudioRecorder.Status.STATUS_RECORD_PAUSED);
                mOnPauseListener.onPaused(mTargetRecordFileName);
            } else {
                setStatus(AudioRecorder.Status.STATUS_READY_TO_RECORD);
                mOnPauseListener.onException(e);
            }
        }
    }

    private Status mStatus;
    private MediaRecorder mMediaRecorder;
    private final String mTargetRecordFileName;
    private final MediaRecorderConfig mMediaRecorderConfig;
    private final boolean mIsLoggable;
    private Handler mAmpHandler;
    private Runnable mAmpRunnable;

    /* package-local */ AudioRecorder(@NonNull final String targetRecordFileName,
                                      @NonNull final MediaRecorderConfig mediaRecorderConfig,
                                      final boolean isLoggable) {
        mTargetRecordFileName = targetRecordFileName;
        mMediaRecorderConfig = mediaRecorderConfig;
        mIsLoggable = isLoggable;

        mStatus = Status.STATUS_READY_TO_RECORD;
    }

    /**
     * Continues an existing record or starts a new one.
     */
    @SuppressLint("NewApi")
    public void start(@NonNull final OnStartListener listener) {
        StartRecordTask task = new StartRecordTask();
        task.execute(listener);

    }

    /**
     * Pauses an active recording.
     */
    @SuppressLint("NewApi")
    public void pause(@NonNull final OnPauseListener listener) {
        PauseRecordTask task = new PauseRecordTask();
        task.execute(listener);
    }

    /**
     * Returns the current recording status.
     *
     */
    public Status getStatus() {
        return mStatus;
    }

    /**
     * Returns the current record filename.
     */
    public String getRecordFileName() {
        return mTargetRecordFileName;
    }

    /**
     * Returns true if record is started, false if not.
     */
    public boolean isRecording() {
        return mStatus == Status.STATUS_RECORDING;
    }

    /**
     * Returns true if record can be started, false if not.
     */
    public boolean isReady() {
        return mStatus == Status.STATUS_READY_TO_RECORD;
    }

    /**
     * Returns true if record is paused, false if not.
     */
    public boolean isPaused() {
        return mStatus == Status.STATUS_RECORD_PAUSED;
    }

    private void setStatus(@NonNull final Status status) {
        mStatus = status;
    }

    public int getMaxAmplitude(){
        if(mMediaRecorder != null && isRecording()){
            try {
                return mMediaRecorder.getMaxAmplitude();
            } catch (RuntimeException e){
                Log.e("Audio Recorder", e.getLocalizedMessage());
            }
        }
        return 0;
    }

    /**
     * Drops the current recording.
     */
    public void cancel() {
        if(mStatus == Status.STATUS_RECORDING) {
            try {
                if (mMediaRecorder != null) {
                    mMediaRecorder.stop();
                    mMediaRecorder.release();
                }
            } catch (Exception e) {
                error("Exception during record cancelling", e);
            }
        }
        mStatus = Status.STATUS_UNKNOWN;
    }

    private void debug(@NonNull final String msg, @Nullable final Exception e) {
        if (mIsLoggable) Log.d(TAG, msg, e);
    }

    private void error(@NonNull final String msg, @Nullable final Exception e) {
        if (mIsLoggable) Log.e(TAG, msg, e);
    }

}
