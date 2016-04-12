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
import fastphrase.com.IPlaybackController;
import fastphrase.com.R;
import fastphrase.com.models.Recording;
import fastphrase.com.models.Tag;
import fastphrase.com.views.FolderView;

/**
 * Created by bob on 2/28/16.
 */
public class PlaybackListAdapter extends RecyclerView.Adapter<FolderViewHolder>
    implements FolderView.IFolderListener{

    private AppDataPresenter mPresenter;
    private WeakReference<Context> mContext;
    private IPlaybackController mListener;


    public PlaybackListAdapter(Context context){
        onRefreshData(context);
        mContext = new WeakReference<Context>(context);
    }

    public void onRefreshData(Context context){
        mPresenter = new AppDataPresenter(context);
        this.notifyDataSetChanged();
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if(viewType == R.layout.viewholder_folder){
            FolderViewHolder holder = new FolderViewHolder(v);
            holder.setPlaybackController(mListener);
            holder.setFolderListener(this);
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
        if(mPresenter == null || mPresenter.getItemCount() == 0) {
            if(mListener != null){
                mListener.onPlaybackListEmpty();
            }
            return 0;
        }
        if(mListener != null){
            mListener.onPlaybackListHasElements();
        }
        return mPresenter.getItemCount();
    }

    public void setPlaybackController(IPlaybackController listener){
        mListener = listener;
    }


    @Override
    public void onFolderOpened(int position) {
        if(mListener != null){
            mListener.onFolderOpened(position);
        }
        if(mPresenter != null){
            mPresenter.openFolder(position);
        }
    }

    @Override
    public void onFolderClosed(int position) {
        if(mListener != null){
            mListener.onFolderClosed(position);
        }
        if(mPresenter != null){
            mPresenter.closeFolder(position);
        }
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
                    folder.recordings.add(r);
                }

                mList.add(folder);
            }

            // get all recordings that don't have tags and put in "untagged" folder
            List<Recording> untagged = appData.getRecordingsWithoutTags();
            if(untagged.size() > 0){

                Tag tag = new Tag(context.getString(R.string.untagged_ucf));

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

        public void openFolder(int position){
            mList.get(position).isFolderOpen = true;
        }

        public void closeFolder(int position){
            mList.get(position).isFolderOpen = false;
        }

    }
}
