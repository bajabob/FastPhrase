package fastphrase.com.views.PlaybackListView;

import android.os.Handler;

import java.util.LinkedList;
import java.util.Queue;

import fastphrase.com.models.Recording;

/**
 * Created by bob on 4/18/16.
 */
public class PlaybackQueueManager {

    private static final int UPDATE_FREQUENCY_MS = 25;
    private Handler mHandler;
    
    private Queue<PlaybackElement> mPlaybackElements = new LinkedList<PlaybackElement>();

    public PlaybackQueueManager(){
        mHandler.postDelayed(mRunnable, UPDATE_FREQUENCY_MS);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - mTimeButtonPressed;

            if(elapsedTime > mPlayLengthMs){
                mListener.onComplete();
            }else{
                int percentFilled = (int) (((float) elapsedTime / (float) mPlayLengthMs) * 100);
                mListener.onUpdate(percentFilled);
                mHandler.postDelayed(this, UPDATE_FREQUENCY_MS);
            }
        }
    };

    public void onPlay(Recording recording, IQueue queueListener){

    }

    public class RecurringTimer{



        private Handler mHandler = new Handler();

        public PlayButtonTimer(){

        }



        public void onDestroy(){
            mListener = null;
            mHandler = null;
            mRunnable = null;
        }
    }

    public class PlaybackElement{
        private IQueue mQueueListener;
        private Recording mRecording;

        public PlaybackElement(Recording recording, IQueue queueListener){
            mRecording = recording;
            mQueueListener = queueListener;
        }
    }
}
