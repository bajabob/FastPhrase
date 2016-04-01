package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import fastphrase.com.views.ElapsedTimeView;
import fastphrase.com.views.TitleBarView;

public class RecordingActivity extends AppCompatActivity{

    private ElapsedTimeView mElapsedTime;

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

        mElapsedTime = (ElapsedTimeView) findViewById(R.id.elapsed_time);

        /**
         * TESTING CODE
         */
        Button timerToggle = (Button) findViewById(R.id.test_timer);
        timerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mElapsedTime.isRunning()){
                    Log.d("Timer", "onStop");
                    mElapsedTime.onStop();
                }else{
                    Log.d("Timer", "onStart");
                    mElapsedTime.onStart();
                }
            }
        });
        /***************/
    }

}
