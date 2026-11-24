package android.support.a.a;

import android.app.Activity;
import android.content.Context;

/** Drawer arrow whose mirror state follows the drag progress. */
class e extends m implements f {

    private final Activity a;

    public e(Activity activity, Context context) {
        super(context);
        this.a = activity;
    }

    @Override
    public void a(float f) {
        if (f == 1.0f) {
            a(true);
        } else if (f == 0.0f) {
            a(false);
        }
        super.b(f);
    }

    @Override
    protected boolean a() {
        return android.support.a.a.a.b(this.a);
    }
}
