package com.alensw.ui.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class GallerySubMenu extends GalleryMenu implements SubMenu {
    private final MenuItem f;

    public GallerySubMenu(Context context, MenuItem menuItem) {
        super(context);
        this.f = menuItem;
    }

    @Override // android.view.SubMenu
    public void clearHeader() {
    }

    @Override // android.view.SubMenu
    public MenuItem getItem() {
        return this.f;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderIcon(int i) {
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderIcon(Drawable drawable) {
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderTitle(int i) {
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderTitle(CharSequence charSequence) {
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setHeaderView(View view) {
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setIcon(int i) {
        this.f.setIcon(i);
        return this;
    }

    @Override // android.view.SubMenu
    public SubMenu setIcon(Drawable drawable) {
        this.f.setIcon(drawable);
        return this;
    }
}
