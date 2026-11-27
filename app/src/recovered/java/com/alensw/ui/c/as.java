package com.alensw.ui.c;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import android.widget.TextView.BufferType;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.bean.CommonFolder;
import com.alensw.bean.LocalFile;
import com.alensw.bean.UriFile;
import com.alensw.jni.JniUtils;
import com.alensw.ui.activity.MainPreference;
import com.alensw.ui.view.PictureView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class as extends com.alensw.ui.c.a implements ViewerSwipe.DetailsSource {
   private int A;
   private Uri B;
   private com.alensw.a.as L;
   private com.alensw.a.as M;
   private com.alensw.a.as N;
   private final com.alensw.ui.view.q O;
   private ViewerSwipe viewerSwipe;
   private int P;
   private int Q;
   private int R;
   private int S;
   private int T;
   private boolean U;
   private boolean V;
   private long W;
   private Runnable X;
   private ArrayList Y;
   private ArrayList Z;
   private Interpolator aa;
   private final AnimationListener ab;
   private final Runnable ac;
   private PictureView f;
   private ImageView g;
   private ImageView h;
   private TextView i;
   private ImageView j;
   private CommonFolder k;
   private CommonFile l;
   private com.alensw.ui.c.df m;
   private com.alensw.b.h.j n;
   private String o;
   private int p = -1;
   private boolean q = true;
   private boolean r;
   private boolean s;
   private boolean t;
   private boolean u;
   private boolean v;
   private boolean w;
   private float[] x;
   private final com.alensw.ui.d.g y = new com.alensw.ui.d.g(0);
   private final Rect z = new Rect();

   public as() {
      this.B = Uri.EMPTY;
      this.O = new com.alensw.ui.c.be(this);
      this.Q = 1000;
      this.aa = new DecelerateInterpolator();
      this.ab = new com.alensw.ui.c.au(this);
      this.ac = new com.alensw.ui.c.aw(this);
   }

   private void H() {
      if (this.z()) {
         this.I();
      }
   }

   private void I() {
      this.o();
      QuickApp.u.b();
      if (this.X == null) {
         this.X = new com.alensw.ui.c.av(this);
         this.b(false);
         this.b.postDelayed(this.ac, 3600000L);
      } else if (this.X != null) {
         this.b.removeCallbacks(this.ac);
         this.b.removeCallbacks(this.X);
         this.X = null;
         this.Y = null;
         this.Z = null;
         this.T = 0;
         this.f.h();
         this.g.setVisibility(4);
         this.d(false);
      }

      boolean var1 = this.z();
      if (this.f.getKeepScreenOn() != var1) {
         this.f.setKeepScreenOn(var1);
      }
   }

   public static Intent a(CommonFolder var0, boolean var1) {
      Intent var2 = new Intent();
      var2.setData(var0.b(var0.k));
      var2.putExtra("QuickPic.folder", var0);
      var2.putExtra("QuickPic.slide_show", var1);
      return var2;
   }

   static com.alensw.a.as a(com.alensw.ui.c.as var0, com.alensw.a.as var1) {
      var0.M = var1;
      return var1;
   }

   private com.alensw.b.h.j a(CommonFile var1, boolean var2) {
      Uri var5 = var1.i();
      com.alensw.a.ao var4 = QuickApp.u;
      com.alensw.b.h.j var3 = var4.a(var5);
      com.alensw.b.h.j var6;
      if (var3 != null) {
         var6 = var3;
      } else if (this.L != null && this.L.b(var5)) {
         var6 = this.L.a();
      } else if (this.N != null && this.N.b(var5)) {
         var6 = this.N.a();
      } else if (var2) {
         this.o();
         this.L = var4.a(var1, 1, this.b);
         var6 = this.L.a();
      } else {
         if (this.N != null) {
            this.N.cancel(false);
         }

         this.N = var4.a(var1, 1, this.b);
         var6 = this.N.a();
      }

      return var6;
   }

   static com.alensw.b.h.j a(com.alensw.ui.c.as var0, com.alensw.b.h.j var1) {
      var0.n = var1;
      return var1;
   }

   static com.alensw.b.h.j a(com.alensw.ui.c.as var0, CommonFile var1, boolean var2) {
      return var0.a(var1, var2);
   }

   static CommonFile a(com.alensw.ui.c.as var0, CommonFile var1) {
      var0.l = var1;
      return var1;
   }

   static PictureView a(com.alensw.ui.c.as var0) {
      return var0.f;
   }

   public static void a(Activity var0, CommonFile var1) {
      com.alensw.ui.activity.a.a(var0, var1.i(), var1.b());
   }

   static void a(com.alensw.ui.c.as var0, boolean var1) {
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

   private Animation b(int var1, boolean var2) {
      Animation var3 = com.alensw.ui.view.e.a(var1, var2, this.f.getWidth(), this.f.getHeight(), this.f.ac);
      var3.setDuration(this.Q);
      if (var1 != 1) {
         var3.setInterpolator(this.aa);
      }

      return var3;
   }

   static CommonFolder b(com.alensw.ui.c.as var0) {
      return var0.k;
   }

   private void b(com.alensw.b.h.j var1) {
      boolean var2 = true;
      if (var1.k()) {
         this.b(true);
      } else {
         QuickApp.u.a(var1);
         if (this.W != 0L) {
            var2 = false;
         }

         long var7 = System.currentTimeMillis();
         if (var2) {
            this.W = var7;
         }

         long var3 = Math.max(0L, (long)this.R);
         long var5 = this.W;
         if (var2) {
            var3 = 0L;
         } else {
            var3 = Math.max(50L, var3 - (var7 - var5));
         }

         this.b.removeCallbacks(this.X);
         this.b.postDelayed(this.X, var3);
      }
   }

   private void b(boolean var1) {
      if (this.V) {
         if (this.Y == null || this.Y.isEmpty()) {
            this.x();
         }

         if (!this.Y.isEmpty()) {
            this.S = (Integer)this.Y.remove(0);
         }
      } else if (var1) {
         this.S = this.a(this.S, true);
      } else {
         int var2;
         if (this.k.k >= 0 && this.k.k < this.k.o()) {
            var2 = this.k.k;
         } else {
            var2 = 0;
         }

         this.S = var2;
      }

      CommonFile var5 = this.k.a(this.S);
      if (!var1) {
         this.l = var5;
      }

      long var3;
      if (var1) {
         var3 = System.currentTimeMillis();
      } else {
         var3 = 0L;
      }

      this.W = var3;
      if (this.L != null) {
         this.L.cancel(false);
      }

      QuickApp.u.b();
      this.L = QuickApp.u.a(var5, 2, this.b);
   }

   static boolean b(com.alensw.ui.c.as var0, boolean var1) {
      var0.q = var1;
      return var1;
   }

   static void c(com.alensw.ui.c.as var0) {
      var0.o();
   }

   static boolean c(com.alensw.ui.c.as var0, boolean var1) {
      var0.t = var1;
      return var1;
   }

   static CommonFile d(com.alensw.ui.c.as var0) {
      return var0.l;
   }

   static void d(com.alensw.ui.c.as var0, boolean var1) {
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

   static void e(com.alensw.ui.c.as var0) {
      var0.n();
   }

   static void e(com.alensw.ui.c.as var0, boolean var1) {
      var0.b(var1);
   }

   static ImageView f(com.alensw.ui.c.as var0) {
      return var0.g;
   }

   static boolean g(com.alensw.ui.c.as var0) {
      return var0.q;
   }

   static com.alensw.b.h.j h(com.alensw.ui.c.as var0) {
      return var0.n;
   }

   static boolean i(com.alensw.ui.c.as var0) {
      return var0.r;
   }

   static boolean j(com.alensw.ui.c.as var0) {
      return var0.t;
   }

   static com.alensw.a.as k(com.alensw.ui.c.as var0) {
      return var0.M;
   }

   static boolean l(com.alensw.ui.c.as var0) {
      return var0.v;
   }

   static boolean m(com.alensw.ui.c.as var0) {
      return var0.U;
   }

   private void n() {
      boolean var3 = false;
      int var1 = this.k.o();
      int var2 = this.k.k;
      if (var2 >= 0 && var2 < var1) {
         if (var1 == 1) {
            this.k.c(var2);
            this.E();
         } else {
            long var4 = this.f.ad;
            Animation var6 = com.alensw.ui.view.e.a(false);
            var6.setDuration(var4);
            var6.setAnimationListener(new com.alensw.ui.c.bc(this));
            this.d(true);
            this.g.setVisibility(0);
            this.g.startAnimation(var6);
            if (var2 < var1 - 1) {
               var3 = true;
            }

            this.q = var3;
            com.alensw.ui.view.q var8 = this.O;
            byte var7;
            if (this.q) {
               var7 = 1;
            } else {
               var7 = -1;
            }

            var8.b(var7);
            this.k.c(var2);
            var6 = com.alensw.ui.view.e.a(true);
            var6.setDuration(var4);
            var6.setInterpolator(this.C.j(true));
            this.f.startAnimation(var6);
         }
      }
   }

   static void n(com.alensw.ui.c.as var0) {
      var0.y();
   }

   private void o() {
      if (this.M != null) {
         this.M.cancel(false);
         this.M = null;
      }

      if (this.L != null) {
         this.L.cancel(false);
         this.L = null;
      }

      if (this.N != null) {
         this.N.cancel(false);
         this.N = null;
      }

      this.b.removeMessages(3000);
   }

   private void p() {
      this.q();
      Uri var1 = this.l.i();
      if (this.f.e() || !this.s() || com.alensw.a.ao.a(QuickApp.u.b(var1))) {
         this.r();
      }

      this.t();
   }

   private void q() {
      Uri var2 = this.l.i();
      if (this.L != null && !this.L.b(var2)) {
         this.L.cancel(false);
         this.L = null;
      }

      if (this.f.getPictureType() == 0) {
         com.alensw.a.ao var1 = QuickApp.u;
         com.alensw.b.h.j var3 = var1.a(var2);
         if (var3 != null) {
            this.a(var3, true);
            var3.m();
         } else if (this.L == null) {
            this.L = var1.a(this.l, 1, this.b);
         }
      }
   }

   private void r() {
      int var1 = this.f.getPictureType();
      if (this.k != null && (var1 == -1 || var1 >= 1)) {
         var1 = this.a(this.k.k, this.q);
         if (var1 != this.k.k) {
            CommonFile var3 = this.k.a(var1);
            Uri var2;
            if (var3 != null) {
               var2 = var3.i();
            } else {
               var2 = Uri.EMPTY;
            }

            if (this.N != null && !this.N.b(var2)) {
               this.N.cancel(false);
               this.N = null;
            }

            com.alensw.a.ao var4 = QuickApp.u;
            if (this.N == null && var3 != null && !var4.c(var2)) {
               this.N = var4.a(var3, 1, this.b);
            }
         }
      }
   }

   private boolean s() {
      return (!this.s && this.f.getPictureType() == 1) || this.f.hasAnimationPreview();
   }

   private void t() {
      Uri var1 = this.l.i();
      if (this.M != null && !this.M.b(var1)) {
         this.M.cancel(false);
         this.M = null;
      }

      if (this.M == null && this.s()) {
         this.M = QuickApp.u.a(this.l, 3, this.b);
      }
   }

   private void u() {
      label40:
      if (this.w) {
         String var2;
         try {
            var2 = this.l.g();
         } catch (Throwable var5) {
            break label40;
         }

         String var1 = var2;
         if (var2 != null) {
            try {
               var1 = var2.replace("{{details}}", this.o);
            } catch (Throwable var4) {
               break label40;
            }
         }

         try {
            this.i.setText(Html.fromHtml(var1), BufferType.SPANNABLE);
            return;
         } catch (Throwable var3) {
         }
      }

      if (this.u && this.f.getPictureType() > 0) {
         String var6;
         if (this.m != null) {
            var6 = this.m.a('\n');
         } else {
            var6 = "";
         }

         this.i.setText(var6);
      } else {
         this.i.setText("");
      }
   }

   private void v() {
      if (this.k != null) {
         int var1 = this.k.o();
         if (!this.k.a(this.k.k).equals(this.l)) {
            this.k.k = this.k.a(this.l);
         }

         if (this.k.k < 0) {
            this.k.k = 0;
         } else if (this.k.k >= var1) {
            this.k.k = var1 - 1;
         }
      }
   }

   private void w() {
      int var2 = this.P;
      int var1 = var2;
      if (var2 == 0) {
         if (this.Z == null) {
            this.Z = new ArrayList(5);

            for (int var5 = 1; var5 < 6; var5++) {
               this.Z.add(var5);
            }

            Collections.shuffle(this.Z);
         }

         var1 = this.T++;
         var1 %= 5;
         if (var1 == 0) {
            Collections.shuffle(this.Z);
         }

         var1 = (Integer)this.Z.get(var1);
      }

      Animation var4 = this.b(var1, true);
      var4.setAnimationListener(this.ab);
      Animation var3 = this.b(var1, false);
      var3.setAnimationListener(null);
      this.f.startAnimation(var4);
      this.g.startAnimation(var3);
   }

   private void x() {
      if (this.k != null) {
         int var2 = this.k.o();
         this.Y = new ArrayList(var2);

         for (int var1 = 0; var1 < var2; var1++) {
            this.Y.add(var1);
         }

         if (this.k.k >= 0 && this.k.k < var2) {
            this.Y.remove(this.k.k);
         }

         Collections.shuffle(this.Y);
      }
   }

   private void y() {
      if (this.S >= 0 && this.S < this.k.o()) {
         this.k.k = this.S;
         this.l = this.k.a(this.k.k);
         Uri var1 = this.l.i();
         com.alensw.b.h.j var2 = QuickApp.u.a(var1);
         if (var2 != null || this.L == null || !this.L.b(var1)) {
            this.d(true);
            if (this.g.getVisibility() != 0) {
               this.g.setVisibility(0);
            }

            this.f.h();
            this.a(var2, true);
            if (var2 != null) {
               var2.m();
            }

            this.w();
         }
      }
   }

   private boolean z() {
      boolean var1;
      if (this.X != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected int a(int var1, boolean var2) {
      int var4 = this.k.o();
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
      Animation var5 = this.C.a(var1, false, var2, null);
      long var3 = var5.getDuration();
      if (var3 > 0L) {
         if (!var1) {
            var5.setAnimationListener(new com.alensw.ui.c.ay(this));
            Animation var6 = this.C.c(false);
            var6.setDuration(var3 / 2L);
            var6.setFillAfter(true);
            if (this.c != null && this.c.getVisibility() == 0) {
               this.c.startAnimation(var6);
            }

            if (this.h != null && this.h.getVisibility() == 0) {
               this.h.startAnimation(var6);
            }
         }

         this.f.startViewerTransition(var5, var1);
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
        this.f.b(-7829368, defaultSharedPreferences.getBoolean("auto_black_bkgnd", false) ? -16777216 : this.C.U);
        this.f.setPattern(defaultSharedPreferences.getBoolean("bkgnd_pattern", false));
        this.u = (defaultSharedPreferences.getInt("show_controls", 2) & 1) != 0;
        this.i.setVisibility((this.u || this.w) ? 0 : 8);
        u();
        this.p = com.alensw.b.l.b.a(defaultSharedPreferences.getString("screen_orientation", "-1"), -1);
        this.r = defaultSharedPreferences.getBoolean("single_tap_zoom", false);
        com.alensw.b.h.b.a = defaultSharedPreferences.getBoolean("force_true_color", false);
        QuickApp.u.a = defaultSharedPreferences.getBoolean("cache_to_media_store", true);
        this.s = defaultSharedPreferences.getBoolean("delay_load", false);
        this.P = com.alensw.b.l.b.a(defaultSharedPreferences.getString("slideshow_animation", ""), 0);
        this.U = defaultSharedPreferences.getBoolean("pan_and_zoom", false);
        this.V = defaultSharedPreferences.getBoolean("slideshow_shuffle", false);
        this.R = com.alensw.b.l.b.a(defaultSharedPreferences.getString("slideshow_interval", "3"), 3) * 1000;
        if (this.R < 1000) {
            this.R = 1000;
        }
        boolean booleanExtra = this.E.getBooleanExtra("QuickPic.slide_show", false);
        this.E.removeExtra("QuickPic.slide_show");
        if (booleanExtra) {
            I();
            return;
        }
        v();
        boolean z2 = this.f.getPictureType() >= 1 && this.f.getPictureUri().equals(this.l.i());
        if (z2 && l()) {
            o();
            QuickApp.u.b();
            QuickApp.u.b((com.alensw.b.h.j) null);
        } else {
            z = z2;
        }
        if (z) {
            this.f.f();
            this.b.sendEmptyMessage(3000);
            return;
        }
        com.alensw.b.h.j srcType0202A = a(this.l, true);
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
      this.C.a(this.z);
      this.c.setPadding(this.z.left, 0, this.z.right, this.z.bottom);
      if (this.viewerSwipe != null) {
         this.viewerSwipe.layout(var3 - var1, var4 - var2, this.z);
      }
      if (var3 > var1 && var4 > var2 && this.x != null) {
         this.f.setTransValues(this.x);
         this.x = null;
      }
   }

   @Override
   public void a(int var1, int var2, Intent var3) {
      if (var1 == 2 && var2 == -1 && var3 != null) {
         Uri var10 = var3.getData();

         long var4;
         long var6;
         com.alensw.b.c.f var8;
         label25: {
            try {
               var8 = com.alensw.b.c.f.a(this.C.getContentResolver(), var10);
               var6 = var8.f();
               com.alensw.b.h.c var9 = new com.alensw.b.h.c();
               if (var9.b(var8, true)) {
                  var4 = var9.a(var6);
                  break label25;
               }
            } catch (Throwable var12) {
               return;
            }

            var4 = var6;
         }

         try {
            this.l = UriFile.a('I', var10, var3.getType(), var8.e(), var6 / 1000L, var4 / 1000L);
            this.k.k = this.k.c(this.l);
         } catch (Throwable var11) {
         }
      }
   }

   @Override
   public void a(Bundle var1) {
      super.a(var1);
      super.h(com.alensw.PicFolder.R.layout.viewer);
      Uri var4 = this.E.getData();
      String var5 = this.E.getType();
      if (var1 != null) {
         this.x = var1.getFloatArray("matrix_values");
      }

      this.k = (CommonFolder)this.E.getParcelableExtra("QuickPic.folder");
      if (this.k == null && var4 == null) {
         Toast.makeText(this.C, com.alensw.PicFolder.R.string.load_failed, 1).show();
         this.E();
      } else {
         if (this.k != null) {
            this.l = this.k.a(this.k.k);
         }

         if (this.l == null) {
            this.l = UriFile.a('I', var4, var5);
         }

         this.v();
         boolean var3;
         if (this.k != null && (this.k.c() & 1073741824) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.w = var3;
         this.c = (ViewGroup)this.f(com.alensw.PicFolder.R.id.bottom_bar);
         this.f = (PictureView)this.f(com.alensw.PicFolder.R.id.image);
         this.f.setListener(this.O);
         this.viewerSwipe = new ViewerSwipe(this, this.f, this);
         this.f.setOnFocusChangeListener(new com.alensw.ui.c.ax(this));
         this.g = (ImageView)this.f(com.alensw.PicFolder.R.id.showing);
         this.h = (ImageView)this.f(com.alensw.PicFolder.R.id.play);
         this.h.setOnClickListener(this.d);
         com.alensw.ui.activity.ao.a(this.h, this.y, -855638017);
         com.alensw.ui.d.d.a(this.h);
         Resources var6 = this.F();
         this.o = var6.getString(com.alensw.PicFolder.R.string.details);
         float var2 = var6.getDisplayMetrics().density;
         this.i = (TextView)this.c.findViewById(com.alensw.PicFolder.R.id.details);
         this.i.setTextColor(-1);
         this.i.setLinkTextColor(-1);
         this.i.setShadowLayer(var2 / 2.0F, var2 / 4.0F, var2 / 4.0F, -16777216);
         if (this.w) {
            this.i.setBackgroundColor(0);
            this.i.setTextSize(0, var6.getDimension(com.alensw.PicFolder.R.dimen.comment_text_size));
            this.i.setMovementMethod(LinkMovementMethod.getInstance());
         } else {
            this.i.setOnClickListener(this.d);
         }

         this.j = (ImageView)this.c.findViewById(com.alensw.PicFolder.R.id.show3d);
         this.j.setOnClickListener(this.d);
         com.alensw.ui.activity.ao.a(this.j, new com.alensw.ui.d.f("3D"));
         this.c.findViewById(com.alensw.PicFolder.R.id.zoomin).setVisibility(8);
         this.c.findViewById(com.alensw.PicFolder.R.id.zoomout).setVisibility(8);
      }
   }

   protected void a(com.alensw.b.h.j var1) {
      if (var1.a(this.l.i())) {
         this.a(var1, false);
      } else if (this.k != null) {
         Uri var2;
         if (this.n != null) {
            var2 = this.n.i;
         } else {
            var2 = this.k.b(this.a(this.k.k, this.q));
         }

         if (var1.a(var2)) {
            if (this.n != null && this.n.d < var1.d) {
               this.n.m();
               this.n = null;
            }

            if (this.n == null) {
               this.n = var1.c();
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
      Uri var9 = this.l.i();
      char var5 = this.l.h();
      if (var1 != null && var1.k() && this.k == null && var1.a(var9)) {
         String var19;
         label66: {
            String var8 = this.E.getType();
            if (var8 != null) {
               var19 = var8;
               if (var8.length() != 0) {
                  break label66;
               }
            }

            var19 = "image/*";
         }

         com.alensw.ui.activity.a.b(this.C, var9, var19);
         var1.m();
         this.E();
      } else {
         this.f.a(var1, var2);
         this.f.f();
         if (!this.B.equals(var9)) {
            String var10 = this.l.j();
            String var11;
            if (var10 != null) {
               var11 = com.alensw.b.l.b.c(var10);
            } else {
               var11 = this.l.a();
            }

            this.b((CharSequence) var11);
            if (this.p == -2) {
               int var6 = this.f.getPictureWidth();
               int var3 = this.f.getPictureHeight();
               if (var6 > 0 && var3 > 0) {
                  com.alensw.ui.c.cx var7 = this.C;
                  byte var13;
                  if (var6 > var3) {
                     var13 = 0;
                  } else {
                     var13 = 1;
                  }

                  var7.setRequestedOrientation(var13);
               }
            }

            com.alensw.b.h.k var16 = QuickApp.u.b(var9);
            this.m = new com.alensw.ui.c.df(this.C, this.l, var16);
            if (var5 != 'V' && (!QuickApp.c || !this.m.b())) {
               var2 = false;
            } else {
               var2 = true;
            }

            this.v = var2;
            if (this.v) {
               com.alensw.ui.d.g var17 = this.y;
               byte var14;
               if (var5 == 'V') {
                  var14 = 0;
               } else {
                  var14 = 4;
               }

               var17.a(var14);
            }

            this.a(this.v);
            this.u();
            ImageView var18 = this.j;
            byte var15;
            if (com.alensw.b.h.j.b(var11)) {
               var15 = var4;
            } else {
               var15 = 8;
            }

            var18.setVisibility(var15);
            this.t = true;
            if (this.viewerSwipe != null) {
               this.viewerSwipe.pictureChanged(var9);
            }
         } else {
            this.t = var2;
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
      this.H();
      return false;
   }

   @TargetApi(11)
   @Override
   public boolean a(Menu var1) {
      com.alensw.b.g.b.a(this.C, com.alensw.PicFolder.R.menu.picture, var1);
      var1.removeItem(com.alensw.PicFolder.R.id.rename);
      if (this.k != null) {
         this.a(var1, com.alensw.PicFolder.R.id.slideshow, 1);
         this.a(var1, com.alensw.PicFolder.R.id.share, 2);
         if ((this.k.c() & 2) != 0) {
            this.a(var1, com.alensw.PicFolder.R.id.delete, 2);
         } else {
            var1.removeItem(com.alensw.PicFolder.R.id.delete);
         }

         this.a(var1, com.alensw.PicFolder.R.id.rotate, 1);
      } else {
         var1.removeItem(com.alensw.PicFolder.R.id.slideshow);
         var1.removeItem(com.alensw.PicFolder.R.id.share);
         var1.removeItem(com.alensw.PicFolder.R.id.delete);
         var1.removeItem(com.alensw.PicFolder.R.id.rotate);
      }

      return true;
   }

   @Override
   public boolean a(MenuItem var1) {
      boolean var6 = false;
      boolean var5 = false;
      boolean var4 = true;
      int var2 = var1.getItemId();
      View var7 = this.C.c(var2);
      boolean var3;
      switch (var2) {
         case 16908332:
         case com.alensw.PicFolder.R.id.home:
            if (this.k != null && this.C.d() == 1) {
               this.C.c(this);
               this.C.a(com.alensw.ui.c.bf.class, com.alensw.ui.c.z.a(this.k, false, null), null);
               var3 = var4;
               break;
            }
         default:
            var3 = super.a(var1);
            break;
         case com.alensw.PicFolder.R.id.share:
            var3 = var4;
            if (this.m != null) {
               var3 = var4;
               if (this.f.getPictureType() > 0) {
                  this.m.a(var7);
                  var3 = var4;
               }
            }
            break;
         case com.alensw.PicFolder.R.id.play:
            if (this.l.h() == 'V') {
               a(this.C, this.l);
               var3 = var4;
            } else {
               var3 = var4;
               if (this.m != null) {
                  var3 = var4;
                  if (this.m.b()) {
                     com.alensw.ui.activity.a.a(this.C, this.l.i());
                     var3 = var4;
                  }
               }
            }
            break;
         case com.alensw.PicFolder.R.id.details:
            var3 = var4;
            if (this.m != null) {
               var3 = var4;
                if (this.f.getPictureType() > 0) {
                  CommonFile var15 = this.l;
                  this.m.a(currentDetailsTitle(), new com.alensw.ui.c.az(this, var15));
                  var3 = var4;
               }
            }
            break;
         case com.alensw.PicFolder.R.id.show3d:
            com.alensw.ui.activity.a.b(this.C, this.l.i(), this.l.b());
            var3 = var4;
            break;
         case com.alensw.PicFolder.R.id.delete:
            com.alensw.ui.activity.a.a(this.C, 1, var7, new com.alensw.ui.c.ba(this));
            var3 = var4;
            break;
         case com.alensw.PicFolder.R.id.move_to:
            if (a) {
               var3 = var4;
               if (this.d()) {
                  break;
               }
            }
         case com.alensw.PicFolder.R.id.copy_to:
            var3 = var4;
            if (this.k != null) {
               HashSet var11 = new HashSet(1);
               var11.add(this.l);
               var3 = var6;
               if (var2 == com.alensw.PicFolder.R.id.copy_to) {
                  var3 = true;
               }

               this.a(var11, var3, new com.alensw.ui.c.bb(this));
               var3 = var4;
            }
            break;
         case com.alensw.PicFolder.R.id.slideshow:
            var3 = var4;
            if (this.k != null) {
               if (!this.V) {
                  this.k.k = this.a(this.k.k, true);
               }

               this.I();
               com.alensw.ui.c.cx var10 = this.C;
               var3 = var5;
               if (!this.z()) {
                  var3 = true;
               }

               var10.a(var3);
               var3 = var4;
            }
            break;
         case com.alensw.PicFolder.R.id.settings:
            com.alensw.ui.activity.a.a(this.C, new Intent(this.C, MainPreference.class));
            var3 = var4;
            break;
         case com.alensw.PicFolder.R.id.rotate:
            com.alensw.ui.activity.a.c(this.C, var7);
            var3 = var4;
            break;
         case com.alensw.PicFolder.R.id.edit:
         case com.alensw.PicFolder.R.id.set_as:
            var3 = var4;
            if (this.m != null) {
               var3 = var4;
               if (this.f.getPictureType() > 0) {
                  Bundle var8 = new Bundle();
                  com.alensw.b.h.j var9 = this.f.getFullPicture();
                  if (var9 != null) {
                     if (var9.d != 2 || var9.g() * var9.h() <= 3145728) {
                        QuickApp.u.b(var9);
                     }

                     var9.m();
                  }

                  if (var2 == com.alensw.PicFolder.R.id.edit) {
                     this.m.a(var7, var8);
                     var3 = var4;
                  } else {
                     this.m.b(var7, var8);
                     var3 = var4;
                  }
               }
            }
            break;
         case com.alensw.PicFolder.R.id.show_in_map:
            var3 = var4;
            if (this.m != null) {
               this.m.b(var7);
               var3 = var4;
            }
            break;
         case com.alensw.PicFolder.R.id.rotate_left:
            this.b(-90);
            var3 = var4;
            break;
         case com.alensw.PicFolder.R.id.rotate_right:
            this.b(90);
            var3 = var4;
            break;
         case com.alensw.PicFolder.R.id.rotate_180:
            this.b(180);
            var3 = var4;
      }

      return var3;
   }

   @Override
   public boolean a(MotionEvent var1) {
      switch (var1.getAction() & 0xFF) {
         case 0:
            this.H();
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
      this.H();
      this.o();
      if (this.I) {
         this.f.a(0, false);
      }

      this.f.g();
      if (this.n != null) {
         this.n.m();
         this.n = null;
      }

      if (!this.I) {
         this.l();
      }

      if (this.l != null) {
         this.E.setData(this.l.i());
      }
   }

   protected void b(int var1) {
      if (this.l.h() == 'I') {
         String var3 = this.l.j();
         if (var3 != null) {
            int var2 = com.alensw.b.l.b.a(var3, this.f.getPictureRotation() + var1, true);
            if (var2 != -1) {
               QuickApp.u.a(this.l.i(), false);
               QuickApp.s.a(var3, var2);
               QuickApp.r.a(var3);
            }
         }

         this.f.b(var1);
      }
   }

   @Override
   public void b(Bundle var1) {
      if (this.f.getPictureType() > 1) {
         var1.putFloatArray("matrix_values", this.f.getTransValues());
      } else {
         var1.remove("matrix_values");
      }
   }

   @Override
   public boolean b(Menu var1) {
      boolean var2;
      if (this.l.h() == 'I') {
         var2 = true;
      } else {
         var2 = false;
      }

      this.a(var1, com.alensw.PicFolder.R.id.edit, var2);
      this.a(var1, com.alensw.PicFolder.R.id.set_as, var2);
      this.a(var1, com.alensw.PicFolder.R.id.show_in_map, var2);
      return true;
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

   @Override
   public df currentPictureDetails() {
      return this.l != null && this.l.h() == 'I' && !this.z() ? this.m : null;
   }

   @Override
   public String currentDetailsTitle() {
      return this.k == null ? this.g(com.alensw.PicFolder.R.string.details)
            : Integer.toString(this.k.k + 1) + "/" + this.k.o();
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
                  if (this.z()) {
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
               this.p();
            } else {
               this.b.removeMessages(3000);
            }
      }

      return true;
   }

   @Override
   protected Uri[] j() {
      return new Uri[]{this.l.i()};
   }

   protected boolean l() {
      boolean var2 = false;
      String var3 = this.l.j();
      int var1;
      if (var3 != null) {
         var1 = JniUtils.fuGetFileTime(var3);
      } else {
         var1 = 0;
      }

      if (this.A != var1) {
         this.A = var1;
         var2 = true;
      }

      return var2;
   }

   protected void m() {
      com.alensw.a.bc var2 = QuickApp.r;
      ContentResolver var3 = this.C.getContentResolver();
      Uri var1 = this.l.i();
      if (this.l instanceof LocalFile) {
         String var5 = this.l.j();
         if (com.alensw.b.l.b.i(var5)) {
            QuickApp.s.a(var5, this.l.h(), false);
            QuickApp.u.a(var1, true);
            var2.a(var1.toString());
            this.n();
         }
      } else {
         try {
            if (var3.delete(var1, null, null) > 0) {
               var2.a(var1.toString());
               this.n();
            }
         } catch (Throwable var4) {
         }
      }
   }
}
