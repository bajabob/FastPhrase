package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import fastphrase.com.record.RecordFragment;
import fastphrase.com.views.TitleBarView;

public class RecordingActivity extends AppCompatActivity {

    public static Intent newInstance(Context context){
        Intent intent = new Intent(context, RecordingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        TitleBarView titleBarView = (TitleBarView)findViewById(R.id.title);
        titleBarView.setRecordingTitleBar();

        if (savedInstanceState == null) {
            Fragment fragment = RecordFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, fragment).commit();
        }

    }


    @Override
    public void onBackPressed() {
        /**
         * Todo, tell fragments to discard recording
         */
        finish();
    }


}
