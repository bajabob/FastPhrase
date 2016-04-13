package fastphrase.com.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fastphrase.com.R;

/**
 * Created by bob on 4/8/16.
 */
public class RecordErrorFragment extends Fragment{

    private ICallback mCallback;

    public static RecordErrorFragment newInstance(){
        RecordErrorFragment f = new RecordErrorFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_record_error, container, false);

        Button retry = (Button) v.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null){
                    mCallback.onRetry();
                }else{
                    throw new RuntimeException("Callback is null, not expected");
                }
            }
        });

        return v;
    }

    /**
     * Set the parent callback for this fragment
     * @param callback
     */
    public void setIRecordCallback(ICallback callback){
        mCallback = callback;
    }

    public interface ICallback{
        void onRetry();
    }
}
