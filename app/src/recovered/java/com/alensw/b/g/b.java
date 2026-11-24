package com.alensw.b.g;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.ArrayList;
import java.util.Iterator;

public class b implements Menu {
   public final int a;
   public final int b;
   public final int c;
   protected final Context d;
   protected final ArrayList<com.alensw.b.g.l> e = new ArrayList<>(8);
   private boolean f;

   public b(Context var1) {
      this.d = var1;
      Resources var2 = var1.getResources();
      Theme var4 = var1.getTheme();
      TypedValue var3 = new TypedValue();
      var4.resolveAttribute(com.alensw.PicFolder.R.attr.colorActionIcon, var3, true);
      this.a = var2.getColor(var3.resourceId);
      var4.resolveAttribute(com.alensw.PicFolder.R.attr.colorMenuIcon, var3, true);
      this.b = var2.getColor(var3.resourceId);
      this.c = var2.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_material_icon_size);
   }

   public static void a(Context var0, int var1, Menu var2) {
      int var3;
      int var4;
      int var5;
      try {
         ((Activity)var0).getMenuInflater().inflate(var1, var2);
         if (VERSION.SDK_INT < 11) {
            return;
         }

         var3 = com.alensw.ui.activity.ao.b(var0, com.alensw.PicFolder.R.attr.colorActionIcon);
         var5 = com.alensw.ui.activity.ao.b(var0);
         var4 = var2.size();
      } catch (Throwable var8) {
         return;
      }

      for (int var9 = 0; var9 < var4; var9++) {
         try {
            MenuItem var6 = var2.getItem(var9);
            if (var6.getIcon() == null) {
               com.alensw.b.g.l.a(var0, var6, var3, var5);
            }
         } catch (Throwable var7) {
            break;
         }
      }
   }

   public static void a(Context var0, View var1, com.alensw.b.g.k var2) {
      com.alensw.b.g.b var3 = new com.alensw.b.g.b(var0);
      if (var2.a(var3, var1)) {
         var3.a(var1);
      }
   }

   private static void a(Context var0, com.alensw.b.g.a var1, View var2, com.alensw.b.g.j var3) {
      try {
         if (VERSION.SDK_INT >= 11) {
            b(var0, var1, var2, var3);
         } else {
            c(var0, var1, var2, var3);
         }
      } catch (Throwable var4) {
      }
   }

   static void a(Context var0, com.alensw.b.g.a var1, CharSequence var2, com.alensw.b.g.j var3) {
      b(var0, var1, var2, var3);
   }

   static void a(PopupWindow var0, View var1, int var2, int var3) {
      b(var0, var1, var2, var3);
   }

   private SubMenu b(int var1, int var2, int var3, CharSequence var4) {
      com.alensw.b.g.l var5 = this.a(var1, var2, var3, var4);
      com.alensw.b.g.m var6 = new com.alensw.b.g.m(this.d, var5);
      var5.a(var6);
      var6.a(this.f);
      return var6;
   }

   @TargetApi(11)
   private static void b(Context var0, com.alensw.b.g.a var1, View var2, com.alensw.b.g.j var3) {
      int var5;
      byte var6;
      Rect var8;
      label34: {
         var6 = 0;
         var8 = new Rect();
         var2.getWindowVisibleDisplayFrame(var8);
         if (var0 instanceof Activity) {
            ActionBar var9 = ((Activity)var0).getActionBar();
            if (var9 != null) {
               var5 = var9.getHeight();
               break label34;
            }
         }

         var5 = 0;
      }

      int var7 = var8.height();
      int var4 = var7;
      if (var7 > var8.width()) {
         var4 = var7 - var5 * 3 / 2;
      }

      int[] var16 = var1.a();
      if (VERSION.SDK_INT >= 21) {
         var5 = 16843844;
      } else {
         var5 = 16843520;
      }

      ListPopupWindow var10 = new ListPopupWindow(var0, null, var5);
      var10.setAdapter(var1);
      var10.setAnchorView(var2);
      var10.setContentWidth(var16[0]);
      if (var16[1] > var4) {
         var10.setHeight(var4);
      }

      var10.setInputMethodMode(2);
      var10.setModal(true);
      var10.setOnItemClickListener(new com.alensw.b.g.f(var1, var10, var16, var4, var3));
      var5 = var2.getWidth();
      var4 = var10.getWidth();
      if (var5 > var4) {
         View var11 = var2.getRootView();
         if (var11.getWidth() > var11.getHeight()) {
            var4 = var6;
         } else {
            var4 = (var5 - var4) / 2;
         }

         var10.setHorizontalOffset(var4);
      }

      var10.show();
   }

   private static void b(Context var0, com.alensw.b.g.a var1, CharSequence var2, com.alensw.b.g.j var3) {
      AlertDialog var4 = com.alensw.ui.a.f.a(var0, 0, var1, new com.alensw.b.g.e(var1, var0, var3));
      if (var4 != null) {
         if (var2 != null) {
            var4.setTitle(var2);
         }

         com.alensw.ui.a.f.a(var4);
      }
   }

   private static void b(PopupWindow var0, View var1, int var2, int var3) {
      var0.getContentView().post(new com.alensw.b.g.i(var0, var2));
      if (var1.getHeight() >= var3) {
         var0.showAtLocation(var1, 1, 0, var1.getBottom());
      } else if (!var1.isShown()) {
         var1 = var1.getRootView();
         var0.showAtLocation(var1, 1, 0, var1.getBottom());
      } else {
         var0.showAsDropDown(var1);
      }
   }

   private static void c(Context var0, com.alensw.b.g.a var1, View var2, com.alensw.b.g.j var3) {
      Resources var6 = var0.getResources();
      int var4 = Math.round(var6.getDisplayMetrics().density * 6.0F);
      Drawable var8 = com.alensw.ui.d.d.a(com.alensw.ui.activity.ao.b(var0, com.alensw.PicFolder.R.attr.colorDialog), var4);
      ListView var7 = new ListView(var0);
      var7.setAdapter((ListAdapter)var1);
      var7.setDivider(null);
      PopupWindow var9 = new PopupWindow(var0);
      var9.setAnimationStyle(16973826);
      var9.setBackgroundDrawable(var8);
      var9.setContentView(var7);
      var9.setFocusable(true);
      var9.setTouchable(true);
      var9.setOutsideTouchable(true);
      var9.setInputMethodMode(2);
      int[] var10 = var1.a();
      int var5 = var6.getDisplayMetrics().heightPixels * 3 / 4;
      var9.setHeight(Math.min(var10[1] + var7.getDividerHeight() * (var1.getCount() - 1), var5) + var4 * 2);
      var9.setWindowLayoutMode(0, -2);
      var7.setScrollbarFadingEnabled(true);
      var7.setOnItemClickListener(new com.alensw.b.g.g(var1, var9, var2, var4, var5, var3));
      var7.setOnKeyListener(new com.alensw.b.g.h(var9));
      b(var9, var2, var10[0] + var4 * 2, var5);
   }

   public int a(int var1) {
      int var3 = this.e.size();
      int var2 = 0;

      while (true) {
         if (var2 >= var3) {
            var1 = -1;
            break;
         }

         if (((com.alensw.b.g.l)this.e.get(var2)).getItemId() == var1) {
            var1 = var2;
            break;
         }

         var2++;
      }

      return var1;
   }

   public com.alensw.b.g.l a(int var1, int var2, int var3, CharSequence var4) {
      com.alensw.b.g.l var5 = new com.alensw.b.g.l(this, this.d, var1, var2, var4);
      this.e.add(var5);
      return var5;
   }

   @TargetApi(13)
   public void a(View var1) {
        if (var1 == null) {
            a((CharSequence) null);
        } else {
            a(this.d, b(), var1, new com.alensw.b.g.c(this));
        }
    }

   public void a(CharSequence var1) {
      b(this.d, this.b(), var1, new com.alensw.b.g.d(this));
   }

   public void a(boolean var1) {
      this.f = var1;
   }

   public boolean a() {
      return this.f;
   }

   @Override
   public MenuItem add(int var1) {
      return this.a(0, 0, 0, this.d.getString(var1));
   }

   @Override
   public MenuItem add(int var1, int var2, int var3, int var4) {
      return this.a(var1, var2, var3, this.d.getString(var4));
   }

   @Override
   public MenuItem add(int var1, int var2, int var3, CharSequence var4) {
      return this.a(var1, var2, var3, var4);
   }

   @Override
   public MenuItem add(CharSequence var1) {
      return this.a(0, 0, 0, var1);
   }

   @Override
   public int addIntentOptions(int var1, int var2, int var3, ComponentName var4, Intent[] var5, Intent var6, int var7, MenuItem[] var8) {
      return 0;
   }

   @Override
   public SubMenu addSubMenu(int var1) {
      return this.b(0, 0, 0, this.d.getText(var1));
   }

   @Override
   public SubMenu addSubMenu(int var1, int var2, int var3, int var4) {
      return this.b(var1, var2, var3, this.d.getText(var4));
   }

   @Override
   public SubMenu addSubMenu(int var1, int var2, int var3, CharSequence var4) {
      return this.b(var1, var2, var3, var4);
   }

   @Override
   public SubMenu addSubMenu(CharSequence var1) {
      return this.b(0, 0, 0, var1);
   }

   public com.alensw.b.g.a b() {
      com.alensw.b.g.a var1 = new com.alensw.b.g.a(this.d, this.b);
      var1.a(this);
      return var1;
   }

   public void b(int var1) {
      if (var1 >= 0 && var1 < this.e.size()) {
         this.e.remove(var1);
      }
   }

   @Override
   public void clear() {
      this.e.clear();
   }

   @Override
   public void close() {
   }

   @Override
   public MenuItem findItem(int var1) {
      Iterator var3 = this.e.iterator();

      Object var2;
      while (true) {
         if (!var3.hasNext()) {
            var2 = null;
            break;
         }

         var2 = (com.alensw.b.g.l)var3.next();
         if (((com.alensw.b.g.l)var2).getItemId() == var1) {
            break;
         }

         if (((com.alensw.b.g.l)var2).hasSubMenu()) {
            var2 = ((com.alensw.b.g.l)var2).getSubMenu().findItem(var1);
            if (var2 != null) {
               break;
            }
         }
      }

      return (MenuItem)var2;
   }

   @Override
   public MenuItem getItem(int var1) {
      return (MenuItem)this.e.get(var1);
   }

   @Override
   public boolean hasVisibleItems() {
      Iterator var2 = this.e.iterator();

      boolean var1;
      while (true) {
         if (var2.hasNext()) {
            if (!((com.alensw.b.g.l)var2.next()).isVisible()) {
               continue;
            }

            var1 = true;
            break;
         }

         var1 = false;
         break;
      }

      return var1;
   }

   @Override
   public boolean isShortcutKey(int var1, KeyEvent var2) {
      return false;
   }

   @Override
   public boolean performIdentifierAction(int var1, int var2) {
      return false;
   }

   @Override
   public boolean performShortcut(int var1, KeyEvent var2, int var3) {
      return false;
   }

   @Override
   public void removeGroup(int var1) {
      for (int var2 = this.e.size() - 1; var2 >= 0; var2--) {
         if (((com.alensw.b.g.l)this.e.get(var2)).getGroupId() == var1) {
            this.b(var2);
         }
      }
   }

   @Override
   public void removeItem(int var1) {
      this.b(this.a(var1));
   }

   @Override
   public void setGroupCheckable(int var1, boolean var2, boolean var3) {
        int n2 = 0;
        int n3 = var2 ? 1 : 0;
        if (var3) {
            n2 = 16;
        }
        Iterator iterator = this.e.iterator();
        while (iterator.hasNext()) {
            com.alensw.b.g.l srcType0191 = (com.alensw.b.g.l)iterator.next();
            if (srcType0191.getGroupId() != var1) continue;
            srcType0191.a(n3 | n2);
        }
        return;
    }

   @Override
   public void setGroupEnabled(int var1, boolean var2) {
      for (com.alensw.b.g.l var3 : this.e) {
         if (var3.getGroupId() == var1) {
            var3.setEnabled(var2);
         }
      }
   }

   @Override
   public void setGroupVisible(int var1, boolean var2) {
      for (com.alensw.b.g.l var4 : this.e) {
         if (var4.getGroupId() == var1) {
            var4.setVisible(var2);
         }
      }
   }

   @Override
   public void setQwertyMode(boolean var1) {
   }

   @Override
   public int size() {
      return this.e.size();
   }
}
