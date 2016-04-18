package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by bob on 2/28/16.
 */
abstract class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(View view) {
        super(view);
    }

    abstract void onBindData(PlaybackViewHolderData data, int position, Context context);
}
