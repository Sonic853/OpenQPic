package android.support.v4.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.EdgeEffect;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/** EdgeEffect with the original reflective EdgeGlow fallback. */
public class j {
    private static Constructor c;
    private static Method d;
    private static Method e;
    private static Method f;
    private static Method g;
    private static Method h;
    private static Method i;
    private static Method j;

    private EdgeEffect a;

    private Object b;

    static {
        if (Build.VERSION.SDK_INT < 14) {
            try {
                Class<?> cls = Class.forName("android.widget.EdgeGlow");
                c = cls.getConstructor(Drawable.class, Drawable.class);
                d = cls.getMethod("setSize", Integer.TYPE, Integer.TYPE);
                e = cls.getMethod("draw", Canvas.class);
                f = cls.getMethod("isFinished", new Class[0]);
                g = cls.getMethod("onAbsorb", Integer.TYPE);
                h = cls.getMethod("onPull", Float.TYPE);
                i = cls.getMethod("onRelease", new Class[0]);
                j = cls.getMethod("finish", new Class[0]);
            } catch (Throwable th) {
            }
        }
    }

    @TargetApi(14)
    public j(Context context) {
        if (Build.VERSION.SDK_INT >= 14) {
            this.a = new EdgeEffect(context);
            return;
        }
        try {
            Resources resources = context.getResources();
            this.b = c.newInstance(resources.getDrawable(resources.getIdentifier("overscroll_edge", "drawable", "android")), resources.getDrawable(resources.getIdentifier("overscroll_glow", "drawable", "android")));
        } catch (Throwable th) {
        }
    }

    private void a(Method method, Object... objArr) {
        if (method == null || this.b == null) {
            return;
        }
        try {
            method.invoke(this.b, objArr);
        } catch (Throwable th) {
        }
    }

    private boolean b(Method method, Object... objArr) {
        if (method != null && this.b != null) {
            try {
                return ((Boolean) method.invoke(this.b, objArr)).booleanValue();
            } catch (Throwable th) {
            }
        }
        return false;
    }

    public void a(int i2) {
        if (this.a != null) {
            this.a.onAbsorb(i2);
        } else {
            a(g, Integer.valueOf(i2));
        }
    }

    public void a(int i2, int i3) {
        if (this.a != null) {
            this.a.setSize(i2, i3);
        } else {
            a(d, Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    public boolean a() {
        return this.a != null ? this.a.isFinished() : b(f, new Object[0]);
    }

    public boolean a(float f2) {
        return a(f2, 0.5f);
    }

    public boolean a(float f2, float f3) {
        if (this.a == null) {
            a(h, Float.valueOf(f2));
        } else if (Build.VERSION.SDK_INT >= 21) {
            this.a.onPull(f2, f3);
        } else {
            this.a.onPull(f2);
        }
        return true;
    }

    public boolean a(Canvas canvas) {
        return this.a != null ? this.a.draw(canvas) : b(e, canvas);
    }

    public boolean b() {
        if (this.a != null) {
            this.a.onRelease();
        } else {
            a(i, new Object[0]);
        }
        return a();
    }
}
