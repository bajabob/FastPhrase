package fastphrase.com.views.PlaybackListView;

import android.content.Context;
import android.view.View;

/**
 * Created by bob on 2/28/16.
 */
public class EmptyViewHolder extends ViewHolder{

    public EmptyViewHolder(View view) {
        super(view);

        /**
         * This view is intentionally blank
         */

    }

    @Override
    void onBindData(PlaybackViewHolderData data, int position, Context context) {
        // intentionally blank
    }
}
