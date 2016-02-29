package fastphrase.com;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by bob on 2/28/16.
 */
public class FastPhraseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
