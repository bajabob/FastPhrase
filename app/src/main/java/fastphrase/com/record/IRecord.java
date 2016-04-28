package fastphrase.com.record;

import fastphrase.com.models.Recording;

/**
 * Created by bobtimm on 4/8/2016.
 */
public interface IRecord {

    void onRecordingStarted();
    void onRecordingComplete(Recording newRecording);
    void onRecordingException(boolean canStartRecordErrorFragment);
    
}
