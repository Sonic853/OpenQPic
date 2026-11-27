package com.alensw.PicFolder;

import android.app.ActionBar;
import android.content.Context;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;
import com.alensw.ui.c.cu;
import com.alensw.ui.view.GalleryArrow;

final class GalleryDrawerToggle extends DrawerLayout.SimpleDrawerListener {
    private final GalleryActivity activity;
    private final DrawerLayout drawer;
    private final GalleryArrow drawerArrow, backArrow;
    private boolean enabled = true;

    GalleryDrawerToggle(GalleryActivity activity, DrawerLayout drawer, int color) {
        this.activity = activity;
        this.drawer = drawer;
        ActionBar bar = activity.getActionBar();
        Context context = bar != null ? bar.getThemedContext() : activity;
        drawerArrow = new GalleryArrow(context);
        backArrow = new GalleryArrow(context);
        drawerArrow.setColor(color);
        backArrow.setColor(color);
    }

    private boolean rtl() {
        return drawer.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    void syncIndicator() {
        drawerArrow.setDrawerProgress(drawer.isDrawerOpen(Gravity.LEFT) ? 1 : 0, rtl());
        backArrow.setDrawerProgress(1, rtl());
        ActionBar bar = activity.getActionBar();
        if (bar != null) {
            bar.setHomeAsUpIndicator(enabled ? drawerArrow : backArrow);
            bar.setHomeActionContentDescription(enabled ? R.string.app_name : 0);
        }
    }

    boolean isEnabled() { return enabled; }

    void setEnabled(boolean value) {
        if (enabled != value) {
            enabled = value;
            syncIndicator();
        }
    }

    void setProgress(float value) {
        (enabled ? drawerArrow : backArrow).setDrawerProgress(value, rtl());
    }

    boolean onOptionsItemSelected(MenuItem item) {
        if (!enabled || item == null || item.getItemId() != android.R.id.home) return false;
        int lockMode = drawer.getDrawerLockMode(Gravity.LEFT);
        if (drawer.isDrawerVisible(Gravity.LEFT)) {
            if (lockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN) drawer.closeDrawer(Gravity.LEFT);
        } else if (lockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            drawer.openDrawer(Gravity.LEFT);
        }
        return true;
    }

    @Override public void onDrawerSlide(View panel, float offset) {
        drawerArrow.setDrawerProgress(offset, rtl());
    }

    @Override public void onDrawerOpened(View panel) {
        drawerArrow.setDrawerProgress(1, rtl());
        activity.setTitle(R.string.app_name);
        activity.a((CharSequence) null);
        GalleryActivity.d(activity).requestFocus();
    }

    @Override public void onDrawerClosed(View panel) {
        drawerArrow.setDrawerProgress(0, rtl());
        cu page = activity.c();
        if (page != null) {
            activity.setTitle(page.B());
            activity.a(page.C());
            page.D().requestFocus();
        }
    }
}
