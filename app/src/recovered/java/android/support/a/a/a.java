package android.support.a.a;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

/** Connects the action-bar home indicator to the start drawer. */
public class a implements android.support.v4.widget.d {

    private final Activity a;

    private final c b;
    private final DrawerLayout c;
    private f d;
    private Drawable e;
    private boolean f = true;
    private boolean g;
    private final int h;
    private final int i;

    public a(Activity activity, DrawerLayout drawerLayout, Object obj, int i, int i2) {
        this.a = activity;
        this.b = a(activity);
        this.c = drawerLayout;
        this.h = i;
        this.i = i2;
        if (obj instanceof Drawable) {
            this.d = new j(this, activity, (Drawable) obj, 0.33333334f, null);
        } else {
            e eVar = new e(activity, this.b.a());
            if (obj instanceof Integer) {
                eVar.a(((Integer) obj).intValue());
            }
            this.d = eVar;
        }
        this.e = c();
    }

    public static c a(Activity activity) {
        if (activity instanceof d) {
            return ((d) activity).a();
        }
        if (Build.VERSION.SDK_INT >= 18) {
            return new i(activity, null);
        }
        return Build.VERSION.SDK_INT >= 11 ? new h(activity, null) : new g(activity);
    }

    public static m a(Activity activity, int i) {
        c cVarA = a(activity);
        e eVar = new e(activity, cVarA.a());
        eVar.a(i);
        eVar.a(1.0f);
        cVarA.a(eVar, 0);
        return eVar;
    }

    public static boolean b(Activity activity) {
        return Build.VERSION.SDK_INT >= 17 && activity.getWindow().getDecorView().getLayoutDirection() == 1;
    }

    private void d() {
        if (this.c.f(8388611)) {
            this.c.d(8388611);
        } else {
            this.c.c(8388611);
        }
    }

    public void a() {
        if (this.c.e(8388611)) {
            this.d.a(1.0f);
        } else {
            this.d.a(0.0f);
        }
        if (this.f) {
            a((Drawable) this.d, this.c.e(8388611) ? this.i : this.h);
        }
    }

    public void a(float f) {
        float fMin = Math.min(1.0f, Math.max(0.0f, f));
        if (this.f) {
            this.d.a(fMin);
        } else if (this.e instanceof f) {
            ((f) this.e).a(fMin);
        }
    }

    void a(int i) {
        this.b.a(i);
    }

    public void a(Configuration configuration) {
        if (!this.g) {
            this.e = c();
        }
        a();
    }

    void a(Drawable drawable, int i) {
        this.b.a(drawable, i);
    }

    @Override
    public void a(View view) {
        this.d.a(1.0f);
        if (this.f) {
            a(this.i);
        }
    }

    @Override
    public void a(View view, float f) {
        this.d.a(Math.min(1.0f, Math.max(0.0f, f)));
    }

    @Override
    public void a(View view, boolean z, int i) {
    }

    public void a(boolean z) {
        if (z != this.f) {
            if (z) {
                a((Drawable) this.d, this.c.e(8388611) ? this.i : this.h);
            } else {
                a(this.e, 0);
            }
            this.f = z;
        }
    }

    public boolean a(MenuItem menuItem) {
        if (menuItem == null || menuItem.getItemId() != 16908332 || !this.f) {
            return false;
        }
        d();
        return true;
    }

    @Override
    public void b(View view) {
        this.d.a(0.0f);
        if (this.f) {
            a(this.h);
        }
    }

    public boolean b() {
        return this.f;
    }

    Drawable c() {
        e eVar = new e(this.a, a(this.a).a());
        if (this.d instanceof e) {
            eVar.a(((e) this.d).b());
        }
        eVar.a(1.0f);
        return eVar;
    }
}
