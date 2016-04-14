package fastphrase.com.helpers;

import android.view.HapticFeedbackConstants;
import android.view.View;

/**
 * Created by Justin on 4/13/2016.
 */
public class HapticClickHelper {
    public static void onClick(View view){
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
    }
}
