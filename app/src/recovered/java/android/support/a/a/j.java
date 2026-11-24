package android.support.a.a;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;

/** Inset indicator that slides and mirrors with drawer progress. */
class j extends InsetDrawable implements f {

    final a a;

    private final boolean b;
    private final Rect c;
    private final Activity d;
    private float e;
    private float f;

    private j(a aVar, Activity activity, Drawable drawable, float f) {
        super(drawable, 0);
        this.a = aVar;
        this.b = Build.VERSION.SDK_INT > 18;
        this.c = new Rect();
        this.d = activity;
        this.f = f;
    }

    j(a toggle, Activity activity, Drawable drawable, float offset, b unused) {
        this(toggle, activity, drawable, offset);
    }

    @Override
    public void a(float f) {
        this.e = f;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        copyBounds(this.c);
        canvas.save();
        boolean zB = android.support.a.a.a.b(this.d);
        int i = zB ? -1 : 1;
        int iWidth = this.c.width();
        canvas.translate(i * (-this.f) * iWidth * this.e, 0.0f);
        if (zB && !this.b) {
            canvas.translate(iWidth, 0.0f);
            canvas.scale(-1.0f, 1.0f);
        }
        super.draw(canvas);
        canvas.restore();
    }
}
