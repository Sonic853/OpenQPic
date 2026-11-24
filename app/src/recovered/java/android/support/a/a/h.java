package android.support.a.a;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

/** Honeycomb action-bar delegate. */
class h implements c {

    final Activity a;

    l b;

    private h(Activity activity) {
        this.a = activity;
    }

    h(Activity activity, b unused) {
        this(activity);
    }

    @Override
    public Context a() {
        return this.a;
    }

    @Override
    public void a(int i) {
        this.b = k.a(this.b, this.a, i);
    }

    @Override
    public void a(Drawable drawable, int i) {
        this.b = k.a(this.b, this.a, drawable, i);
    }
}
