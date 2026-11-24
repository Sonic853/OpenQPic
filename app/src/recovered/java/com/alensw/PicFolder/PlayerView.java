package com.alensw.PicFolder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.VideoView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressLint("Instantiatable")
class PlayerView extends FrameLayout {
   protected final OnGestureListener a;
   private int b;
   private int c;
   private int d;
   private int e;
   private boolean f;
   private boolean g;
   private SeekBar h;
   private SeekBar i;
   private OnSeekBarChangeListener j;
   private final Handler k;
   private final VideoView l;
   private final GestureDetector m;
   private int n = 0;
   private int o;
   private float p;
   private float[] q;

   public PlayerView(Context var1) {
      this(var1, null);
   }

   @TargetApi(8)
   public PlayerView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.a = new com.alensw.PicFolder.ax(this);
      this.o = 0;
      this.p = 1.0F;
      this.q = new float[3];
      this.k = new Handler();
      this.m = new GestureDetector(var1, this.a, this.k);
      this.l = new VideoView(var1);
      this.addView(this.l, new FrameLayout.LayoutParams(-1, -1, 17));
      this.b = (int)(this.getResources().getDisplayMetrics().density * 16.0F);
   }

   static float a(PlayerView var0, float var1) {
      var0.p = var1;
      return var1;
   }

   static int a(PlayerView var0) {
      return var0.b;
   }

   static int a(PlayerView var0, int var1) {
      var0.n = var1;
      return var1;
   }

   static int b(PlayerView var0, int var1) {
      var0.c = var1;
      return var1;
   }

   static void b(PlayerView var0) {
      var0.f();
   }

   static int c(PlayerView var0, int var1) {
      var0.d = var1;
      return var1;
   }

   static OnSeekBarChangeListener c(PlayerView var0) {
      return var0.j;
   }

   static int d(PlayerView var0) {
      return var0.n;
   }

   static SeekBar e(PlayerView var0) {
      return var0.h;
   }

   static VideoView f(PlayerView var0) {
      return var0.l;
   }

   private void f() {
      float[] var4 = this.e();
      float var1 = this.p;

      for (int var3 = 0; var3 < 3; var3++) {
         this.o = (this.o + 1) % 3;
         var1 = var4[this.o];
         if (Math.abs(var1 - this.p) > 0.05F) {
            break;
         }
      }

      if (var1 != this.p) {
         float var2 = this.p;
         new com.alensw.PicFolder.az(this, this.l, new com.alensw.ui.view.cc(), var2, var1 - var2, var1).a(QuickApp.i, true);
      }
   }

   static SeekBar g(PlayerView var0) {
      return var0.i;
   }

   static int h(PlayerView var0) {
      return var0.o;
   }

   public final VideoView a() {
      return this.l;
   }

   public void a(Intent intent, int i) {
        try {
            Uri data = intent.getData();
            android.os.Bundle bundleExtra = intent.getBundleExtra("com.android.browser.headers");
            if (bundleExtra != null) {
                try {
                    Method declaredMethod = this.l.getClass().getDeclaredMethod("setVideoURI", Uri.class, Map.class);
                    Set<String> setKeySet = bundleExtra.keySet();
                    HashMap map = new HashMap(setKeySet.size());
                    for (String str : setKeySet) {
                        map.put(str, bundleExtra.getString(str));
                    }
                    declaredMethod.invoke(this.l, data, map);
                } catch (Throwable th) {
                    this.l.setVideoURI(data);
                }
            } else {
                this.l.setVideoURI(data);
            }
            if (i != 0) {
                this.l.seekTo(i);
            }
            this.l.requestFocus();
            this.l.start();
        } catch (Throwable th2) {
        }
    }

   public void a(OnPreparedListener var1) {
      this.l.setOnPreparedListener(new com.alensw.PicFolder.ay(this, var1));
   }

   public void a(SeekBar var1, SeekBar var2, OnSeekBarChangeListener var3) {
      this.h = var1;
      this.i = var2;
      this.j = var3;
   }

   public void b() {
      try {
         if (this.l != null) {
            this.l.stopPlayback();
         }
      } catch (Throwable var2) {
      }
   }

   public void c() {
      try {
         this.f = true;
         this.e = this.l.getCurrentPosition();
         if (VERSION.SDK_INT >= 8) {
            this.l.suspend();
         } else {
            this.g = this.l.isPlaying();
            if (this.g) {
               this.l.pause();
            }
         }
      } catch (Throwable var2) {
      }
   }

   public void d() {
      if (this.f) {
         try {
            if (VERSION.SDK_INT >= 8) {
               this.l.seekTo(this.e);
               this.l.resume();
            } else {
               if (this.g) {
                  this.l.start();
               }

               if (this.e != 0) {
                  this.l.seekTo(this.e);
               }
            }
         } catch (Throwable var2) {
         }
      }
   }

   public float[] e() {
      float var1 = this.getWidth();
      float var2 = this.getHeight();
      if (this.c > 0) {
         var1 /= this.c;
      } else {
         var1 = 1.0F;
      }

      if (this.d > 0) {
         var2 /= this.d;
      } else {
         var2 = 1.0F;
      }

      this.q[0] = Math.min(var1, var2);
      this.q[1] = Math.max(var1, var2);
      this.q[2] = 1.0F;
      return this.q;
   }

   @Override
   protected boolean fitSystemWindows(Rect var1) {
      boolean var2 = super.fitSystemWindows(var1);
      ((PlayerActivity)this.getContext()).a(var1);
      return var2;
   }

   @Override
   public void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.o == 0 && this.p == this.q[0]) {
         super.onLayout(var1, var2, var3, var4, var5);
      } else {
         int var11 = var4 - var2;
         int var10 = var5 - var3;
         int var9 = var2;
         int var8 = var3;
         int var7 = var4;
         int var6 = var5;
         if (var11 > 0) {
            var9 = var2;
            var8 = var3;
            var7 = var4;
            var6 = var5;
            if (var10 > 0) {
               var9 = var2;
               var8 = var3;
               var7 = var4;
               var6 = var5;
               if (this.c > 0) {
                  var9 = var2;
                  var8 = var3;
                  var7 = var4;
                  var6 = var5;
                  if (this.d > 0) {
                     var5 = Math.round(this.c * this.p);
                     var4 = Math.round(this.d * this.p);
                     var9 = var2 + (var11 - var5) / 2;
                     var8 = var3 + (var10 - var4) / 2;
                     var7 = var9 + var5;
                     var6 = var8 + var4;
                  }
               }
            }
         }

         this.l.layout(var9, var8, var7, var6);
      }
   }

   @Override
   public void onSizeChanged(int var1, int var2, int var3, int var4) {
      if (var1 > 0 && var2 > 0 && this.c > 0 && this.d > 0) {
         this.o = 0;
         this.p = this.e()[this.o];
         this.requestLayout();
      }
   }

   @Override
   public boolean onTouchEvent(MotionEvent var1) {
      switch (var1.getAction() & 0xFF) {
         case 1:
            if (this.n != 0 && this.j != null) {
               SeekBar var2;
               if (this.n == 1) {
                  var2 = this.h;
               } else {
                  var2 = this.i;
               }

               this.n = 0;
               this.j.onStopTrackingTouch(var2);
            } else {
               ((PlayerActivity)this.getContext()).f(3000);
            }
         default:
            return this.m.onTouchEvent(var1);
      }
   }
}
