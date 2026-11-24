package android.support.v4.a;

import android.os.Build;

/** GravityCompat: delegates absolute gravity calculation by API level. */
public class a {
    private a() {}

    static final b a;

    static {
        if (Build.VERSION.SDK_INT >= 17) {
            a = new d();
        } else {
            a = new c();
        }
    }

    public static int a(int i, int i2) {
        return a.a(i, i2);
    }
}
