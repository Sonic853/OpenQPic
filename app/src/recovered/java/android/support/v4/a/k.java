package android.support.v4.a;

import android.os.Build;
import android.view.VelocityTracker;

/** Pointer-specific velocity access with the pre-Honeycomb fallback. */
public class k {
    private k() {}

    static final n a;

    static {
        if (Build.VERSION.SDK_INT >= 11) {
            a = new m();
        } else {
            a = new l();
        }
    }

    public static float a(VelocityTracker velocityTracker, int i) {
        return a.a(velocityTracker, i);
    }

    public static float b(VelocityTracker velocityTracker, int i) {
        return a.b(velocityTracker, i);
    }
}
