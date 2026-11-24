package com.alensw.ui.view;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
public class ContainerLayout extends FrameLayout {
   public static final boolean b;
   private com.alensw.ui.view.u a;
   private boolean c;
   private final int d;
   private final int e;
   private final int f;
   private final int g;
   private final Rect h;
   private final Paint i;

   static {
      boolean var0;
      if (VERSION.SDK_INT == 19) {
         var0 = true;
      } else {
         var0 = false;
      }

      b = var0;
   }

   public ContainerLayout(Context var1) {
      this(var1, null);
   }

   @TargetApi(11)
   public ContainerLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      boolean var5 = false;
      this.h = new Rect();
      this.i = new Paint(1);
      if (b) {
         Resources var10 = var1.getResources();
         Theme var6 = var1.getTheme();
         TypedValue var7 = new TypedValue();
         var6.resolveAttribute(com.alensw.PicFolder.R.attr.colorNavigateBar, var7, true);
         this.d = var10.getColor(var7.resourceId);
         var6.resolveAttribute(com.alensw.PicFolder.R.attr.colorStatusBar, var7, true);
         int var3 = var10.getColor(var7.resourceId);
         if (var1 instanceof com.alensw.ui.activity.ao) {
            com.alensw.ui.activity.ao var9 = (com.alensw.ui.activity.ao)var1;
            boolean var14;
            if (var9.T != -3 && var9.T != -2) {
               var14 = false;
            } else {
               var14 = true;
            }

            if (var14) {
               var3 = var9.V;
            }

            this.e = var3;
         } else if (!(var1 instanceof com.alensw.ui.activity.ba)) {
            this.e = var3;
         } else {
            com.alensw.ui.activity.ba var8 = (com.alensw.ui.activity.ba)var1;
            boolean var4;
            if (var8.a != -3 && var8.a != -2) {
               var4 = false;
            } else {
               var4 = true;
            }

            if (var4) {
               var3 = var8.b;
            }

            this.e = var3;
         }

         var3 = var10.getIdentifier("status_bar_height", "dimen", "android");
         int var15 = var10.getIdentifier("navigation_bar_size", "dimen", "android");
         if (var3 != 0) {
            var3 = var10.getDimensionPixelSize(var3);
         } else {
            var3 = 0;
         }

         this.g = var3;
         if (var15 != 0) {
            var3 = var10.getDimensionPixelSize(var15);
         } else {
            var3 = 0;
         }

         this.f = var3;
         if ((this.getSystemUiVisibility() & 4) == 0) {
            var5 = true;
         }

         this.c = var5;
      } else {
         this.d = 0;
         this.e = 0;
         this.f = 0;
         this.g = 0;
         this.c = false;
      }
   }

   @Override
   protected void dispatchDraw(Canvas canvas) {
        int i;
        super.dispatchDraw(canvas);
        if (b && this.c) {
            Context context = getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                int i2 = activity.getWindow().getAttributes().flags;
                boolean z = (134217728 & i2) != 0;
                boolean z2 = (i2 & 67108864) != 0;
                if (z || z2) {
                    Rect rect = this.h;
                    boolean z3 = rect.right > 0;
                    ActionBar actionBar = activity.getActionBar();
                    int height = actionBar != null ? actionBar.getHeight() : 0;
                    int width = getWidth() - (z3 ? this.f : 0);
                    int height2 = getHeight();
                    if (z2) {
                        int i3 = (this.g == 0 || rect.top <= height) ? rect.top - height : this.g;
                        if (i3 > 0) {
                            this.i.setColor(this.e);
                            canvas.drawRect(0.0f, 0.0f, width, i3, this.i);
                        }
                    }
                    if (!z || z3) {
                        return;
                    }
                    if (activity instanceof com.alensw.ui.activity.ao) {
                        height = ((com.alensw.ui.activity.ao) activity).k();
                    } else if (!com.alensw.ui.activity.ao.e(activity)) {
                        height = 0;
                    }
                    if (this.f != 0) {
                        i = this.f;
                    } else {
                        i = z3 ? rect.right : rect.bottom - height;
                    }
                    if (i > 0) {
                        this.i.setColor(this.d);
                        canvas.drawRect(0.0f, height2 - i, width, height2, this.i);
                    }
                }
            }
        }
    }

   @Override
   protected boolean fitSystemWindows(Rect var1) {
      this.h.set(var1);
      if (this.a != null) {
         this.a.a(var1);
      }

      return super.fitSystemWindows(var1);
   }

   @Override
   public void onWindowSystemUiVisibilityChanged(int var1) {
      super.onWindowSystemUiVisibilityChanged(var1);
      boolean var2;
      if (b && (var1 & 4) == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (this.c != var2) {
         this.c = var2;
         this.invalidate();
      }
   }

   public void setOnInsetsListener(com.alensw.ui.view.u var1) {
      this.a = var1;
   }
}
