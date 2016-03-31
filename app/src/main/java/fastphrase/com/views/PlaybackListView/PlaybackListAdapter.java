package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import fastphrase.com.AppDataManager;
import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;

/**
 * Created by bob on 2/28/16.
 */
public class PlaybackListAdapter extends RecyclerView.Adapter<FolderViewHolder> {

    private AppDataPresenter mPresenter;
    private IPlaybackViewHolderListener mListener;
    private WeakReference<Context> mContext;


    public PlaybackListAdapter(Context context, IPlaybackViewHolderListener listener){
        mPresenter = new AppDataPresenter(context);
        mListener = listener;
        mContext = new WeakReference<Context>(context);
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if(viewType == R.layout.viewholder_folder){
            FolderViewHolder holder = new FolderViewHolder(v);
            holder.setPlaybackViewHolderListener(mListener);
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        if(holder != null){
            holder.onBindData(mPresenter.get(position), position, mContext.get());
        }
    }

    @Override
    public int getItemViewType(int position){
        return R.layout.viewholder_folder;
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

                // create folder name
                PlaybackViewHolderData folder = new PlaybackViewHolderData();
                folder.tag = tag;
                folder.recordings = new ArrayList<Recording>();

                for(Recording r : appData.getRecordings(tag)) {
                    r.afterLoad(appData.getTags());
                    folder.recordings.add(r);
                }

                mList.add(folder);
            }

            // get all recordings that don't have tags and put in "untagged" folder
            List<Recording> untagged = appData.getRecordingsWithoutTags();
            if(untagged.size() > 0){

                Tag tag = new Tag(context.getString(R.string.untagged));

                // create folder name
                PlaybackViewHolderData folder = new PlaybackViewHolderData();
                folder.tag = tag;
                folder.recordings = new ArrayList<Recording>();

                for(Recording r : untagged) {
                    r.afterLoad(appData.getTags());
                    folder.recordings.add(r);
                }

                mList.add(folder);
            }

        }

        public PlaybackViewHolderData get(int position){
            return mList.get(position);
        }

        public int getItemCount(){
            return mList.size();
        }

    }
}
