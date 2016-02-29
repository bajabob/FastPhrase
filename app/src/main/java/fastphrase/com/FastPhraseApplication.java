package fastphrase.com;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import fastphrase.com.helpers.FontsOverride;

/**
 * Created by bob on 2/28/16.
 */
public class FastPhraseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        /**
         * Font implementation found here:
         *  http://stackoverflow.com/questions/2711858/is-it-possible-to-set-font-for-entire-application/16883281#16883281
         *
         * For more awesome fonts,
         *  http://stackoverflow.com/questions/12128331/how-to-change-fontfamily-of-textview-in-android
         */
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Roboto-Light.ttf");
    }
}
