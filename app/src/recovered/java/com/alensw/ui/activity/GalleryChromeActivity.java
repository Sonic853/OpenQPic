package com.alensw.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;
import com.alensw.PicFolder.QuickApp;
import com.alensw.svg.SvgPicture;
import com.alensw.ui.menu.GalleryMenu;
import com.alensw.ui.menu.GalleryMenuItem;
import com.alensw.ui.shape.GalleryDrawables;
import com.alensw.ui.view.TitleBar;
import com.alensw.ui.view.TitleView;
@SuppressLint("Registered")
public class GalleryChromeActivity extends Activity {
   public static final boolean F;
   public static final boolean G;
   public static final boolean H;
   private static final int NAVIGATION_HIDE_FLAGS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LOW_PROFILE;
   private static int i;
   private static int j;
   protected View I;
   protected ActionBar J;
   protected TitleBar K;
   protected ViewGroup L;
   protected boolean M;
   protected boolean N;
   protected int O;
   protected int P;
   public boolean Q;
   public boolean R;
   public long S;
   public int T = 0;
   public int U = -16777216;
   public int V = -12303292;
   public int W = -12303292;
   public int X = -7829368;
   public int Y = -1;
   public final OnClickListener Z = new DispatchActionButtonClick(this);
   private View a;
   protected final Runnable aa = new AutoHideActionBar(this);
   private boolean b;
   private ActionMode c;
   private TitleView d;
   private TitleBar e;
   private TitleBar f;
   private int g = -1;
   private ColorDrawable h = new ColorDrawable(this.g);
   private boolean keepNavigationButtonsVisible;
   private int suppressedNavigationFlags;

   static {
      boolean var1 = true;
      boolean var0;
      if (VERSION.SDK_INT >= 11) {
         var0 = true;
      } else {
         var0 = false;
      }

      F = var0;
      if (VERSION.SDK_INT >= 11) {
         var0 = true;
      } else {
         var0 = false;
      }

      G = var0;
      if (VERSION.SDK_INT >= 16) {
         var0 = var1;
      } else {
         var0 = false;
      }

      H = var0;
   }

   @TargetApi(11)
   public static int a(Context var0) {
      if (i == 0) {
         Resources var1 = var0.getResources();
         if (F) {
            TypedValue var2 = new TypedValue();
            var0.getTheme().resolveAttribute(16843499, var2, true);
            i = (int)var2.getDimension(var1.getDisplayMetrics());
         } else {
            i = var1.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_height);
         }
      }

      return i;
   }

   static ActionMode a(GalleryChromeActivity var0) {
      return var0.c;
   }

   static ActionMode a(GalleryChromeActivity var0, ActionMode var1) {
      var0.c = var1;
      return var1;
   }

   public static void a(Activity var0, int var1) {
      int up = var0.getResources().getIdentifier("up", "id", "android");
      View button = up != 0 ? var0.findViewById(up) : null;
      ImageView var3 = button instanceof ImageView ? (ImageView)button : null;
      if (QuickApp.e && button != null) {
         for (ViewParent parent = button.getParent(); parent instanceof View; parent = parent.getParent()) {
            if (parent instanceof LinearLayout && ((View)parent).getId() == View.NO_ID) {
               ((View)parent).setOnClickListener(new DispatchHomeMenuItem(var0));
               break;
            }
         }
      }

      if (var3 != null) {
         LayoutParams var8 = var3.getLayoutParams();
         var8.width = var0.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.home_width);
         var8.height = -1;
         if (var8 instanceof MarginLayoutParams) {
            MarginLayoutParams var5 = (MarginLayoutParams)var8;
            var5.rightMargin = 0;
            var5.leftMargin = 0;
         }

         var3.setLayoutParams(var8);
         var3.setPadding(0, 0, 0, 0);
         var3.setScaleType(ScaleType.CENTER);
      }
   }

   public static void a(View var0, int var1) {
      if (var0 instanceof ImageView) {
         Resources var3 = var0.getResources();
         int var2 = var3.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.overflow_size);
         ImageView var4 = (ImageView)var0;
         var4.setImageDrawable(SvgPicture.a(var3, com.alensw.PicFolder.R.raw.ic_menu_overflow, var1, var2));
         var4.setScaleType(ScaleType.CENTER);
      }
   }

   public static void a(ImageView var0, Shape var1) {
      a(var0, var1, -1426063361);
   }

   public static void a(ImageView var0, Shape var1, int var2) {
      LayoutParams var4 = var0.getLayoutParams();
      int var3 = Math.min(
         Math.max(var0.getWidth(), var4.width) - var0.getPaddingLeft() - var0.getPaddingRight(),
         Math.max(var0.getHeight(), var4.height) - var0.getPaddingTop() - var0.getPaddingBottom()
      );
      var0.setImageDrawable(GalleryDrawables.a(var1, var3, var3, var2));
   }

   public static int b(Context var0) {
      if (j == 0) {
         j = var0.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_material_icon_size);
      }

      return Math.max(a(var0) * 2 / 3, j);
   }

   public static int b(Context var0, int var1) {
      TypedValue var2 = new TypedValue();
      var0.getTheme().resolveAttribute(var1, var2, true);
      return var0.getResources().getColor(var2.resourceId);
   }

   public static View b(ViewGroup var0) {
      int var1 = var0.getChildCount() - 1;

      View var2;
      while (true) {
         if (var1 < 0) {
            var2 = null;
            break;
         }

         View var3 = var0.getChildAt(var1);
         if (var3 instanceof ViewGroup) {
            var2 = b((ViewGroup)var3);
            if (var2 != null) {
               break;
            }
         }

         if (var3.getVisibility() != 8) {
            var2 = var3;
            if (var3.getClass().getName().contains("OverflowMenuButton")) {
               break;
            }
         }

         var1--;
      }

      return var2;
   }

   public static int c(Context var0) {
      int var5;
      if (VERSION.SDK_INT >= 11) {
         int[] var2 = new int[]{16843470};
         TypedArray var3 = var0.obtainStyledAttributes(var2);
         var5 = var3.getResourceId(0, 0);
         var3.recycle();
         var2[0] = var0.getResources().getIdentifier("titleTextStyle", "attr", "android");
         TypedArray var4 = var0.obtainStyledAttributes(var5, var2);
         var5 = var4.getResourceId(0, 0);
         var4.recycle();
      } else {
         var5 = 16973892;
      }

      return var5;
   }

   public static int d(Context var0) {
      if (var0 instanceof GalleryChromeActivity) {
         int var2 = ((GalleryChromeActivity)var0).V;
         int var3 = Color.red(var2);
         int var5 = Color.green(var2);
         int var4 = Color.blue(var2);
         if (var3 != var5) {
            return var2;
         }

         if (var5 != var4) {
            return var2;
         }
      }

      return var0.getResources().getColor(com.alensw.PicFolder.R.color.material_accent_light);
   }

   public static boolean e(Context var0) {
      Resources var3 = var0.getResources();
      int var1 = var3.getIdentifier("split_action_bar_is_narrow", "bool", "android");
      boolean var2;
      if (var1 != 0 && var3.getBoolean(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static int h(int var0) {
      switch (var0) {
         case -3:
            var0 = com.alensw.PicFolder.R.style.Theme_DarkColor;
            break;
         case -2:
            var0 = com.alensw.PicFolder.R.style.Theme_LightColor;
            break;
         case -1:
            var0 = com.alensw.PicFolder.R.style.Theme_Player;
            break;
         case 0:
         default:
            var0 = com.alensw.PicFolder.R.style.Theme_Translucid;
            break;
         case 1:
            var0 = com.alensw.PicFolder.R.style.Theme_Blue;
            break;
         case 2:
            var0 = com.alensw.PicFolder.R.style.Theme_Dark;
            break;
         case 3:
            var0 = com.alensw.PicFolder.R.style.Theme_Black;
      }

      return var0;
   }

   public void a(Rect var1, Rect var2) {
      boolean var4 = this.m();
      var2.set(var1);
      if (VERSION.SDK_INT < 19) {
         int var3 = this.j();
         if (var3 > 0 && var2.top < var3) {
            var2.top += var3;
         }

         var3 = this.k();
         if (var4 && var3 > 0 && var2.bottom < var3) {
            var2.bottom += var3;
         }
      }

      if (QuickApp.j == 3 && !var4) {
         var2.bottom = 0;
      }

      if (this.a != null) {
         this.a.getLayoutParams().height = var2.bottom;
      }
   }

   public void a(Menu var1, int var2, int var3) {
      this.a(var1.findItem(var2), var3);
   }

   public void a(Menu var1, int var2, boolean var3) {
      this.a(var1.findItem(var2), var3);
   }

   @TargetApi(11)
   public void a(MenuItem var1, int var2) {
      if (var1 != null) {
         if (F) {
            var1.setShowAsAction(var2);
         } else if (this.K != null && var1 instanceof GalleryMenuItem) {
            this.K.a(var1, var2);
         }
      }
   }

   public void a(MenuItem var1, boolean var2) {
      if (var1 != null) {
         var1.setVisible(var2);
      }
   }

   public void a(ViewGroup var1) {
      this.L = var1;
   }

   public void a(TitleBar var1) {
      ViewGroup var3 = (ViewGroup)this.I.getParent();
      if (this.K != null) {
         var3.removeView(this.K);
      }

      this.K = var1;
      if (this.K != null) {
         var1 = this.K;
         byte var2;
         if (this.M) {
            var2 = -1;
         } else {
            var2 = 0;
         }

         var3.addView(var1, var2);
         this.K.setBackgroundColor(this.V);
         this.K.setTitle(this.getTitle());
         if (this.N) {
            this.K.a();
         }
      }
   }

   public void a(CharSequence var1) {
      if (this.J != null) {
         this.J.setSubtitle(var1);
      } else if (this.K != null && this.K != this.e) {
         this.K.setSubtitle(var1);
      }
   }

   @TargetApi(11)
   public void a(CharSequence var1, boolean var2) {
      if (this.d != null) {
         this.d.setText(var1);
      }

      if (this.c != null) {
         this.c.setTitle(var1);
         if (var2) {
            this.c.invalidate();
         }
      } else if (this.e != null) {
         this.e.setTitle(var1);
         if (var2) {
            this.e.requestLayout();
         }
      }
   }

   @TargetApi(11)
   public void a(boolean z) {
        u();
        f(z);
        if (this.J != null && this.J.isShowing() != z && !H) {
            if (z) {
                this.J.show();
            } else {
                this.J.hide();
            }
        }
        int i2 = z ? 0 : 4;
        if (this.K != null && this.K.getVisibility() != i2) {
            if (!G) {
                this.K.setVisibility(8);
            }
            this.K.setVisibility(i2);
            this.K.startAnimation(c(z));
        }
        if (this.L != null && this.L.getVisibility() != i2 && this.L.getVisibility() != 8) {
            if (!G) {
                this.L.setVisibility(8);
            }
            this.L.setVisibility(i2);
            this.L.startAnimation(c(z));
        }
        int i3 = (!z && this.R) ? -16777216 : this.U;
        if (this.g != i3) {
            new ActionBarColorAnimation(this, this.I, null, this.g, i3, i3).a(300L, true);
        }
    }

   @TargetApi(11)
   public boolean a() {
      boolean var1 = true;
      if ((this.J == null || !this.J.isShowing()) && (this.K == null || this.K.getVisibility() != 0)) {
         var1 = false;
      }

      return var1;
   }

   @TargetApi(11)
   public boolean a(float var1, float var2) {
      boolean var4 = true;
      if (this.J != null && this.J.isShowing()) {
         if (var2 < this.J.getHeight()) {
            return var4;
         }

         if (var2 >= this.I.getHeight() - this.k()) {
            return var4;
         }
      } else {
         if (this.K != null && this.K.getVisibility() == 0 && var2 >= this.K.getTop() && var2 < this.K.getBottom()) {
            return var4;
         }

         if (this.L != null && this.L.getVisibility() == 0 && var2 >= this.L.getTop() && var2 < this.L.getBottom()) {
            return var4;
         }
      }

      return false;
   }

   public View c(int var1) {
      if (var1 != 0) {
         if (this.K != null) {
            View var2 = this.K.a(var1);
            if (var2 != null && var2.getVisibility() == 0) {
               return var2;
            }
         }

         View var3 = this.findViewById(var1);
         if (var3 != null && var3.getVisibility() == 0) {
            return var3;
         }
      }

      if (F) {
         View var4 = this.p();
         if (var4 != null) {
            return var4;
         }
      } else if (this.K != null) {
         View var5 = this.K.a(com.alensw.PicFolder.R.id.menu);
         if (var5 != null) {
            return var5;
         }
      }

      View var6;
      if (this.a != null) {
         var6 = this.a;
      } else {
         var6 = this.I;
      }

      return var6;
   }

   public Animation c(boolean var1) {
      float var3 = 1.0F;
      float var2;
      if (var1) {
         var2 = 0.0F;
      } else {
         var2 = 1.0F;
      }

      if (!var1) {
         var3 = 0.0F;
      }

      AlphaAnimation var4 = new AlphaAnimation(var2, var3);
      var4.setDuration(300L);
      return var4;
   }

   protected boolean c(Menu var1) {
      return false;
   }

   protected void d(int var1) {
      if (this.P != var1) {
         int var2 = this.P;
         this.P = var1;
          if (!this.keepNavigationButtonsVisible && this.Q && (var2 & 2) != 0 && (var1 & 2) == 0
                && this.J != null && !this.J.isShowing()) {
            this.a(true);
            this.S = System.currentTimeMillis();
         }
      }
   }

   @TargetApi(11)
   public void d(boolean var1) {
      TitleBar var3 = null;
      if (F) {
         if (this.c != null) {
            this.c.finish();
            this.c = null;
         }

         if (var1) {
            this.c = this.startActionMode(new GalleryActionModeCallback(this));
            this.d = (TitleView)this.getLayoutInflater().inflate(com.alensw.PicFolder.R.layout.action_text, null, false);
            this.d.a(true);
            this.d.setId(16908319);
            this.d.setMinHeight(this.j());
            this.d.setTextAppearance(this, c(this));
            this.d.setOnClickListener(this.Z);
            FrameLayout var2 = new FrameLayout(this);
            var2.addView(this.d, new android.widget.FrameLayout.LayoutParams(-2, -1));
            this.c.setCustomView(var2);
         }
      } else {
         TitleBar var4;
         if (var1 && this.e == null) {
            this.e = (TitleBar)this.getLayoutInflater().inflate(com.alensw.PicFolder.R.layout.title_bar, (ViewGroup)this.I.getParent(), false);
            this.e.setMenuCallback(new ActivityTitleBarMenu(this));
            this.e.b(true);
            this.e.setOnHomeClickListener(new ExitSelectionHomeClick(this));
            this.e.c(true);
            this.e.setOnTitleClickListener(new DispatchTitleClick(this));
            var3 = this.K;
            var4 = this.e;
            this.f = this.K;
         } else if (this.e != null) {
            var3 = this.e;
            var4 = this.f;
            this.e = null;
            this.f = null;
            this.r();
         } else {
            var4 = null;
         }

         if (var3 != null) {
            var3.setVisibility(8);
            var3.startAnimation(this.c(false));
         }

         if (var4 != null) {
            this.a(var4);
            var4.d();
            var4.setVisibility(0);
            var4.startAnimation(this.c(true));
         }
      }
   }

   protected boolean d(Menu var1) {
      return false;
   }

   public void setKeepNavigationButtonsVisible(boolean keepVisible) {
      if (this.keepNavigationButtonsVisible != keepVisible) {
         int requestedFlags = this.t() | this.suppressedNavigationFlags;
         this.keepNavigationButtonsVisible = keepVisible;
         this.e(requestedFlags);
      }
   }

   public void e(int var1) {
      if (VERSION.SDK_INT >= 11) {
         // Keep the viewer's requested state so the auto-hide timer still applies after details closes.
         this.suppressedNavigationFlags = this.keepNavigationButtonsVisible ? var1 & NAVIGATION_HIDE_FLAGS : 0;
         this.getWindow().getDecorView().setSystemUiVisibility(var1 & ~this.suppressedNavigationFlags);
      }
   }

   @TargetApi(19)
   public boolean e(boolean var1) {
      if (H) {
         View var3 = this.getWindow().getDecorView();
         short var2 = 1280;
         this.Q = var1;
         if (var1) {
            var2 = 1792;
            if (VERSION.SDK_INT >= 19) {
               var2 = 3840;
            }
         }

         this.e(var2);
         var3.setOnSystemUiVisibilityChangeListener(new SystemUiVisibilityListener(this));
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void f(int var1) {
      this.I.removeCallbacks(this.aa);
      this.I.postDelayed(this.aa, var1);
   }

   @TargetApi(16)
   public void f(boolean var1) {
      if (VERSION.SDK_INT >= 11) {
         int var5 = this.t() | this.suppressedNavigationFlags;
         int var4;
         if (var1) {
            var4 = var5 & -2;
         } else {
            var4 = var5 | 1;
         }

         int var3 = var4;
         if (H) {
            int var2 = var4;
            if ((var5 & 1024) != 0) {
               if (var1) {
                  var2 = var4 & -5;
               } else {
                  var2 = var4 | 4;
               }
            }

            var3 = var2;
            if ((var5 & 512) != 0) {
               if (var1) {
                  var3 = var2 & -3;
               } else {
                  var3 = var2 | 2;
               }
            }
         }

         if (var3 != var5) {
            this.e(var3);
         }
      }
   }

   public void g(int var1) {
      if (this.g != var1) {
         this.g = var1;
         if (VERSION.SDK_INT >= 11) {
            this.h.setColor(var1);
         } else {
            this.h = new ColorDrawable(var1);
         }

         this.getWindow().setBackgroundDrawable(this.h);
      }
   }

   @TargetApi(11)
   public void g(boolean var1) {
      if (this.J != null) {
         this.J.setDisplayHomeAsUpEnabled(var1);
      } else if (this.K != null) {
         this.K.a(var1);
      }
   }

   @TargetApi(11)
   protected void h() {
      if (F) {
         try {
            ProgressBar var1 = (ProgressBar)this.findViewById(this.getResources().getIdentifier("progress_circular", "id", "android"));
            var1.setScaleX(0.5F);
            var1.setScaleY(0.5F);
            var1.setVisibility(8);
         } catch (Throwable var2) {
         }
      }
   }

   @TargetApi(14)
   public void h(boolean var1) {
      if (VERSION.SDK_INT >= 14 && this.J != null) {
         this.J.setHomeButtonEnabled(var1);
      } else if (this.K != null) {
         this.K.setHomeButtonEnabled(var1);
      }
   }

   protected void i() {
      View var1 = this.o();
      if (var1 instanceof ViewGroup) {
         View var2 = b((ViewGroup)var1);
         if (var2 instanceof ImageView) {
            a(var2, this.X);
         }

         var1.getViewTreeObserver().addOnGlobalLayoutListener(new ActionBarLayoutListener(this, var1));
      }
   }

   public void i(boolean var1) {
      try {
         if (F) {
            this.setProgressBarIndeterminateVisibility(var1);
         } else if (this.K != null) {
            this.K.setProgressVisible(var1);
         }
      } catch (Throwable var3) {
      }
   }

   @TargetApi(11)
   @Override
   public void invalidateOptionsMenu() {
      if (F) {
         super.invalidateOptionsMenu();
      } else {
         this.b = true;
      }

      if (this.K != null) {
         this.K.c();
         this.K.d();
      }
   }

   @TargetApi(11)
   public int j() {
      int var1 = 0;
      if (this.J != null) {
         var1 = this.J.getHeight();
      } else if (this.K != null) {
         var1 = this.K.getHeight();
      }

      if (var1 <= 0) {
         var1 = a((Context)this);
      }

      return var1;
   }

   public int k() {
      int var1;
      if (!this.l()) {
         var1 = 0;
      } else {
         if (F) {
            var1 = this.getResources().getIdentifier("split_action_bar", "id", "android");
            if (var1 != 0) {
               View var3 = this.findViewById(var1);
               if (var3 != null) {
                  int var2 = var3.getHeight();
                  if (var2 > 0) {
                     return var2;
                  }
               }
            }
         }

         var1 = this.j();
      }

      return var1;
   }

   public boolean l() {
      boolean var2 = false;
      boolean var1;
      if (F) {
         var1 = var2;
         if (this.N) {
            var1 = var2;
            if (e(this)) {
               var1 = true;
            }
         }
      } else {
         var1 = var2;
         if (this.K != null) {
            var1 = this.K.b();
         }
      }

      return var1;
   }

   public boolean m() {
      int var1;
      int var2;
      label16: {
         View var5 = this.getWindow().getDecorView();
         var1 = var5.getWidth();
         int var3 = var5.getHeight();
         if (var1 > 0) {
            var2 = var3;
            if (var3 > 0) {
               break label16;
            }
         }

         DisplayMetrics var6 = this.getResources().getDisplayMetrics();
         var1 = var6.widthPixels;
         var2 = var6.heightPixels;
      }

      boolean var4;
      if (var1 < Math.min(var2, this.O)) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   protected void n() {
      if (F) {
         this.a = new View(this);
         this.a.setClickable(false);
         this.a.setWillNotDraw(true);
         ((ViewGroup)this.I.getParent()).addView(this.a, -1, 0);
         LayoutParams var1 = this.a.getLayoutParams();
         if (var1 instanceof android.widget.RelativeLayout.LayoutParams) {
            ((android.widget.RelativeLayout.LayoutParams)var1).addRule(12);
         } else {
            try {
               var1.getClass().getDeclaredField("gravity").setInt(var1, 80);
            } catch (Throwable var2) {
            }
         }
      }
   }

   protected View o() {
      if (this.l()) {
         int var1 = this.getResources().getIdentifier("split_action_bar", "id", "android");
         if (var1 != 0) {
            View var2 = this.findViewById(var1);
            if (var2 != null && var2.getVisibility() == 0) {
               return var2;
            }
         }
      }

      int var3 = this.getResources().getIdentifier("action_bar_container", "id", "android");
      View var4;
      if (var3 != 0) {
         var4 = this.findViewById(var3);
      } else {
         var4 = null;
      }

      return var4;
   }

   @Override
   public void onBackPressed() {
      if (this.q()) {
         this.d(false);
      } else {
         super.onBackPressed();
      }
   }

   @Override
   public void onConfigurationChanged(Configuration var1) {
      i = 0;
      j = 0;
      if (F && VERSION.SDK_INT < 21) {
         this.i();
      }

      super.onConfigurationChanged(var1);
   }

   @Override
   public void onCreate(Bundle bundle) {
        java.lang.reflect.Field declaredField;
        android.content.SharedPreferences defaultSharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        this.T = this instanceof com.alensw.PicFolder.PlayerActivity ? -1 : defaultSharedPreferences.getInt("main_theme", 0);
        setTheme(h(this.T));
        Resources resources = getResources();
        android.content.res.Resources.Theme theme = getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.alensw.PicFolder.R.attr.colorBackground, typedValue, true);
        int color = resources.getColor(typedValue.resourceId);
        this.g = color;
        this.U = color;
        if (this.T == -3 || this.T == -2) {
            this.V = defaultSharedPreferences.getInt("actionbar_color", this.V);
        } else {
            theme.resolveAttribute(com.alensw.PicFolder.R.attr.colorActionBar, typedValue, true);
            this.V = resources.getColor(typedValue.resourceId);
        }
        theme.resolveAttribute(com.alensw.PicFolder.R.attr.colorNavigateBar, typedValue, true);
        this.W = resources.getColor(typedValue.resourceId);
        theme.resolveAttribute(16842806, typedValue, true);
        this.Y = resources.getColor(typedValue.resourceId);
        theme.resolveAttribute(com.alensw.PicFolder.R.attr.colorActionIcon, typedValue, true);
        this.X = resources.getColor(typedValue.resourceId);
        this.O = (int) (resources.getDisplayMetrics().density * 480.0f);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            try {
                int identifier = resources.getIdentifier("screen_toolbar", "layout", "android");
                int identifier2 = resources.getIdentifier("screen_action_bar", "layout", "android");
                if (identifier == 0 || identifier2 == 0) {
                    throw new RuntimeException("wrong id: " + identifier + ", " + identifier2);
                }
                TypedArray windowStyle = getWindow().getWindowStyle();
                int iIntValue = ((Integer) Class.forName("com.android.internal.R$styleable").getField("Window_windowActionBarFullscreenDecorLayout").get(null)).intValue();
                int resourceId = windowStyle.getResourceId(iIntValue, 0);
                if (resourceId != identifier) {
                    throw new RuntimeException("not material: " + resourceId + ", " + identifier);
                }
                java.lang.reflect.Field declaredField2 = android.content.res.AssetManager.class.getDeclaredField("STYLE_NUM_ENTRIES");
                java.lang.reflect.Field declaredField3 = android.content.res.AssetManager.class.getDeclaredField("STYLE_RESOURCE_ID");
                declaredField2.setAccessible(true);
                declaredField3.setAccessible(true);
                int iIntValue2 = (iIntValue * ((Integer) declaredField2.get(null)).intValue()) + ((Integer) declaredField3.get(null)).intValue();
                try {
                    declaredField = windowStyle.getClass().getDeclaredField("mData");
                    declaredField.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    declaredField = windowStyle.getClass().getSuperclass().getDeclaredField("mData");
                    declaredField.setAccessible(true);
                }
                ((int[]) declaredField.get(windowStyle))[iIntValue2] = identifier2;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        super.onCreate(bundle);
        ((QuickApp) getApplication()).a((Activity) this);
        requestWindowFeature(5);
    }

   @Override
   public boolean onCreateOptionsMenu(Menu var1) {
      if (F && VERSION.SDK_INT < 21) {
         this.i();
      }

      return true;
   }

   @Override
   public void onDestroy() {
      ((QuickApp)this.getApplication()).b(this);
      super.onDestroy();
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var2;
      switch (var1.getItemId()) {
         case 16908332:
         case com.alensw.PicFolder.R.id.home:
            this.onBackPressed();
            var2 = true;
            break;
         default:
            var2 = super.onOptionsItemSelected(var1);
      }

      return var2;
   }

   @Override
   public void onPostCreate(Bundle var1) {
      super.onPostCreate(var1);
      if (this.K != null) {
         this.K.d();
      }
   }

   @Override
   public boolean onPrepareOptionsMenu(Menu var1) {
      boolean var2 = false;
      if (VERSION.SDK_INT < 21 && !(var1 instanceof GalleryMenu) && this.b) {
         this.b = false;
         var1.clear();
         if (!this.onCreateOptionsMenu(var1)) {
            return var2;
         }
      }

      if (this.K != null) {
         this.K.a(var1);
      }

      return true;
   }

   @Override
   public void onStart() {
      super.onStart();
      ((QuickApp)this.getApplication()).c(this);
   }

   @Override
   public void onStop() {
      ((QuickApp)this.getApplication()).d(this);
      super.onStop();
   }

   @Override
   public void onTitleChanged(CharSequence var1, int var2) {
      super.onTitleChanged(var1, var2);
      if (this.K != null && this.K != this.e) {
         this.K.setTitle(var1);
      }
   }

   protected View p() {
      if (F) {
         View var2 = this.o();
         View var1 = var2;
         if (var2 == null) {
            var1 = this.getWindow().getDecorView();
         }

         if (var1 instanceof ViewGroup) {
            var1 = b((ViewGroup)var1);
            if (var1 != null) {
               return var1;
            }
         }
      }

      return null;
   }

   public boolean q() {
      boolean var1;
      if (this.c == null && this.e == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected void r() {
   }

   @TargetApi(11)
   @Override
   public void recreate() {
      if (VERSION.SDK_INT >= 11) {
         super.recreate();
      } else {
         Intent var1 = new Intent(this.getIntent());
         var1.setClass(this, this.getClass());
         var1.addFlags(67108864);
         this.startActivity(var1);
      }
   }

   @TargetApi(16)
   public boolean s() {
      boolean var1;
      if ((this.t() & 1024) == 0 && (this.getWindow().getAttributes().flags & 1024) == 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   @Override
   public void setContentView(int var1) {
      this.setContentView(this.getLayoutInflater().inflate(var1, null));
   }

   @TargetApi(14)
   @Override
   public void setContentView(View var1) {
      boolean var2;
      if (VERSION.SDK_INT == 19 && !(this instanceof GalleryIntentActivity)) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         ActivityInsetsLayout var3 = new ActivityInsetsLayout(this, this);
         var3.addView(var1, -1, -1);
         this.M = true;
         this.I = var3;
      } else {
         this.I = var1;
      }

      Window var4 = this.getWindow();
      if (F) {
         if (this.M) {
            var4.requestFeature(9);
            var4.requestFeature(10);
         }

         super.setContentView(this.I);
         this.J = this.getActionBar();
         if (this.T == -3 || this.T == -2) {
            this.J.setBackgroundDrawable(new ColorDrawable(this.V));
            if (VERSION.SDK_INT >= 14) {
               this.J.setStackedBackgroundDrawable(new ColorDrawable(this.V));
               this.J.setSplitBackgroundDrawable(new ColorDrawable(this.W));
            }

            if (VERSION.SDK_INT >= 21) {
               var4.setStatusBarColor(this.V);
            }
         }

         this.n();
         this.h();
         a(this, this.X);
      } else {
         var4.requestFeature(1);
         if (this.M) {
            super.setContentView(this.I);
         } else {
            LinearLayout var5 = new LinearLayout(this);
            android.widget.LinearLayout.LayoutParams var7 = new android.widget.LinearLayout.LayoutParams(-1, -1, 1.0F);
            var5.setOrientation(1);
            var5.addView(this.I, var7);
            super.setContentView(var5);
         }

         TitleBar var6 = (TitleBar)this.getLayoutInflater().inflate(com.alensw.PicFolder.R.layout.title_bar, (ViewGroup)this.I.getParent(), false);
         var6.setOnHomeClickListener(this.Z);
         var6.setOnTitleClickListener(this.Z);
         this.a(var6);
      }

      if (var2) {
         this.e(false);
         this.getWindow().addFlags(67108864);
      }
   }

   public int t() {
      int var1;
      if (VERSION.SDK_INT >= 11) {
         var1 = this.getWindow().getDecorView().getSystemUiVisibility();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public void u() {
      this.I.removeCallbacks(this.aa);
   }

   public boolean v() {
      boolean var1;
      if (!this.a()) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.a(var1);
      return var1;
   }
}
