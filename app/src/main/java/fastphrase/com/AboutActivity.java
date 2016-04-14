package fastphrase.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import fastphrase.com.helpers.SettingsHelper;

public class AboutActivity extends AppCompatActivity {

    private CheckBox mAudio, mHaptic;

    public static Intent newInstance(Context context){
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mAudio = (CheckBox)findViewById(R.id.audio_checkbox);
        mHaptic = (CheckBox)findViewById(R.id.haptic_checkbox);

        mAudio.setChecked(SettingsHelper.getAudioPreference(this));
        mHaptic.setChecked(SettingsHelper.getHapticPreference(this));

        mAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsHelper.onClick(buttonView.getContext(), buttonView);
                SettingsHelper.setAudioPreference(buttonView.getContext(), isChecked);
            }
        });

        mHaptic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsHelper.onClick(buttonView.getContext(), buttonView);
                SettingsHelper.setHapticPreference(buttonView.getContext(), isChecked);
            }
        });
    }
}
