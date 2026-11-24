package android.support.a.a;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

/** Action-bar delegate using the API 18 home indicator methods. */
class i implements c {

    final Activity a;

    private i(Activity activity) {
        this.a = activity;
    }

    i(Activity activity, b unused) {
        this(activity);
    }

    @Override
    public Context a() {
        ActionBar actionBar = this.a.getActionBar();
        return actionBar != null ? actionBar.getThemedContext() : this.a;
    }

    @Override
    public void a(int i) {
        ActionBar actionBar = this.a.getActionBar();
        if (actionBar != null) {
            actionBar.setHomeActionContentDescription(i);
        }
    }

    @Override
    public void a(Drawable drawable, int i) {
        ActionBar actionBar = this.a.getActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(drawable);
            actionBar.setHomeActionContentDescription(i);
        }
    }
}
