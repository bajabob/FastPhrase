package fastphrase.com.helpers;

import java.util.Random;

/**
 * Created by bob on 4/6/16.
 */
public class Hash {

    public static long generate() {
        Random r = new Random(System.currentTimeMillis());
        return Math.abs(r.nextLong());
    }
}
