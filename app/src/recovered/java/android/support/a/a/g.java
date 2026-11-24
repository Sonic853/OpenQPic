package android.support.a.a;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/** Home image delegate used before ActionBar. */
class g implements c {

    final Activity a;

    private View b;

    g(Activity activity) {
        this.a = activity;
    }

    @Override
    public Context a() {
        return this.a;
    }

    @Override
    public void a(int i) {
    }

    @Override
    public void a(Drawable drawable, int i) {
        if (this.b == null) {
            this.b = this.a.findViewById(R.id.home);
        }
        if (this.b instanceof ImageView) {
            ((ImageView) this.b).setImageDrawable(drawable);
        }
    }
}
