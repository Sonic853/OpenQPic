package com.alensw.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/** The gallery's three-stroke icon, shared by the drawer, theme preview and player. */
public final class GalleryArrow extends Drawable {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private final float density;
    private float progress;
    private boolean mirror, rtl;

    public GalleryArrow(Context context) {
        density = context.getResources().getDisplayMetrics().density;
        TypedValue color = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, color, true);
        paint.setColor(color.resourceId == 0 ? color.data : context.getColor(color.resourceId));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeWidth(2 * density);
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidateSelf();
    }

    public void setProgress(float value) {
        progress = value;
        invalidateSelf();
    }

    public void setDrawerProgress(float value, boolean rightToLeft) {
        if (value == 0 || value == 1) mirror = value == 1;
        rtl = rightToLeft;
        setProgress(value);
    }

    @Override public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float head = (18 + (11.31f - 18) * progress) * density;
        float shaft = (18 - 2 * progress) * density;
        float cut = density * progress;
        double angle = Math.toRadians(45) * progress;
        float gap = 5.2f * density * (1 - progress);
        float left = -shaft / 2;
        float dx = Math.round(head * Math.cos(angle));
        float dy = Math.round(head * Math.sin(angle));
        path.rewind();
        path.moveTo(left + cut, 0);
        path.rLineTo(shaft - cut, 0);
        path.moveTo(left, gap);
        path.rLineTo(dx, dy);
        path.moveTo(left, -gap);
        path.rLineTo(dx, -dy);
        path.moveTo(0, 0);
        path.close();
        int save = canvas.save();
        canvas.translate(bounds.centerX(), bounds.centerY());
        canvas.rotate((mirror ^ rtl ? -1 : 1) * ((rtl ? 0 : -180) + 180 * progress));
        canvas.drawPath(path, paint);
        canvas.restoreToCount(save);
    }

    @Override public int getIntrinsicHeight() { return (int) (24 * density); }
    @Override public int getIntrinsicWidth() { return getIntrinsicHeight(); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
    @Override public boolean isAutoMirrored() { return true; }
    @Override public void setAlpha(int alpha) {
        paint.setAlpha(Color.alpha(paint.getColor()) * alpha / 255);
        invalidateSelf();
    }
    @Override public void setColorFilter(ColorFilter filter) {
        paint.setColorFilter(filter);
        invalidateSelf();
    }
}
