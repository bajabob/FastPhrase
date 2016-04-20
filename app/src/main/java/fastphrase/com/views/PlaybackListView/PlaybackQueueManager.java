package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import fastphrase.com.R;
import fastphrase.com.helpers.RecordingFileSystem;
import fastphrase.com.models.Recording;

/**
 * Created by bob on 4/18/16.
 */
public class PlaybackQueueManager {

    private static final int UPDATE_FREQUENCY_MS = 25;

    private Context mContext;
    private boolean mIsRunning = true;
    private Handler mHandler = new Handler();
    private Queue<PlaybackElement> mPlaybackElements = new LinkedList<PlaybackElement>();

    public PlaybackQueueManager(Context context){
        mContext = context;
        mHandler.postDelayed(mRunnable, UPDATE_FREQUENCY_MS);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            PlaybackElement top = mPlaybackElements.peek();

            if(top != null) {

                if (top.isComplete()) {
                    mPlaybackElements.poll();
                } else if (top.isQueued()) {
                    top.onPlay(mContext);
                }

            }
            if(mIsRunning) {
                mHandler.postDelayed(this, UPDATE_FREQUENCY_MS);
            }
        }
    };

    public void onPlay(Recording recording, IQueue queueListener){
        mPlaybackElements.add(new PlaybackElement(recording, queueListener));
        if(mPlaybackElements != null && mPlaybackElements.size() > 1){
            queueListener.onQueued();
        }
    }

    public void onDestroy(){
        mIsRunning = false;
        mRunnable = null;
        mContext = null;
    }

    public class PlaybackElement{

        private final static int STATE_QUEUED = 0;
        private final static int STATE_PLAYING = 1;
        private final static int STATE_COMPLETE = 2;

        private int mPlayState = STATE_QUEUED;
        private IQueue mQueueListener;
        private Recording mRecording;

        public PlaybackElement(Recording recording, IQueue queueListener){
            mRecording = recording;
            mQueueListener = queueListener;
        }

        public boolean isComplete(){
            return mPlayState == STATE_COMPLETE;
        }

        public boolean isQueued(){
            return mPlayState == STATE_QUEUED;
        }

        public void onPlay(Context context){
            RecordingFileSystem rfs = new RecordingFileSystem(mRecording);
            File recordingFile = new File(rfs.getFilenameAndPath());

            if( !recordingFile.exists()){
                String noRecording = context.getString(R.string.error_empty_recording);
                Toast.makeText(context, noRecording, Toast.LENGTH_LONG).show();
                mQueueListener.onError();
                mPlayState = STATE_COMPLETE;
            } else {
                Log.d("Recording", "Play: " + mRecording.label);
                MediaPlayer mp = MediaPlayer.create(context, Uri.fromFile(recordingFile));
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                        mPlayState = STATE_COMPLETE;
                    }
                });
                mp.start();
                mPlayState = STATE_PLAYING;
                mQueueListener.onAnimate();
            }
        }
    }
}
