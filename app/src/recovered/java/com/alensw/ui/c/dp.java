package com.alensw.ui.c;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alensw.PicFolder.QuickApp;
import com.alensw.jni.JniUtils;
import com.alensw.ui.activity.MainPreference;
import com.alensw.ui.view.PictureView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class dp extends com.alensw.ui.c.a {
   private final Rect A;
   private int B;
   private Uri L;
   private com.alensw.a.as M;
   private com.alensw.a.as N;
   private com.alensw.a.as O;
   private final com.alensw.ui.view.q P;
   private ViewerSwipe viewerSwipe;
   private int Q;
   private int R;
   private int S;
   private int T;
   private int U;
   private boolean V;
   private boolean W;
   private long X;
   private Runnable Y;
   private ArrayList Z;
   private ArrayList aa;
   private Interpolator ab;
   private final AnimationListener ac;
   private final Runnable ad;
   private PictureView f;
   private ImageView g;
   private ImageView h;
   private TextView i;
   private ImageView j;
   private ImageView k;
   private ImageView l;
   private com.alensw.a.e m;
   private char n = 'I';
   private Uri o = Uri.EMPTY;
   private com.alensw.ui.c.df p;
   private com.alensw.b.h.j q;
   private int r = -1;
   private boolean s = true;
   private boolean t;
   private boolean u;
   private boolean v;
   private boolean w;
   private boolean x;
   private float[] y;
   private final com.alensw.ui.d.g z = new com.alensw.ui.d.g(0);

   public dp() {
      this.A = new Rect();
      this.L = Uri.EMPTY;
      this.P = new com.alensw.ui.c.eb(this);
      this.R = 1000;
      this.ab = new DecelerateInterpolator();
      this.ac = new com.alensw.ui.c.dr(this);
      this.ad = new com.alensw.ui.c.dt(this);
   }

   private void H() {
      if (this.m != null) {
         int var2 = this.m.j();
         this.Z = new ArrayList(var2);

         for (int var1 = 0; var1 < var2; var1++) {
            this.Z.add(var1);
         }

         if (this.m.c >= 0 && this.m.c < var2) {
            this.Z.remove(this.m.c);
         }

         Collections.shuffle(this.Z);
      }
   }

   private void I() {
      if (this.T >= 0 && this.T < this.m.j()) {
         this.m.c = this.T;
         this.o = this.m.d(this.m.c);
         this.n = this.m.f(this.m.c);
         com.alensw.b.h.j var1 = QuickApp.u.a(this.o);
         if (var1 != null || this.M == null || !this.M.b(this.o)) {
            this.d(true);
            if (this.g.getVisibility() != 0) {
               this.g.setVisibility(0);
            }

            this.f.h();
            this.a(var1, true);
            if (var1 != null) {
               var1.m();
            }

            this.z();
         }
      }
   }

   private boolean J() {
      boolean var1;
      if (this.Y != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void K() {
      if (this.J()) {
         this.L();
      }
   }

   private void L() {
      this.r();
      QuickApp.u.b();
      if (this.Y == null) {
         this.Y = new com.alensw.ui.c.ds(this);
         this.b(false);
         this.b.postDelayed(this.ad, 60000L);
      } else if (this.Y != null) {
         this.b.removeCallbacks(this.ad);
         this.b.removeCallbacks(this.Y);
         this.Y = null;
         this.Z = null;
         this.aa = null;
         this.U = 0;
         this.f.h();
         this.g.setVisibility(4);
         this.d(false);
      }

      boolean var1 = this.J();
      if (this.f.getKeepScreenOn() != var1) {
         this.f.setKeepScreenOn(var1);
      }
   }

   static char a(com.alensw.ui.c.dp var0, char var1) {
      var0.n = var1;
      return var1;
   }

   static Uri a(com.alensw.ui.c.dp var0, Uri var1) {
      var0.o = var1;
      return var1;
   }

   static com.alensw.a.as a(com.alensw.ui.c.dp var0, com.alensw.a.as var1) {
      var0.N = var1;
      return var1;
   }

   static com.alensw.a.e a(com.alensw.ui.c.dp var0) {
      return var0.m;
   }

   private com.alensw.b.h.j a(Uri var1, char var2, boolean var3) {
      com.alensw.a.ao var5 = QuickApp.u;
      com.alensw.b.h.j var4 = var5.a(var1);
      com.alensw.b.h.j var6;
      if (var4 != null) {
         var6 = var4;
      } else if (this.M != null && this.M.b(var1)) {
         var6 = this.M.a();
      } else if (this.O != null && this.O.b(var1)) {
         var6 = this.O.a();
      } else if (var3) {
         this.r();
         this.M = var5.a(var1, var2, 1, this.b);
         var6 = this.M.a();
      } else {
         if (this.O != null) {
            this.O.cancel(false);
         }

         this.O = var5.a(var1, var2, 1, this.b);
         var6 = this.O.a();
      }

      return var6;
   }

   static com.alensw.b.h.j a(com.alensw.ui.c.dp var0, Uri var1, char var2, boolean var3) {
      return var0.a(var1, var2, var3);
   }

   static com.alensw.b.h.j a(com.alensw.ui.c.dp var0, com.alensw.b.h.j var1) {
      var0.q = var1;
      return var1;
   }

   static void a(com.alensw.ui.c.dp var0, boolean var1) {
      var0.d(var1);
   }

   private void a(boolean var1) {
      boolean var3 = false;
      byte var2;
      if (var1) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      if (this.h.getVisibility() != var2) {
         this.h.setVisibility(var2);
         if (com.alensw.ui.activity.ao.G) {
            com.alensw.ui.c.cx var4 = this.C;
            var1 = var3;
            if (var2 == 0) {
               var1 = true;
            }

            Animation var6 = var4.c(var1);
            var6.setDuration(150L);
            this.h.startAnimation(var6);
         }
      }
   }

   static Uri b(com.alensw.ui.c.dp var0) {
      return var0.o;
   }

   private Animation b(int var1, boolean var2) {
      Animation var3 = com.alensw.ui.view.e.a(var1, var2, this.f.getWidth(), this.f.getHeight(), this.f.ac);
      var3.setDuration(this.R);
      if (var1 != 1) {
         var3.setInterpolator(this.ab);
      }

      return var3;
   }

   private void b(com.alensw.b.h.j var1) {
      boolean var2 = true;
      if (var1.k()) {
         this.b(true);
      } else {
         QuickApp.u.a(var1);
         if (this.X != 0L) {
            var2 = false;
         }

         long var3 = System.currentTimeMillis();
         if (var2) {
            this.X = var3;
         }

         long var5 = Math.max(0L, (long)this.S);
         long var7 = this.X;
         if (var2) {
            var3 = 0L;
         } else {
            var3 = Math.max(50L, var5 - (var3 - var7));
         }

         this.b.removeCallbacks(this.Y);
         this.b.postDelayed(this.Y, var3);
      }
   }

   private void b(boolean var1) {
      if (this.W) {
         if (this.Z == null || this.Z.isEmpty()) {
            this.H();
         }

         if (!this.Z.isEmpty()) {
            this.T = (Integer)this.Z.remove(0);
         }
      } else if (var1) {
         this.T = this.a(this.T, true);
      } else {
         int var3;
         if (this.m.c >= 0 && this.m.c < this.m.j()) {
            var3 = this.m.c;
         } else {
            var3 = 0;
         }

         this.T = var3;
      }

      Uri var6 = this.m.d(this.T);
      char var2 = this.m.f(this.T);
      if (!var1) {
         this.o = var6;
         this.n = var2;
      }

      long var4;
      if (var1) {
         var4 = System.currentTimeMillis();
      } else {
         var4 = 0L;
      }

      this.X = var4;
      if (this.M != null) {
         this.M.cancel(false);
      }

      QuickApp.u.b();
      this.M = QuickApp.u.a(var6, var2, 2, this.b);
   }

   static boolean b(com.alensw.ui.c.dp var0, boolean var1) {
      var0.s = var1;
      return var1;
   }

   static void c(com.alensw.ui.c.dp var0) {
      var0.L();
   }

   static boolean c(com.alensw.ui.c.dp var0, boolean var1) {
      var0.v = var1;
      return var1;
   }

   static void d(com.alensw.ui.c.dp var0) {
      var0.y();
   }

   static void d(com.alensw.ui.c.dp var0, boolean var1) {
      var0.a(var1);
   }

   private void d(boolean z) {
        com.alensw.b.h.j picture;
        if (z) {
            com.alensw.b.h.j srcType0202A = this.f.a((android.graphics.Bitmap.Config) null);
            picture = (srcType0202A == null && this.f.getPictureType() == 1) ? this.f.getPicture() : srcType0202A;
        } else {
            picture = null;
        }
        Object tag = this.g.getTag();
        this.g.setImageBitmap(null);
        this.g.setTag(null);
        if (tag instanceof com.alensw.b.h.j) {
            ((com.alensw.b.h.j) tag).m();
        }
        if (picture != null) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(F(), picture.d());
            bitmapDrawable.setAntiAlias(true);
            this.g.setImageDrawable(bitmapDrawable);
            this.g.setTag(picture);
            Matrix matrix = new Matrix();
            picture.a(matrix, this.g.getWidth(), this.g.getHeight(), 1);
            this.g.setImageMatrix(matrix);
        }
    }

   static PictureView e(com.alensw.ui.c.dp var0) {
      return var0.f;
   }

   static void e(com.alensw.ui.c.dp var0, boolean var1) {
      var0.b(var1);
   }

   static void f(com.alensw.ui.c.dp var0) {
      var0.r();
   }

   static void g(com.alensw.ui.c.dp var0) {
      var0.q();
   }

   static ImageView h(com.alensw.ui.c.dp var0) {
      return var0.g;
   }

   static com.alensw.ui.c.df i(com.alensw.ui.c.dp var0) {
      return var0.p;
   }

   static boolean j(com.alensw.ui.c.dp var0) {
      return var0.s;
   }

   static com.alensw.b.h.j k(com.alensw.ui.c.dp var0) {
      return var0.q;
   }

   static boolean l(com.alensw.ui.c.dp var0) {
      return var0.t;
   }

   static boolean m(com.alensw.ui.c.dp var0) {
      return var0.v;
   }

   static com.alensw.a.as n(com.alensw.ui.c.dp var0) {
      return var0.N;
   }

   static char o(com.alensw.ui.c.dp var0) {
      return var0.n;
   }

   static boolean p(com.alensw.ui.c.dp var0) {
      return var0.x;
   }

   private void q() {
      boolean var3 = false;
      int var1 = this.m.j();
      int var2 = this.m.c;
      if (var2 >= 0 && var2 < var1) {
         if (var1 == 1) {
            this.m.h(var2);
            this.E();
         } else {
            long var4 = this.f.ad;
            Animation var6 = com.alensw.ui.view.e.a(false);
            var6.setDuration(var4);
            var6.setAnimationListener(new com.alensw.ui.c.dz(this));
            this.d(true);
            this.g.setVisibility(0);
            this.g.startAnimation(var6);
            if (var2 < var1 - 1) {
               var3 = true;
            }

            this.s = var3;
            com.alensw.ui.view.q var8 = this.P;
            byte var7;
            if (this.s) {
               var7 = 1;
            } else {
               var7 = -1;
            }

            var8.b(var7);
            this.m.h(var2);
            var6 = com.alensw.ui.view.e.a(true);
            var6.setDuration(var4);
            var6.setInterpolator(this.C.j(true));
            this.f.startAnimation(var6);
         }
      }
   }

   static boolean q(com.alensw.ui.c.dp var0) {
      return var0.V;
   }

   private void r() {
      if (this.N != null) {
         this.N.cancel(false);
         this.N = null;
      }

      if (this.M != null) {
         this.M.cancel(false);
         this.M = null;
      }

      if (this.O != null) {
         this.O.cancel(false);
         this.O = null;
      }

      this.b.removeMessages(3000);
   }

   static void r(com.alensw.ui.c.dp var0) {
      var0.I();
   }

   private void s() {
      this.t();
      if (this.f.e() || !this.v() || com.alensw.a.ao.a(QuickApp.u.b(this.o))) {
         this.u();
      }

      this.w();
   }

   private void t() {
      if (this.M != null && !this.M.b(this.o)) {
         this.M.cancel(false);
         this.M = null;
      }

      if (this.f.getPictureType() == 0) {
         com.alensw.a.ao var2 = QuickApp.u;
         com.alensw.b.h.j var1 = var2.a(this.o);
         if (var1 != null) {
            this.a(var1, true);
            var1.m();
         } else if (this.M == null) {
            this.M = var2.a(this.o, this.n, 1, this.b);
         }
      }
   }

   private void u() {
      int var1 = this.f.getPictureType();
      if (this.m != null && (var1 == -1 || var1 >= 1)) {
         var1 = this.a(this.m.c, this.s);
         if (var1 != this.m.c) {
            Uri var3 = this.m.d(var1);
            if (this.O != null && !this.O.b(var3)) {
               this.O.cancel(false);
               this.O = null;
            }

            com.alensw.a.ao var2 = QuickApp.u;
            if (this.O == null && !var2.c(var3)) {
               this.O = var2.a(var3, this.m.f(var1), 1, this.b);
            }
         }
      }
   }

   private boolean v() {
      return this.f.getPictureType() == 1 && (!this.u || this.f.hasAnimationPreview());
   }

   private void w() {
      if (this.N != null && !this.N.b(this.o)) {
         this.N.cancel(false);
         this.N = null;
      }

      if (this.N == null && this.v()) {
         this.N = QuickApp.u.a(this.o, this.n, 3, this.b);
      }
   }

   private void x() {
      if (this.w) {
         String var1;
         if (this.p != null) {
            var1 = this.p.a('\n');
         } else {
            var1 = "";
         }

         this.i.setText(var1);
      } else {
         this.i.setText("");
      }
   }

   private void y() {
      if (this.m != null) {
         String var2 = this.o.getPath();
         char var1 = com.alensw.b.f.c.a(var2);
         if (var1 == 0) {
            var1 = 'I';
         }

         this.n = var1;
         if (var2 != null && !this.m.b(this.m.c).equals(var2)) {
            this.m.c = this.m.a(var2, this.n);
         }

         if (this.m.c < 0) {
            this.m.c = 0;
         } else if (this.m.c >= this.m.j()) {
            this.m.c = this.m.j() - 1;
         }
      }
   }

   private void z() {
      int var2 = this.Q;
      int var1 = var2;
      if (var2 == 0) {
         if (this.aa == null) {
            this.aa = new ArrayList(5);

            for (int var5 = 1; var5 < 6; var5++) {
               this.aa.add(var5);
            }

            Collections.shuffle(this.aa);
         }

         var1 = this.U++;
         var1 %= 5;
         if (var1 == 0) {
            Collections.shuffle(this.aa);
         }

         var1 = (Integer)this.aa.get(var1);
      }

      Animation var4 = this.b(var1, true);
      var4.setAnimationListener(this.ac);
      Animation var3 = this.b(var1, false);
      var3.setAnimationListener(null);
      this.f.startAnimation(var4);
      this.g.startAnimation(var3);
   }

   protected int a(int var1, boolean var2) {
      int var4 = this.m.j();
      if (var2) {
         int var3 = var1 + 1;
         var1 = var3;
         if (var3 >= var4) {
            var1 = 0;
         }
      } else {
         int var6 = var1 - 1;
         var1 = var6;
         if (var6 < 0) {
            var1 = var4 - 1;
         }
      }

      return var1;
   }

   @Override
   public long a(boolean var1, RectF var2) {
      Animation var6 = this.C.a(var1, false, var2, null);
      long var3 = var6.getDuration();
      if (var3 > 0L) {
         if (!var1) {
            var6.setAnimationListener(new com.alensw.ui.c.dv(this));
            Animation var5 = this.C.c(false);
            var5.setDuration(var3 / 2L);
            var5.setFillAfter(true);
            if (this.c != null && this.c.getVisibility() == 0) {
               this.c.startAnimation(var5);
            }

            if (this.h != null && this.h.getVisibility() == 0) {
               this.h.startAnimation(var5);
            }
         }

         this.f.startViewerTransition(var6, var1);
      }

      return var3;
   }

   @TargetApi(16)
   @Override
   public void a() {
        boolean z = false;
        super.a();
        this.C.g(true);
        this.C.h(true);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.C);
        this.f.b(0, defaultSharedPreferences.getBoolean("auto_black_bkgnd", false) ? -16777216 : this.C.U);
        this.f.setPattern(defaultSharedPreferences.getBoolean("bkgnd_pattern", false));
        int i = defaultSharedPreferences.getInt("show_controls", 2);
        this.w = (i & 1) != 0;
        this.i.setVisibility(this.w ? 0 : 8);
        x();
        boolean z2 = (i & 2) != 0;
        this.k.setVisibility(z2 ? 0 : 8);
        this.l.setVisibility(z2 ? 0 : 8);
        this.r = com.alensw.b.l.b.a(defaultSharedPreferences.getString("screen_orientation", "-1"), -1);
        this.t = defaultSharedPreferences.getBoolean("single_tap_zoom", false);
        com.alensw.b.h.b.a = defaultSharedPreferences.getBoolean("force_true_color", false);
        QuickApp.u.a = defaultSharedPreferences.getBoolean("cache_to_media_store", true);
        this.u = defaultSharedPreferences.getBoolean("delay_load", false);
        this.Q = com.alensw.b.l.b.a(defaultSharedPreferences.getString("slideshow_animation", ""), 0);
        this.V = defaultSharedPreferences.getBoolean("pan_and_zoom", false);
        this.W = defaultSharedPreferences.getBoolean("slideshow_shuffle", false);
        this.S = com.alensw.b.l.b.a(defaultSharedPreferences.getString("slideshow_interval", "3"), 3) * 1000;
        if (this.S < 1000) {
            this.S = 1000;
        }
        if (this.m != null) {
            boolean booleanExtra = this.E.getBooleanExtra("QuickPic.slide_show", false);
            this.E.removeExtra("QuickPic.slide_show");
            if ((this.m.j() == 0 || this.m.m()) || a(0)) {
                QuickApp.o.a(this.m, true);
                QuickApp.w.c();
                a(this.m, new com.alensw.ui.c.du(this, booleanExtra));
            } else if (booleanExtra) {
                L();
            } else {
                y();
            }
            if (booleanExtra) {
                return;
            }
        }
        boolean z3 = this.f.getPictureType() >= 1 && this.o.equals(this.f.getPictureUri());
        if (z3 && l()) {
            r();
            QuickApp.u.b();
            QuickApp.u.b((com.alensw.b.h.j) null);
        } else {
            z = z3;
        }
        if (z) {
            this.f.f();
            this.b.sendEmptyMessage(3000);
            return;
        }
        com.alensw.b.h.j srcType0202A = a(this.o, this.n, true);
        a(srcType0202A, true);
        if (srcType0202A != null) {
            srcType0202A.m();
        }
        this.b.sendEmptyMessageDelayed(3000, com.alensw.ui.activity.ao.G ? 20L : this.C.b(true) + 50);
    }

   @TargetApi(16)
   @Override
   public void a(int var1, int var2, int var3, int var4) {
      super.a(var1, var2, var3, var4);
      this.C.a(this.A);
      this.c.setPadding(this.A.left, 0, this.A.right, this.A.bottom);
      if (this.viewerSwipe != null) {
         this.viewerSwipe.layout(var3 - var1, var4 - var2, this.A);
      }
      if (var3 > var1 && var4 > var2 && this.y != null) {
         this.f.setTransValues(this.y);
         this.y = null;
      }
   }

   @Override
   public void a(int var1, int var2, Intent var3) {
      if (var1 == 2 && var2 == -1 && var3 != null) {
         Uri var4 = var3.getData();
         if (var4 != null) {
            this.o = var4;
            this.n = 'I';
            this.y();
         }
      }
   }

   @Override
   public void a(Bundle var1) {
      super.a(var1);
      super.h(com.alensw.PicFolder.R.layout.viewer);
      this.o = this.E.getData();
      if (var1 != null) {
         if (this.o == null) {
            this.o = (Uri)var1.getParcelable("current_uri");
         }

         this.y = var1.getFloatArray("matrix_values");
      }

      String var4 = this.E.getType();
      byte var2;
      if (var4 != null && var4.startsWith("video/")) {
         var2 = 86;
      } else {
         var2 = 73;
      }

      this.n = (char)var2;
      this.m = QuickApp.o.a(this.C, this.o);
      if (this.m == null && this.o == null) {
         Toast.makeText(this.C, com.alensw.PicFolder.R.string.load_failed, 1).show();
         this.E();
      } else {
         if (this.m != null) {
            this.d(this.m.getPath());
            if (this.o == null) {
               this.o = this.m.d(this.m.c);
            }

            if (this.o != Uri.EMPTY) {
               this.y();
            }
         }

         this.c = (ViewGroup)this.f(com.alensw.PicFolder.R.id.bottom_bar);
         this.f = (PictureView)this.f(com.alensw.PicFolder.R.id.image);
         this.f.setListener(this.P);
         this.viewerSwipe = new ViewerSwipe(this, this.f, this::currentSwipeDetails);
         this.f.setOnFocusChangeListener(new com.alensw.ui.c.dq(this));
         this.g = (ImageView)this.f(com.alensw.PicFolder.R.id.showing);
         this.h = (ImageView)this.f(com.alensw.PicFolder.R.id.play);
         this.h.setOnClickListener(this.d);
         com.alensw.ui.activity.ao.a(this.h, this.z, -855638017);
         com.alensw.ui.d.d.a(this.h);
         float var3 = this.F().getDisplayMetrics().density;
         this.i = (TextView)this.c.findViewById(com.alensw.PicFolder.R.id.details);
         this.i.setTextColor(-1);
         this.i.setOnClickListener(this.d);
         this.i.setShadowLayer(var3 / 2.0F, var3 / 4.0F, var3 / 4.0F, -16777216);
         this.j = (ImageView)this.c.findViewById(com.alensw.PicFolder.R.id.show3d);
         this.j.setOnClickListener(this.d);
         com.alensw.ui.activity.ao.a(this.j, new com.alensw.ui.d.f("3D"));
         this.k = (ImageView)this.c.findViewById(com.alensw.PicFolder.R.id.zoomin);
         this.l = (ImageView)this.c.findViewById(com.alensw.PicFolder.R.id.zoomout);
         this.k.setOnClickListener(this.d);
         this.l.setOnClickListener(this.d);
         com.alensw.ui.activity.ao.a(this.k, new com.alensw.ui.d.h(true));
         com.alensw.ui.activity.ao.a(this.l, new com.alensw.ui.d.h(false));
      }
   }

   protected void a(com.alensw.b.h.j var1) {
      if (var1.a(this.o)) {
         this.a(var1, false);
      } else if (this.m != null) {
         Uri var2;
         if (this.q != null) {
            var2 = this.q.i;
         } else {
            var2 = this.m.d(this.a(this.m.c, this.s));
         }

         if (var1.a(var2)) {
            if (this.q != null && this.q.d < var1.d) {
               this.q.m();
               this.q = null;
            }

            if (this.q == null) {
               this.q = var1.c();
               this.f.invalidate();
            }
         }
      }

      if (!this.b.hasMessages(3000)) {
         this.b.sendEmptyMessageDelayed(3000, 20L);
      }
   }

   protected void a(com.alensw.b.h.j var1, boolean var2) {
      byte var4 = 0;
      if (var1 != null && var1.k() && this.m == null && var1.a(this.o)) {
         String var16;
         label71: {
            String var17 = this.E.getType();
            if (var17 != null) {
               var16 = var17;
               if (var17.length() != 0) {
                  break label71;
               }
            }

            var16 = "image/*";
         }

         com.alensw.ui.activity.a.b(this.C, this.o, var16);
         var1.m();
         this.E();
      } else {
         this.f.a(var1, var2);
         this.f.f();
         if (!this.L.equals(this.o)) {
            this.E.setData(this.o);
            String var8;
            if (this.m()) {
               var8 = this.o.getLastPathSegment();
            } else {
               var8 = this.o.toString();
            }

            this.b((CharSequence) com.alensw.b.l.b.c(var8));
            if (this.r == -2) {
               int var3 = this.f.getPictureWidth();
               int var5 = this.f.getPictureHeight();
               if (var3 > 0 && var5 > 0) {
                  com.alensw.ui.c.cx var6 = this.C;
                  byte var10;
                  if (var3 > var5) {
                     var10 = 0;
                  } else {
                     var10 = 1;
                  }

                  var6.setRequestedOrientation(var10);
               }
            }

            String var13;
            if (this.n == 'I') {
               var13 = this.f.getMimeType();
            } else {
               var13 = null;
            }

            com.alensw.b.h.k var7 = QuickApp.u.b(this.o);
            this.p = new com.alensw.ui.c.df(this.C, this.o, this.n, var13, var7);
            if (this.n != 'V' && (!QuickApp.c || !this.p.b())) {
               var2 = false;
            } else {
               var2 = true;
            }

            this.x = var2;
            if (this.x) {
               com.alensw.ui.d.g var14 = this.z;
               byte var11;
               if (this.n == 'V') {
                  var11 = 0;
               } else {
                  var11 = 4;
               }

               var14.a(var11);
            }

            this.a(this.x);
            this.x();
            ImageView var15 = this.j;
            byte var12;
            if (com.alensw.b.h.j.b(var8)) {
               var12 = var4;
            } else {
               var12 = 8;
            }

            var15.setVisibility(var12);
            this.v = true;
            if (this.viewerSwipe != null) {
               this.viewerSwipe.pictureChanged(this.o);
            }
         } else {
            this.v = var2;
         }
      }
   }

   @Override
   public boolean a(int var1, Menu var2) {
      this.C.a(true);
      return super.a(var1, var2);
   }

   @Override
   public boolean a(KeyEvent var1, boolean var2) {
      this.K();
      return false;
   }

   @TargetApi(11)
   @Override
   public boolean a(Menu var1) {
      com.alensw.b.g.b.a(this.C, com.alensw.PicFolder.R.menu.picture, var1);
      if (this.m != null) {
         this.a(var1, com.alensw.PicFolder.R.id.slideshow, 1);
         this.a(var1, com.alensw.PicFolder.R.id.share, 2);
         this.a(var1, com.alensw.PicFolder.R.id.delete, 2);
         this.a(var1, com.alensw.PicFolder.R.id.rotate, 1);
      } else {
         var1.removeItem(com.alensw.PicFolder.R.id.slideshow);
         var1.removeItem(com.alensw.PicFolder.R.id.share);
         var1.removeItem(com.alensw.PicFolder.R.id.delete);
         var1.removeItem(com.alensw.PicFolder.R.id.rotate);
         var1.removeItem(com.alensw.PicFolder.R.id.edit);
         var1.removeItem(com.alensw.PicFolder.R.id.rename);
         var1.removeItem(com.alensw.PicFolder.R.id.move_to);
         var1.removeItem(com.alensw.PicFolder.R.id.copy_to);
      }

      return true;
   }

   @Override
   public boolean a(MenuItem var1) {
      com.alensw.a.d var8 = null;
      boolean var4 = false;
      boolean var7 = false;
      boolean var6 = false;
      boolean var5 = true;
      int var3 = var1.getItemId();
      View var9 = this.C.c(var3);
      String var10 = this.n();
      switch (var3) {
         case 16908310:
         case com.alensw.PicFolder.R.id.rename:
            var4 = var5;
            if (this.m != null) {
               if (a && this.d()) {
                  return var5;
               }

               this.p();
               var4 = var5;
            }
            break;
         case 16908332:
         case com.alensw.PicFolder.R.id.home:
            if (this.m != null && this.C.d() == 1) {
               this.C.c(this);
               this.C.a(com.alensw.ui.c.bf.class, new Intent(null, this.o), null);
               var4 = var5;
               break;
            }
         default:
            var4 = super.a(var1);
            break;
         case com.alensw.PicFolder.R.id.share:
            var4 = var5;
            if (this.p != null) {
               this.p.a(var9);
               var4 = var5;
            }
            break;
         case com.alensw.PicFolder.R.id.play:
            if (this.n == 'V') {
               com.alensw.ui.activity.a.a(this.C, this.o, var10);
               var4 = var5;
            } else {
               var4 = var5;
               if (this.p != null) {
                  var4 = var5;
                  if (this.p.b()) {
                     com.alensw.ui.activity.a.a(this.C, this.o);
                     var4 = var5;
                  }
               }
            }
            break;
         case com.alensw.PicFolder.R.id.details:
            var4 = var5;
            if (this.p != null) {
               String var15;
               if (this.m != null) {
                  var15 = Integer.toString(this.m.c + 1) + "/" + this.m.j();
               } else {
                  var15 = this.g(com.alensw.PicFolder.R.string.details);
               }

               if (this.m != null) {
                  var8 = this.m.e(this.m.c);
               }

               this.p.a(var15, new com.alensw.ui.c.dw(this, var8));
               var4 = var5;
            }
            break;
         case com.alensw.PicFolder.R.id.show3d:
            com.alensw.ui.activity.a.b(this.C, this.o, "image/jpeg");
            var4 = var5;
            break;
         case com.alensw.PicFolder.R.id.zoomout:
         case com.alensw.PicFolder.R.id.zoomin:
            PictureView var14 = this.f;
            if (var3 == com.alensw.PicFolder.R.id.zoomin) {
               var4 = true;
            }

            float var2 = var14.d(var4);
            this.P.a(var2, true);
            var4 = var5;
            break;
         case com.alensw.PicFolder.R.id.delete:
            com.alensw.ui.activity.a.a(this.C, 1, var9, new com.alensw.ui.c.dx(this));
            var4 = var5;
            break;
         case com.alensw.PicFolder.R.id.move_to:
            if (a) {
               var4 = var5;
               if (this.d()) {
                  break;
               }
            }
         case com.alensw.PicFolder.R.id.copy_to:
            var4 = var5;
            if (this.m != null) {
               HashSet var13 = new HashSet(1);
               var8 = this.m.e(this.m.c);
               var4 = var5;
               if (var8 != null) {
                  var13.add(var8);
                  var4 = var7;
                  if (var3 == com.alensw.PicFolder.R.id.copy_to) {
                     var4 = true;
                  }

                  this.a(var13, var4, new com.alensw.ui.c.dy(this));
                  var4 = var5;
               }
            }
            break;
         case com.alensw.PicFolder.R.id.slideshow:
            var4 = var5;
            if (this.m != null) {
               if (!this.W) {
                  this.m.c = this.a(this.m.c, true);
               }

               this.L();
               com.alensw.ui.c.cx var12 = this.C;
               var4 = var6;
               if (!this.J()) {
                  var4 = true;
               }

               var12.a(var4);
               var4 = var5;
            }
            break;
         case com.alensw.PicFolder.R.id.settings:
            com.alensw.ui.activity.a.a(this.C, new Intent(this.C, MainPreference.class));
            var4 = var5;
            break;
         case com.alensw.PicFolder.R.id.rotate:
            com.alensw.ui.activity.a.c(this.C, var9);
            var4 = var5;
            break;
         case com.alensw.PicFolder.R.id.edit:
         case com.alensw.PicFolder.R.id.set_as:
            var4 = var5;
            if (this.p != null) {
               Bundle var11 = new Bundle();
               com.alensw.b.h.j var19 = this.f.getFullPicture();
               if (var19 != null) {
                  if (var19.d != 2 || var19.g() * var19.h() <= 3145728) {
                     QuickApp.u.b(var19);
                  }

                  var19.m();
               }

               if (var3 == com.alensw.PicFolder.R.id.edit) {
                  this.p.a(var9, var11);
                  var4 = var5;
               } else {
                  this.p.b(var9, var11);
                  var4 = var5;
               }
            }
            break;
         case com.alensw.PicFolder.R.id.show_in_map:
            var4 = var5;
            if (this.p != null) {
               this.p.b(var9);
               var4 = var5;
            }
            break;
         case com.alensw.PicFolder.R.id.rotate_left:
            this.b(-90);
            var4 = var5;
            break;
         case com.alensw.PicFolder.R.id.rotate_right:
            this.b(90);
            var4 = var5;
            break;
         case com.alensw.PicFolder.R.id.rotate_180:
            this.b(180);
            var4 = var5;
      }

      return var4;
   }

   @Override
   public boolean a(MotionEvent var1) {
      switch (var1.getAction() & 0xFF) {
         case 0:
            this.K();
            break;
         case 1:
            if (!this.C.a(var1.getX(), var1.getY())) {
               this.C.f(3000);
            } else {
               this.C.u();
            }
      }

      return false;
   }

   @Override
   public void b() {
      super.b();
      if (this.viewerSwipe != null) {
         this.viewerSwipe.reset();
      }
      this.K();
      this.r();
      if (this.I) {
         this.f.a(0, false);
      }

      this.f.g();
      if (this.q != null) {
         this.q.m();
         this.q = null;
      }

      if (!this.I) {
         this.l();
      }

      this.E.setData(this.o);
   }

   protected void b(int var1) {
      boolean var3 = true;
      if (this.n == 'I') {
         if (this.m != null) {
            String var4 = this.o.getPath();
            int var2 = com.alensw.b.l.b.a(var4, this.f.getPictureRotation() + var1, true);
            if (var2 != -1) {
               com.alensw.a.d var5 = this.m.e(this.m.c);
               if (var5 != null) {
                  QuickApp.w.c(var5);
               }

               com.alensw.a.x var6 = QuickApp.o;
               com.alensw.a.e var7 = this.m;
               if (this.m.c >= 4) {
                  var3 = false;
               }

               var6.a(var7, var3);
               QuickApp.u.a(this.o, false);
               QuickApp.s.a(var4, var2);
               QuickApp.r.a(var4);
            }
         }

         this.f.b(var1);
      }
   }

   @Override
   public void b(Bundle var1) {
      if (this.o != null && this.o != Uri.EMPTY) {
         var1.putParcelable("current_uri", this.o);
         var1.putFloatArray("matrix_values", this.f.getTransValues());
      } else {
         var1.remove("current_uri");
         var1.remove("matrix_values");
      }
   }

   @Override
   public boolean b(Menu var1) {
      boolean var2;
      if (this.n == 'I') {
         var2 = true;
      } else {
         var2 = false;
      }

      this.a(var1, com.alensw.PicFolder.R.id.edit, var2);
      this.a(var1, com.alensw.PicFolder.R.id.set_as, var2);
      this.a(var1, com.alensw.PicFolder.R.id.show_in_map, var2);
      return true;
   }

   protected String c() {
      return "Picture";
   }

   @Override
   public void e(Menu var1) {
      if (this.C.a()) {
         this.C.f(3000);
      }

      super.e(var1);
   }

   @Override
   public void g() {
        super.g();
        QuickApp.u.b();
        QuickApp.u.b((com.alensw.b.h.j) null);
    }

   private df currentSwipeDetails() {
      return this.n == 'I' && !this.J() ? this.p : null;
   }

   @Override
   public boolean f() {
      return this.viewerSwipe == null || !this.viewerSwipe.closeDetails();
   }

   @Override
   public boolean handleMessage(Message var1) {
      switch (var1.what) {
         case 30:
            if (var1.obj instanceof com.alensw.b.h.j) {
               com.alensw.b.h.j var2 = (com.alensw.b.h.j)var1.obj;
               if (this.H == 2) {
                  if (this.J()) {
                     this.b(var2);
                  } else {
                     this.a(var2);
                  }
               }

               var2.m();
            }
            break;
         case 3000:
            if (this.H == 2) {
               this.s();
            } else {
               this.b.removeMessages(3000);
            }
      }

      return true;
   }

   @Override
   protected Uri[] j() {
      return new Uri[]{this.o};
   }

   protected boolean l() {
      boolean var3 = false;
      boolean var2 = var3;
      if (this.m()) {
         String var4 = this.o.getPath();
         int var1;
         if (var4 != null) {
            var1 = JniUtils.fuGetFileTime(var4);
         } else {
            var1 = 0;
         }

         var2 = var3;
         if (this.B != var1) {
            this.B = var1;
            var2 = true;
         }
      }

      return var2;
   }

   protected boolean m() {
      return com.alensw.b.c.f.a(this.o);
   }

   protected String n() {
      String var1 = null;
      if (this.n == 'I') {
         var1 = this.f.getMimeType();
      }

      String var2 = var1;
      if (var1 == null) {
         if (this.m != null) {
            var2 = this.m.g(this.m.c);
         } else {
            var2 = this.E.getType();
         }
      }

      return var2;
   }

   protected void o() {
      boolean var1 = false;
      if (this.m != null) {
         this.r();
         String var2 = this.o.getPath();
         if (com.alensw.b.l.b.i(var2)) {
            QuickApp.s.a(var2, this.n, false);
            com.alensw.a.x var4 = QuickApp.o;
            com.alensw.a.e var3 = this.m;
            if (this.m.c < 4) {
               var1 = true;
            }

            var4.a(var3, var1);
            QuickApp.u.a(this.o, true);
            QuickApp.r.a(var2);
            this.q();
         } else {
            this.a(var2);
         }
      }
   }

   protected void p() {
      this.a(this.m, this.m.c, new com.alensw.ui.c.ea(this));
   }
}
