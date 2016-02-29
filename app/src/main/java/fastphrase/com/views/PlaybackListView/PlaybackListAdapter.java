package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fastphrase.com.AppDataManager;
import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;

/**
 * Created by bob on 2/28/16.
 */
public class PlaybackListAdapter extends RecyclerView.Adapter<PlaybackViewHolder> {

    private AppDataPresenter mPresenter;
    private IPlaybackViewHolderListener mListener;


    public PlaybackListAdapter(Context context, IPlaybackViewHolderListener listener){
        mPresenter = new AppDataPresenter(context);
        mListener = listener;
    }

    @Override
    public PlaybackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if(viewType == R.layout.viewholder_folder){
            FolderViewHolder holder = new FolderViewHolder(v);
            holder.setPlaybackViewHolderListener(mListener);
            return holder;
        }

        if(viewType == R.layout.viewholder_recording){
            RecordingViewHolder holder = new RecordingViewHolder(v);
            holder.setPlaybackViewHolderListener(mListener);
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(PlaybackViewHolder holder, int position) {
        if(holder != null){
            holder.onBindData(mPresenter.get(position), position);
        }
    }

    @Override
    public int getItemViewType(int postion){
        return mPresenter.getViewHolderId(postion);
    }

    @Override
    public int getItemCount() {
        if(mPresenter == null) {
            return 0;
        }
        return mPresenter.getItemCount();
    }


    /**
     * This class converts app data to a form that we can use more easily
     *  App stores data in simple lists. This DS stores everything in filtered folders.
     *  This DS is unique to this part of the app
     */
    public class AppDataPresenter{
        private List<PlaybackViewHolderData> mList;

        public AppDataPresenter(Context context){
            AppDataManager appData = new AppDataManager(context);

            mList = new ArrayList<PlaybackViewHolderData>();

            // get all recordings that have tags and put in filtered list
            for(Tag tag : appData.getTags()){

                // create folder name (by leaving recording null, it will register as a folder)
                PlaybackViewHolderData folder = new PlaybackViewHolderData();
                folder.tag = tag;
                mList.add(folder);

                for(Recording r : appData.getRecordings(tag)) {
                    PlaybackViewHolderData recording = new PlaybackViewHolderData();
                    recording.recording = r;
                    recording.tag = tag;
                    mList.add(recording);
                }
            }

            // get all recordings that don't have tags and put in "untagged" folder
            List<Recording> untagged = appData.getRecordingsWithoutTags();
            if(untagged.size() > 0){

                Tag tag = new Tag(context.getString(R.string.untagged));

                // create folder name (by leaving recording null, it will register as a folder)
                PlaybackViewHolderData folder = new PlaybackViewHolderData();
                folder.tag = tag;
                mList.add(folder);

                for(Recording r : appData.getRecordingsWithoutTags()) {
                    PlaybackViewHolderData recording = new PlaybackViewHolderData();
                    recording.recording = r;
                    recording.tag = tag;
                    mList.add(recording);
                }
            }

        }

        public PlaybackViewHolderData get(int position){
            return mList.get(position);
        }

        public int getItemCount(){
            return mList.size();
        }

        public int getViewHolderId(int position){
            PlaybackViewHolderData data = mList.get(position);
            if(data.isFolder()){
                return R.layout.viewholder_folder;
            }
            return R.layout.viewholder_recording;
        }

    }
}
