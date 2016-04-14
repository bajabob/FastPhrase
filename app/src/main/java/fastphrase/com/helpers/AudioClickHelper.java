package fastphrase.com.helpers;

import android.view.SoundEffectConstants;
import android.view.View;

/**
 * Created by Justin on 4/13/2016.
 */
public class AudioClickHelper {
    public static void onClick(View view){
        view.playSoundEffect(SoundEffectConstants.CLICK);
    }
}
