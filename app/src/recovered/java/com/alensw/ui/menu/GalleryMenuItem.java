package com.alensw.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.SparseIntArray;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import com.alensw.svg.SvgPicture;
import com.alensw.ui.activity.GalleryChromeActivity;
public class GalleryMenuItem implements MenuItem {
   public static final int[][] a;
   public static final SparseIntArray b;
   private final GalleryMenu c;
   private final Context d;
   private final int e;
   private final int f;
   private int g;
   private CharSequence h;
   private Drawable i;
   private Object j;
   private SubMenu k;
   private OnMenuItemClickListener l;

   static {
      int[] var3 = new int[]{com.alensw.PicFolder.R.id.add, com.alensw.PicFolder.R.raw.ic_menu_add};
      int[] var7 = new int[]{com.alensw.PicFolder.R.id.delete, com.alensw.PicFolder.R.drawable.ic_bar_delete};
      int[] var2 = new int[]{com.alensw.PicFolder.R.id.done, com.alensw.PicFolder.R.raw.ic_menu_done};
      int[] var9 = new int[]{com.alensw.PicFolder.R.id.download, com.alensw.PicFolder.R.raw.ic_menu_download};
      int[] var6 = new int[]{com.alensw.PicFolder.R.id.flip, com.alensw.PicFolder.R.raw.ic_menu_flip};
      int[] var8 = new int[]{com.alensw.PicFolder.R.id.help, com.alensw.PicFolder.R.drawable.ic_bar_help};
      int[] var4 = new int[]{com.alensw.PicFolder.R.id.rotation, com.alensw.PicFolder.R.raw.ic_menu_rotation};
      int[] var5 = new int[]{com.alensw.PicFolder.R.id.search, com.alensw.PicFolder.R.raw.ic_menu_search};
      int[] var10 = new int[]{com.alensw.PicFolder.R.id.select, com.alensw.PicFolder.R.drawable.ic_bar_select};
      a = new int[][]{
         var3,
         {com.alensw.PicFolder.R.id.camera, com.alensw.PicFolder.R.raw.ic_menu_camera},
         {com.alensw.PicFolder.R.id.clear, com.alensw.PicFolder.R.raw.ic_menu_cancel},
         {com.alensw.PicFolder.R.id.crop, com.alensw.PicFolder.R.raw.ic_menu_crop},
         var7,
         var2,
         var9,
         var6,
         var8,
         {com.alensw.PicFolder.R.id.retry, com.alensw.PicFolder.R.raw.ic_menu_retry},
         {com.alensw.PicFolder.R.id.rotate, com.alensw.PicFolder.R.raw.ic_menu_rotate},
         var4,
         var5,
         var10,
         {com.alensw.PicFolder.R.id.share, com.alensw.PicFolder.R.drawable.ic_bar_share},
         {com.alensw.PicFolder.R.id.shrink, com.alensw.PicFolder.R.raw.ic_menu_shrink},
         {com.alensw.PicFolder.R.id.slideshow, com.alensw.PicFolder.R.drawable.ic_bar_slideshow}
      };
      b = new SparseIntArray(a.length);

      for (int[] var12 : a) {
         b.put(var12[0], var12[1]);
      }
   }

   public GalleryMenuItem(GalleryMenu var1, Context var2, int var3, int var4, CharSequence var5) {
      this.c = var1;
      this.d = var2;
      this.e = var3;
      this.f = var4;
      this.h = var5;
   }

   public static Drawable a(Context var0, MenuItem var1) {
      Resources var2 = var0.getResources();
      return a(var0, var1, GalleryChromeActivity.b(var0, com.alensw.PicFolder.R.attr.colorActionIcon), var2.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_material_icon_size));
   }

   public static Drawable a(Context var0, MenuItem var1, int var2, int var3) {
        int i3 = b.get(var1.getItemId());
        Drawable drawable = null;
        if (i3 != 0) {
            Resources resources = var0.getResources();
            try {
                drawable = SvgPicture.a(resources, i3, var2, var3);
            } catch (Throwable th) {
                drawable = resources.getDrawable(i3);
            }
            if (drawable != null) {
                var1.setIcon(drawable);
            }
        }
        return drawable;
    }

   public static Drawable a(Context var0, Object var1, int var2, int var3) {
      if (var1 != null) {
         label37: {
            int var4;
            Resources var5;
            try {
               if (!(var1 instanceof Integer)) {
                  break label37;
               }

               var5 = var0.getResources();
               var4 = (Integer)var1;
            } catch (Throwable var9) {
               return null;
            }

            try {
               return SvgPicture.a(var5, var4, var2, var3);
            } catch (Throwable var8) {
               try {
                  return var5.getDrawable(var4);
               } catch (Throwable var7) {
                  return null;
               }
            }
         }

         try {
            if (var1 instanceof ResolveInfo) {
               return ((ResolveInfo)var1).loadIcon(var0.getPackageManager());
            }
         } catch (Throwable var6) {
         }
      }

      return null;
   }

   public static GalleryMenuItem a(View var0) {
      return new GalleryMenuItem(null, var0.getContext(), 0, var0.getId(), null);
   }

   public MenuItem a(ResolveInfo var1) {
      this.j = var1;
      return this;
   }

   public GalleryMenuItem a(int var1) {
      this.g = var1;
      return this;
   }

   public void a() {
      if (this.j != null) {
         this.i = null;
      }
   }

   protected void a(SubMenu var1) {
      this.k = var1;
   }

   public int b() {
      return this.g;
   }

   protected boolean c() {
      boolean var1;
      if (this.l != null && this.l.onMenuItemClick(this)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @Override
   public boolean collapseActionView() {
      return false;
   }

   @Override
   public boolean expandActionView() {
      return false;
   }

   @Override
   public ActionProvider getActionProvider() {
      return null;
   }

   @Override
   public View getActionView() {
      return null;
   }

   @Override
   public char getAlphabeticShortcut() {
      return '\u0000';
   }

   @Override
   public int getGroupId() {
      return this.e;
   }

   @Override
   public Drawable getIcon() {
      Drawable var2;
      if (this.i != null) {
         var2 = this.i;
      } else {
         if (this.j == null) {
            int var1 = b.get(this.f);
            if (var1 != 0) {
               this.j = var1;
            }
         }

         if (this.j != null) {
            this.i = a(this.d, this.j, this.c.a, this.c.c);
         }

         var2 = this.i;
      }

      return var2;
   }

   @Override
   public Intent getIntent() {
      return null;
   }

   @Override
   public int getItemId() {
      return this.f;
   }

   @Override
   public ContextMenuInfo getMenuInfo() {
      return null;
   }

   @Override
   public char getNumericShortcut() {
      return '\u0000';
   }

   @Override
   public int getOrder() {
      return 0;
   }

   @Override
   public SubMenu getSubMenu() {
      return this.k;
   }

   @Override
   public CharSequence getTitle() {
      return this.h;
   }

   @Override
   public CharSequence getTitleCondensed() {
      return this.h;
   }

   @Override
   public boolean hasSubMenu() {
      boolean var1;
      if (this.k != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @Override
   public boolean isActionViewExpanded() {
      return false;
   }

   @Override
   public boolean isCheckable() {
      boolean var1 = true;
      if ((this.g & 1) != 1) {
         var1 = false;
      }

      return var1;
   }

   @Override
   public boolean isChecked() {
      boolean var1;
      if ((this.g & 2) == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @Override
   public boolean isEnabled() {
      boolean var1;
      if ((this.g & 4) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @Override
   public boolean isVisible() {
      boolean var1;
      if ((this.g & 8) == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @Override
   public MenuItem setActionProvider(ActionProvider var1) {
      return this;
   }

   @Override
   public MenuItem setActionView(int var1) {
      return this;
   }

   @Override
   public MenuItem setActionView(View var1) {
      return this;
   }

   @Override
   public MenuItem setAlphabeticShortcut(char var1) {
      return this;
   }

   @Override
   public MenuItem setCheckable(boolean var1) {
      if (var1) {
         this.g |= 1;
      } else {
         this.g &= -2;
      }

      return this;
   }

   @Override
   public MenuItem setChecked(boolean var1) {
      if (var1 && this.e != 0 && (this.g & 16) != 0) {
         int var3 = this.c.size();

         for (int var2 = 0; var2 < var3; var2++) {
            MenuItem var4 = this.c.getItem(var2);
            if (var4.getGroupId() == this.e && var4 instanceof GalleryMenuItem) {
               GalleryMenuItem var5 = (GalleryMenuItem)var4;
               var5.g &= -3;
            }
         }
      }

      if (var1) {
         this.g |= 2;
      } else {
         this.g &= -3;
      }

      return this;
   }

   @Override
   public MenuItem setEnabled(boolean var1) {
      if (!var1) {
         this.g |= 4;
      } else {
         this.g &= -5;
      }

      return this;
   }

   @Override
   public MenuItem setIcon(int var1) {
      Integer var2;
      if (var1 != 0) {
         var2 = var1;
      } else {
         var2 = null;
      }

      this.j = var2;
      return this;
   }

   @Override
   public MenuItem setIcon(Drawable var1) {
      this.i = var1;
      this.j = null;
      return this;
   }

   @Override
   public MenuItem setIntent(Intent var1) {
      return this;
   }

   @Override
   public MenuItem setNumericShortcut(char var1) {
      return this;
   }

   @Override
   public MenuItem setOnActionExpandListener(OnActionExpandListener var1) {
      return this;
   }

   @Override
   public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener var1) {
      this.l = var1;
      return this;
   }

   @Override
   public MenuItem setShortcut(char var1, char var2) {
      return this;
   }

   @Override
   public void setShowAsAction(int var1) {
   }

   @Override
   public MenuItem setShowAsActionFlags(int var1) {
      return null;
   }

   @Override
   public MenuItem setTitle(int var1) {
      if (var1 != 0) {
         this.setTitle(this.d.getString(var1));
      }

      return this;
   }

   @Override
   public MenuItem setTitle(CharSequence var1) {
      this.h = var1;
      return this;
   }

   @Override
   public MenuItem setTitleCondensed(CharSequence var1) {
      return this;
   }

   @Override
   public MenuItem setVisible(boolean var1) {
      if (!var1) {
         this.g |= 8;
      } else {
         this.g &= -9;
      }

      return this;
   }
}
