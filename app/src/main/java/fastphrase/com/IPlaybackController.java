package fastphrase.com;

import fastphrase.com.models.Recording;

/**
 * Created by bobtimm on 4/5/2016.
 */
public interface IPlaybackController {
    void onRequestEditRecordingDialog(long recordingHash);
    void onFolderOpened(int position);
    void onFolderClosed(int position);
    void onPlayRecording(Recording recording);
    void onPlaybackListEmpty();
    void onPlaybackListHasElements();
}
