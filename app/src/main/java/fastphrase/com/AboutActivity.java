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

    private CheckBox mHaptic;

    public static Intent newInstance(Context context){
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mHaptic = (CheckBox)findViewById(R.id.haptic_checkbox);

        mHaptic.setChecked(SettingsHelper.getHapticPreference(this));

        mHaptic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsHelper.onClick(buttonView.getContext(), buttonView);
                SettingsHelper.setHapticPreference(buttonView.getContext(), isChecked);
            }
        });
    }
}
