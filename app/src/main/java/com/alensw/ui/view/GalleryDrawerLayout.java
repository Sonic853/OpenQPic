package com.alensw.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;
import com.alensw.PicFolder.R;

/** The gallery leaves the system bars outside the drawer scrim. */
public final class GalleryDrawerLayout extends DrawerLayout {
    private final Paint scrim = new Paint();
    private int scrimColor = 0x99000000;
    private View panel;

    public GalleryDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScrimColor(Color.TRANSPARENT);
        setDrawerElevation(0);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        panel = findViewById(R.id.drawer_panel);
    }

    @Override public void setScrimColor(int color) {
        scrimColor = color;
        invalidate();
    }

    @Override protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean drawn = super.drawChild(canvas, child, drawingTime);
        if (child != panel && panel.getRight() > 0 && panel.getWidth() > 0) {
            // The panel is anchored to the physical left in both layout directions.
            float offset = (float) panel.getRight() / panel.getWidth();
            scrim.setColor(scrimColor);
            scrim.setAlpha((int) (Color.alpha(scrimColor) * offset));
            canvas.drawRect(0, panel.getTop(), getWidth(), panel.getBottom(), scrim);
        }
        return drawn;
    }
}
