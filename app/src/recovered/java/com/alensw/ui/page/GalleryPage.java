package com.alensw.ui.page;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;

public class GalleryPage {
   protected GalleryPageActivity C;
   protected View D;
   protected Intent E;
   protected CharSequence F;
   protected CharSequence G;
   protected int H;
   protected boolean I;
   protected boolean J;
   protected final OnGlobalLayoutListener K = new PageBoundsListener(this);

   public boolean A() {
      return this.I;
   }

   public CharSequence B() {
      return this.F;
   }

   public CharSequence C() {
      return this.G;
   }

   public View D() {
      return this.D;
   }

   public void E() {
      if (this.C.d() > 1) {
         this.C.c(this);
      } else {
         this.C.finish();
      }
   }

   public Resources F() {
      return this.C.getResources();
   }

   public RectF G() {
      int var3 = this.D.getLeft();
      int var1 = this.D.getTop();
      int var2 = this.D.getWidth();
      int var5 = this.D.getHeight();
      if (var2 > 0 && var5 > 0) {
         return new RectF(var3, var1, var2 + var3, var5 + var1);
      } else {
         View var6 = this.D.getRootView();
         var3 = var6.getLeft();
         var1 = var6.getTop();
         var2 = var6.getWidth();
         int var4 = var6.getHeight();
         return new RectF(var3, var1, var2 + var3, var4 + var1);
      }
   }

   public long a(boolean var1, RectF var2) {
      RectF var6 = this.G();
      boolean var5 = GalleryPageActivity.G;
      Animation var7 = this.C.a(var1, var5, var2, var6);
      long var3 = var7.getDuration();
      if (var3 > 0L) {
         if (var1) {
            this.D.startAnimation(var7);
         } else {
            this.D.post(new StartPageExitAnimation(this, var7));
         }
      }

      return var3;
   }

   public void a() {
   }

   public void a(int var1, int var2, int var3, int var4) {
   }

   public void a(int var1, int var2, Intent var3) {
   }

   public void a(Bundle var1) {
   }

   public boolean a(int var1, Menu var2) {
      return true;
   }

   public boolean a(KeyEvent var1, boolean var2) {
      return false;
   }

   public boolean a(Menu var1) {
      return true;
   }

   public boolean a(MenuItem var1) {
      return false;
   }

   public boolean a(MotionEvent var1) {
      return false;
   }

   public void b() {
   }

   public void b(Intent var1) {
   }

   public void b(Bundle var1) {
   }

   public void b(View var1) {
      this.D = var1;
      this.D.getViewTreeObserver().addOnGlobalLayoutListener(this.K);
   }

   public void b(CharSequence var1) {
      this.F = var1;
      if (this.H == 2) {
         this.C.setTitle(var1);
      }
   }

   public boolean b(Menu var1) {
      return true;
   }

   public void c(CharSequence var1) {
      this.G = var1;
      if (this.H == 2) {
         this.C.a(var1);
      }
   }

   public void c(boolean var1) {
      this.J = var1;
      if (this.H == 2) {
         this.C.i(var1);
      }
   }

   protected boolean c(Menu var1) {
      return false;
   }

   protected boolean d(Menu var1) {
      return false;
   }

   public void e(int var1) {
      switch (var1) {
         case 0:
            if (this.H == 2) {
               this.e(1);
            }

            if (this.H == 1) {
               this.g();
               this.H = 0;
            }
            break;
         case 1:
            if (this.H == 0) {
               this.H = 1;
            } else if (this.H == 2) {
               this.H = 1;
               this.b();
            }
            break;
         case 2:
            if (this.H == 1) {
               this.H = 2;
               this.a();
            }
      }
   }

   public void e(Menu var1) {
   }

   public View f(int var1) {
      return this.D.findViewById(var1);
   }

   public boolean f() {
      return true;
   }

   public String g(int var1) {
      return this.C.getString(var1);
   }

   public void g() {
      if (this.D != null) {
         this.D.getViewTreeObserver().removeGlobalOnLayoutListener(this.K);
      }
   }

   public RectF h() {
      RectF var1 = this.G();
      var1.inset(var1.width() / 4.0F, var1.height() / 4.0F);
      return var1;
   }

   public void h(int var1) {
      this.b(this.C.getLayoutInflater().inflate(var1, null));
   }

   public void i() {
   }

   protected Uri[] j() {
      return null;
   }

   protected void k() {
   }
}
