package android.support.a.a;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.lang.reflect.Method;

/** Cached action-bar methods, or the fallback up-indicator view. */
class l {

    public Method a;

    public Method b;
    public ImageView c;

    l(Activity activity) {
        try {
            this.a = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
            this.b = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
        } catch (NoSuchMethodException e) {
            this.c = k.a(activity, null);
        }
    }
}
