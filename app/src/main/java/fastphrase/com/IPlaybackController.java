package fastphrase.com;

import fastphrase.com.models.Recording;
import fastphrase.com.views.PlaybackListView.IQueue;

/**
 * Created by bobtimm on 4/5/2016.
 */
public interface IPlaybackController {
    void onRequestEditRecordingDialog(long recordingHash);
    void onFolderOpened(int position);
    void onFolderClosed(int position);
    void onPlayRecording(Recording recording, IQueue queueListener);
    void onPlaybackListEmpty();
    void onPlaybackListHasElements();
}
