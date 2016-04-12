package fastphrase.com.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fastphrase.com.R;

/**
 * Created by bobtimm on 3/31/2016.
 */
public class AreYouSureDialog extends DialogFragment{

    private DialogListener mListener;

    public static AreYouSureDialog newInstance(){
        AreYouSureDialog f = new AreYouSureDialog();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    public void setDialogListener(DialogListener listener){
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_are_you_sure, container, false);

        // cancel button
        Button cancel = (Button) v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dismiss();
                            mListener.onCancel();
                        }
                    }, 400);
                } else {
                    throw new RuntimeException("listener is null, not expected");
                }
            }
        });

        // yes button
        Button yes = (Button) v.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dismiss();
                            mListener.onYes();
                        }
                    }, 400);
                } else {
                    throw new RuntimeException("listener is null, not expected");
                }
            }
        });

        return v;
    }

    public interface DialogListener{
        void onYes();
        void onCancel();
    }
}
