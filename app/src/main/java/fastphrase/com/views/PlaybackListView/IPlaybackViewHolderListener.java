package fastphrase.com.views.PlaybackListView;

import fastphrase.com.models.Recording;

/**
 * Created by bob on 2/28/16.
 */
public interface IPlaybackViewHolderListener {

    void onFolderOpened(int position);
    void onFolderClosed(int position);
    void onPlayRecording(Recording recording, int position);
}
