package com.alensw.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

public class LockPatternView extends View {
   private final PatternCellState[][] a;
   private final float b;
   private final float c;
   private final float d;
   private final Paint e = new Paint(5);
   private final Paint f = new Paint(5);
   private PatternInputListener g;
   private final ArrayList h = new ArrayList(9);
   private final boolean[][] i = new boolean[3][3];
   private float j = -1.0F;
   private float k = -1.0F;
   private boolean l = true;
   private boolean m = false;
   private boolean n = false;
   private boolean o = false;
   private float p = 0.6F;
   private float q;
   private float r;
   private final Path s = new Path();
   private final Rect t = new Rect();
   private final Rect u = new Rect();
   private int v = 1;
   private int w;
   private int x;
   private final Interpolator y;
   private final Interpolator z;

   public LockPatternView(Context var1) {
      this(var1, null);
   }

   public LockPatternView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setClickable(true);
      Resources var7 = this.getResources();
      TypedValue var6 = new TypedValue();
      var1.getTheme().resolveAttribute(16842806, var6, true);
      int var4 = var7.getColor(var6.resourceId);
      float var3 = var7.getDisplayMetrics().density;
      this.w = var4;
      this.x = var4;
      var4 = this.w;
      this.f.setColor(var4);
      this.f.setStyle(Style.STROKE);
      this.f.setStrokeJoin(Join.ROUND);
      this.f.setStrokeCap(Cap.ROUND);
      this.d = 3.0F * var3;
      this.f.setStrokeWidth(this.d);
      this.b = var3 * 12.0F;
      this.c = 2.3333333F;
      this.a = new PatternCellState[3][3];

      for (int var9 = 0; var9 < 3; var9++) {
         for (int var5 = 0; var5 < 3; var5++) {
            this.a[var9][var5] = new PatternCellState();
         }
      }

      if (VERSION.SDK_INT >= 21) {
         this.y = AnimationUtils.loadInterpolator(var1, 17563661);
         this.z = AnimationUtils.loadInterpolator(var1, 17563662);
      } else {
         this.y = new AccelerateDecelerateInterpolator();
         this.z = new AccelerateDecelerateInterpolator();
      }
   }

   private float a(float var1, float var2, float var3, float var4) {
      var1 -= var3;
      var2 -= var4;
      return Math.min(1.0F, Math.max(0.0F, ((float)Math.sqrt(var1 * var1 + var2 * var2) / this.q - 0.3F) * 4.0F));
   }

   private float a(int var1) {
      return this.getPaddingLeft() + var1 * this.q + this.q / 2.0F;
   }

   static float a(LockPatternView var0) {
      return var0.c;
   }

   private int a(float var1) {
      float var5 = this.r;
      float var2 = var5 * this.p;
      float var3 = this.getPaddingTop();
      float var6 = (var5 - var2) / 2.0F;
      int var7 = 0;

      while (true) {
         if (var7 >= 3) {
            var7 = -1;
            break;
         }

         float var4 = var7 * var5 + (var6 + var3);
         if (var1 >= var4 && var1 <= var4 + var2) {
            break;
         }

         var7++;
      }

      return var7;
   }

   private int a(int var1, int var2) {
      int var4 = MeasureSpec.getSize(var1);
      int var3 = var2;
      switch (MeasureSpec.getMode(var1)) {
         case Integer.MIN_VALUE:
            var3 = Math.max(var4, var2);
         case 0:
            break;
         default:
            var3 = var4;
      }

      return var3;
   }

   private int a(boolean var1) {
      int var2;
      if (var1 && !this.m && !this.o) {
         var2 = this.x;
      } else {
         var2 = this.w;
      }

      return var2;
   }

   private PatternCell a(float var1, float var2) {
      PatternCell var9 = null;
      Object var11 = null;
      int var4 = 1;
      PatternCell var10 = this.b(var1, var2);
      if (var10 != null) {
         ArrayList var12 = this.h;
         var9 = (PatternCell)var11;
         if (!var12.isEmpty()) {
            var9 = (PatternCell)var12.get(var12.size() - 1);
            int var8 = var10.a - var9.a;
            int var7 = var10.b - var9.b;
            int var6 = var9.a;
            int var5 = var9.b;
            int var3 = var6;
            if (Math.abs(var8) == 2) {
               var3 = var6;
               if (Math.abs(var7) != 1) {
                  var6 = var9.a;
                  byte var13;
                  if (var8 > 0) {
                     var13 = 1;
                  } else {
                     var13 = -1;
                  }

                  var3 = var13 + var6;
               }
            }

            if (Math.abs(var7) == 2 && Math.abs(var8) != 1) {
               var5 = var9.b;
               if (var7 <= 0) {
                  var4 = -1;
               }

               var4 = var5 + var4;
            } else {
               var4 = var5;
            }

            var9 = PatternCell.a(var3, var4);
         }

         if (var9 != null && !this.i[var9.a][var9.b]) {
            this.a(var9);
         }

         this.a(var10);
         var9 = var10;
      }

      return var9;
   }

   public static String a(List var0) {
      String var6;
      if (var0 == null) {
         var6 = "";
      } else {
         int var2 = var0.size();
         StringBuilder var4 = new StringBuilder(var2);

         for (int var1 = 0; var1 < var2; var1++) {
            PatternCell var5 = (PatternCell)var0.get(var1);
            int var3 = var5.a();
            var4.append((char)(var5.b() + var3 * 3 + 97));
         }

         var6 = var4.toString();
      }

      return var6;
   }

   private void a(float var1, float var2, long var3, Interpolator var5, PatternCellState var6, Runnable var7) {
      new PatternDotScaleAnimation(this, this, var5, var6, var1, var2, var7).a(var3, true);
   }

   private void a(Canvas var1, float var2, float var3, float var4, boolean var5, float var6) {
      this.e.setColor(this.a(var5));
      this.e.setAlpha((int)(255.0F * var6));
      var1.drawCircle(var2, var3, var4 / 2.0F, this.e);
   }

   private void a(MotionEvent var1) {
      float var6 = this.d;
      int var13 = var1.getHistorySize();
      this.u.setEmpty();
      boolean var12 = false;

      for (int var11 = 0; var11 < var13 + 1; var11++) {
         float var2;
         if (var11 < var13) {
            var2 = var1.getHistoricalX(var11);
         } else {
            var2 = var1.getX();
         }

         float var3;
         if (var11 < var13) {
            var3 = var1.getHistoricalY(var11);
         } else {
            var3 = var1.getY();
         }

         PatternCell var15 = this.a(var2, var3);
         int var14 = this.h.size();
         if (var15 != null && var14 == 1) {
            this.o = true;
            this.c();
         }

         float var4 = Math.abs(var2 - this.j);
         float var5 = Math.abs(var3 - this.k);
         if (var4 > 0.0F || var5 > 0.0F) {
            var12 = true;
         }

         if (this.o && var14 > 0) {
            PatternCell var16 = (PatternCell)this.h.get(var14 - 1);
            var4 = this.a(var16.b);
            float var7 = this.b(var16.a);
            var5 = Math.min(var4, var2) - var6;
            var4 = Math.max(var4, var2) + var6;
            var2 = Math.min(var7, var3) - var6;
            var3 = Math.max(var7, var3) + var6;
            if (var15 != null) {
               float var10 = this.q * 0.5F;
               var7 = this.r * 0.5F;
               float var9 = this.a(var15.b);
               float var8 = this.b(var15.a);
               var5 = Math.min(var9 - var10, var5);
               var4 = Math.max(var10 + var9, var4);
               var2 = Math.min(var8 - var7, var2);
               var3 = Math.max(var8 + var7, var3);
            }

            this.u.union(Math.round(var5), Math.round(var2), Math.round(var4), Math.round(var3));
         }
      }

      this.j = var1.getX();
      this.k = var1.getY();
      if (var12) {
         this.t.union(this.u);
         this.invalidate(this.t);
         this.t.set(this.u);
      }
   }

   static void a(LockPatternView var0, float var1, float var2, long var3, Interpolator var5, PatternCellState var6, Runnable var7) {
      var0.a(var1, var2, var3, var5, var6, var7);
   }

   private void a(PatternCell var1) {
      this.i[var1.a()][var1.b()] = true;
      this.h.add(var1);
      if (!this.m) {
         this.b(var1);
      }

      this.b();
   }

   private void a(PatternCellState var1, float var2, float var3, float var4, float var5) {
      PatternLineEndAnimation var6 = new PatternLineEndAnimation(this, this, this.y, var1, var2, var4, var3, var5);
      var6.a(100L, true);
      var1.f = var6;
   }

   private float b(int var1) {
      return this.getPaddingTop() + var1 * this.r + this.r / 2.0F;
   }

   private int b(float var1) {
      float var2 = this.q;
      float var3 = var2 * this.p;
      float var4 = this.getPaddingLeft();
      float var5 = (var2 - var3) / 2.0F;
      int var7 = 0;

      while (true) {
         if (var7 >= 3) {
            var7 = -1;
            break;
         }

         float var6 = var7 * var2 + (var5 + var4);
         if (var1 >= var6 && var1 <= var6 + var3) {
            break;
         }

         var7++;
      }

      return var7;
   }

   static Interpolator b(LockPatternView var0) {
      return var0.y;
   }

   private PatternCell b(float var1, float var2) {
      Object var6 = null;
      int var4 = this.a(var2);
      PatternCell var5;
      if (var4 < 0) {
         var5 = (PatternCell)var6;
      } else {
         int var3 = this.b(var1);
         var5 = (PatternCell)var6;
         if (var3 >= 0) {
            var5 = (PatternCell)var6;
            if (!this.i[var4][var3]) {
               var5 = PatternCell.a(var4, var3);
            }
         }
      }

      return var5;
   }

   private void b() {
      if (this.g != null) {
         this.g.a(this.h);
      }
   }

   private void b(MotionEvent var1) {
      if (!this.h.isEmpty()) {
         this.o = false;
         this.h();
         this.d();
         this.invalidate();
      }
   }

   private void b(PatternCell var1) {
      PatternCellState var2 = this.a[var1.a][var1.b];
      this.a(1.0F, this.c, 96L, this.z, var2, new RestorePatternDotSize(this, var2));
      this.a(var2, this.j, this.k, this.a(var1.b), this.b(var1.a));
   }

   private void c() {
      if (this.g != null) {
         this.g.a();
      }
   }

   private void c(MotionEvent var1) {
      this.f();
      float var3 = var1.getX();
      float var7 = var1.getY();
      PatternCell var8 = this.a(var3, var7);
      if (var8 != null) {
         this.o = true;
         this.c();
      } else if (this.o) {
         this.o = false;
         this.e();
      }

      if (var8 != null) {
         float var2 = this.a(var8.b);
         float var5 = this.b(var8.a);
         float var4 = this.q / 2.0F;
         float var6 = this.r / 2.0F;
         this.invalidate((int)(var2 - var4), (int)(var5 - var6), (int)(var2 + var4), (int)(var5 + var6));
      }

      this.j = var3;
      this.k = var7;
   }

   private void d() {
      if (this.g != null) {
         this.g.b(this.h);
      }
   }

   private void e() {
      if (this.g != null) {
         this.g.b();
      }
   }

   private void f() {
      this.h.clear();
      this.g();
      this.invalidate();
   }

   private void g() {
      for (int var1 = 0; var1 < 3; var1++) {
         for (int var2 = 0; var2 < 3; var2++) {
            this.i[var1][var2] = false;
         }
      }
   }

   private void h() {
      for (int var1 = 0; var1 < 3; var1++) {
         for (int var2 = 0; var2 < 3; var2++) {
            PatternCellState var3 = this.a[var1][var2];
            if (var3.f != null) {
               var3.f.c();
               var3.d = Float.MIN_VALUE;
               var3.e = Float.MIN_VALUE;
            }
         }
      }
   }

   public void a() {
      this.f();
   }

   public PatternCellState[][] getCellStates() {
      return this.a;
   }

   @Override
   protected void onDraw(Canvas var1) {
      ArrayList var10 = this.h;
      int var9 = var10.size();
      boolean[][] var11 = this.i;
      Path var12 = this.s;
      var12.rewind();

      for (int var7 = 0; var7 < 3; var7++) {
         float var3 = this.b(var7);

         for (int var8 = 0; var8 < 3; var8++) {
            PatternCellState var13 = this.a[var7][var8];
            float var2 = this.a(var8);
            float var5 = var13.a;
            float var4 = this.b;
            float var6 = var13.b;
            this.a(var1, (int)var2, var6 + (int)var3, var5 * var4, var11[var7][var8], var13.c);
         }
      }

      boolean var18;
      if (!this.m) {
         var18 = true;
      } else {
         var18 = false;
      }

      if (var18) {
         this.f.setColor(this.a(true));
         var18 = false;
         float var15 = 0.0F;
         float var14 = 0.0F;

         for (int var20 = 0; var20 < var9; var18 = true) {
            PatternCell var21 = (PatternCell)var10.get(var20);
            if (!var11[var21.a][var21.b]) {
               break;
            }

            float var16 = this.a(var21.b);
            float var17 = this.b(var21.a);
            if (var20 != 0) {
               PatternCellState var22 = this.a[var21.a][var21.b];
               var12.rewind();
               var12.moveTo(var15, var14);
               if (var22.d != Float.MIN_VALUE && var22.e != Float.MIN_VALUE) {
                  var12.lineTo(var22.d, var22.e);
               } else {
                  var12.lineTo(var16, var17);
               }

               var1.drawPath(var12, this.f);
            }

            var20++;
            var14 = var17;
            var15 = var16;
         }

         if (this.o && var18) {
            var12.rewind();
            var12.moveTo(var15, var14);
            var12.lineTo(this.j, this.k);
            this.f.setAlpha((int)(this.a(this.j, this.k, var15, var14) * 255.0F));
            var1.drawPath(var12, this.f);
         }
      }
   }

   @Override
   public boolean onHoverEvent(MotionEvent var1) {
      boolean var3;
      if (VERSION.SDK_INT >= 14) {
         if (((AccessibilityManager)this.getContext().getSystemService("accessibility")).isTouchExplorationEnabled()) {
            int var2 = var1.getAction();
            switch (var2) {
               case 7:
                  var1.setAction(2);
               case 8:
               default:
                  break;
               case 9:
                  var1.setAction(0);
                  break;
               case 10:
                  var1.setAction(1);
            }

            this.onTouchEvent(var1);
            var1.setAction(var2);
         }

         var3 = super.onHoverEvent(var1);
      } else {
         var3 = false;
      }

      return var3;
   }

   @Override
   protected void onMeasure(int var1, int var2) {
      int var3 = this.getSuggestedMinimumWidth();
      int var4 = this.getSuggestedMinimumHeight();
      var3 = this.a(var1, var3);
      var1 = this.a(var2, var4);
      switch (this.v) {
         case 0:
            var1 = Math.min(var3, var1);
            var2 = var1;
            break;
         case 1:
            var1 = Math.min(var3, var1);
            var2 = var3;
            break;
         case 2:
            var2 = Math.min(var3, var1);
            break;
         default:
            var2 = var3;
      }

      this.setMeasuredDimension(var2, var1);
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      this.q = (var1 - this.getPaddingLeft() - this.getPaddingRight()) / 3.0F;
      this.r = (var2 - this.getPaddingTop() - this.getPaddingBottom()) / 3.0F;
   }

   @Override
   public boolean onTouchEvent(MotionEvent var1) {
      boolean var3 = true;
      boolean var2;
      if (this.l && this.isEnabled()) {
         switch (var1.getAction()) {
            case 0:
               this.c(var1);
               var2 = var3;
               break;
            case 1:
               this.b(var1);
               var2 = var3;
               break;
            case 2:
               this.a(var1);
               var2 = var3;
               break;
            case 3:
               var2 = var3;
               if (this.o) {
                  this.o = false;
                  this.f();
                  this.e();
                  var2 = var3;
               }
               break;
            default:
               var2 = false;
         }
      } else {
         var2 = false;
      }

      return var2;
   }

   public void setInStealthMode(boolean var1) {
      this.m = var1;
   }

   public void setOnPatternListener(PatternInputListener var1) {
      this.g = var1;
   }

   public void setTactileFeedbackEnabled(boolean var1) {
      this.n = var1;
   }
}
