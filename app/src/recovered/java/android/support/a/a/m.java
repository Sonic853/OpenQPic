package android.support.a.a;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Morphing three-stroke drawer arrow with the gallery’s dimensions and theme color. */
public class m extends Drawable {
    private static volatile Method matrixSave;

    private static final float b = (float) Math.toRadians(45.0d);
    private final float c;
    private final float d;
    private final float e;
    private final float f;
    private final float g;
    private final boolean h;
    private final int j;
    private float l;

    private final Paint a = new Paint();
    private final Path i = new Path();
    private boolean k = false;

    public m(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.textColorPrimary, typedValue, true);
        this.j = (int) (24.0f * density);
        this.e = 18.0f * density;
        this.c = 2.0f * density;
        this.g = 3.2f * density;
        this.d = 11.31f * density;
        this.f = density * 16.0f;
        this.h = true;
        int color = context.getResources().getColor(typedValue.resourceId);
        this.a.setAntiAlias(true);
        this.a.setColor(color);
        this.a.setStyle(Paint.Style.STROKE);
        this.a.setStrokeJoin(Paint.Join.ROUND);
        this.a.setStrokeCap(Paint.Cap.SQUARE);
        this.a.setStrokeWidth(this.c);
    }

    private static float a(float start, float end, float progress) {
        return ((end - start) * progress) + start;
    }

    public void a(int i) {
        this.a.setColor(i);
        invalidateSelf();
    }

    protected void a(boolean z) {
        this.k = z;
    }

    protected boolean a() {
        return false;
    }

    public int b() {
        return this.a.getColor();
    }

    public void b(float f) {
        this.l = f;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        boolean rtl = a();
        float head = a(this.e, this.d, this.l);
        float shaft = a(this.e, this.f, this.l);
        float cut = a(0.0f, this.c / 2.0f, this.l);
        float angle = a(0.0f, b, this.l);
        float rotation = a(rtl ? 0.0f : -180.0f, rtl ? 180.0f : 0.0f, this.l);
        float gap = a(this.g + this.c, 0.0f, this.l);
        this.i.rewind();
        float left = -shaft / 2.0f;
        this.i.moveTo(left + cut, 0.0f);
        this.i.rLineTo(shaft - cut, 0.0f);
        float dx = Math.round(head * Math.cos(angle));
        float dy = Math.round(Math.sin(angle) * head);
        this.i.moveTo(left, gap);
        this.i.rLineTo(dx, dy);
        this.i.moveTo(left, -gap);
        this.i.rLineTo(dx, -dy);
        this.i.moveTo(0.0f, 0.0f);
        this.i.close();
        saveMatrix(canvas);
        if (this.h) {
            canvas.rotate((this.k ^ rtl ? -1 : 1) * rotation, bounds.centerX(), bounds.centerY());
        } else if (rtl) {
            canvas.rotate(180.0f, bounds.centerX(), bounds.centerY());
        }
        canvas.translate(bounds.centerX(), bounds.centerY());
        canvas.drawPath(this.i, this.a);
        canvas.restore();
    }

    // The original uses MATRIX_SAVE_FLAG, not the overload that also saves the clip.
    // save(int) is still a runtime method but no longer declared by the API 36 SDK.
    private static void saveMatrix(Canvas canvas) {
        try {
            Method save = matrixSave;
            if (save == null) {
                matrixSave = save = Canvas.class.getMethod("save", int.class);
            }
            save.invoke(canvas, 1);
        } catch (NoSuchMethodException e) {
            throw (NoSuchMethodError) new NoSuchMethodError("Canvas.save(int)").initCause(e);
        } catch (IllegalAccessException e) {
            throw (IllegalAccessError) new IllegalAccessError(e.getMessage()).initCause(e);
        } catch (InvocationTargetException e) {
            throw propagate(e.getCause());
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> RuntimeException propagate(Throwable cause) throws E {
        throw (E) cause;
    }

    @Override
    public int getIntrinsicHeight() {
        return this.j;
    }

    @Override
    public int getIntrinsicWidth() {
        return this.j;
    }

    @Override
    public int getOpacity() {
        return -3;
    }

    @Override
    public boolean isAutoMirrored() {
        return true;
    }

    @Override
    public void setAlpha(int i) {
        int color = this.a.getColor();
        this.a.setColor((color & 16777215) | (((Color.alpha(color) * i) / 255) << 24));
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.a.setColorFilter(colorFilter);
    }
}
