package fastphrase.com.record;

import android.support.annotation.NonNull;

/**
 * Created by bobtimm on 4/7/2016.
 */
public class AudioRecorderBuilder {

    private String mFileName;
    private AudioRecorder.MediaRecorderConfig mConfig;
    private boolean mIsLoggable;

    private AudioRecorderBuilder() {
    }

    public static AudioRecorderBuilder with() {
        final AudioRecorderBuilder audioRecorderBuilder = new AudioRecorderBuilder();
        return audioRecorderBuilder;
    }

    public AudioRecorderBuilder fileName(@NonNull String targetFileName) {
        mFileName = targetFileName;
        return this;
    }

    public AudioRecorderBuilder config(@NonNull AudioRecorder.MediaRecorderConfig config) {
        mConfig = config;
        return this;
    }

    public AudioRecorderBuilder loggable() {
        mIsLoggable = true;
        return this;
    }

    /**
     * Returns a ready-to-use AudioRecorder.<p>
     * {@link android.media.MediaRecorder} config by default.<p>
     * Logs are turned off by default.
     */
    public AudioRecorder build() {
        if (mFileName == null) {
            throw new RuntimeException("Target filename is not set: use `#fileName` method");
        }
        return new AudioRecorder(mFileName, mConfig, mIsLoggable);
    }
}
