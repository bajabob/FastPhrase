package fastphrase.com.record.audio;

import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.os.Build;

/**
 * Created by bobtimm on 4/7/2016.
 */
public class ApiHelper {

    @SuppressLint("InlinedApi")
    public static final int DEFAULT_AUDIO_ENCODER =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1
                    ? MediaRecorder.AudioEncoder.AAC
                    : MediaRecorder.AudioEncoder.DEFAULT;
}
