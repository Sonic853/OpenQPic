package com.alensw.PicFolder;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.TrackInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.SeekBar.OnSeekBarChangeListener;
public class PlayerActivity extends com.alensw.ui.activity.p {
   protected ViewGroup a;
   private boolean ab;
   private int ac;
   private Drawable ad;
   private Drawable ae;
   private final Rect af;
   private final Rect ag;
   protected ImageView b;
   protected VolumeBar c;
   protected SeekBar d;
   protected TextView e;
   protected TextView f;
   protected ViewGroup g;
   protected ImageView h;
   protected ImageView i;
   protected PlayerView j;
   protected VideoView k;
   protected MediaPlayer l;
   protected MenuItem m;
   protected int n;
   protected int o;
   protected boolean p;
   protected boolean q;
   protected boolean r;
   protected boolean s;
   protected AudioManager t;
   protected TrackInfo[] u;
   protected BroadcastReceiver v;
   protected final com.alensw.ui.d.b w = new com.alensw.ui.d.b();
   protected final com.alensw.b.c.d x = new com.alensw.b.c.d(16);
   protected final OnGlobalLayoutListener y = new com.alensw.PicFolder.al(this);
   protected final OnSeekBarChangeListener z = new com.alensw.PicFolder.am(this);

   public PlayerActivity() {
      this.ab = true;
      this.af = new Rect();
      this.ag = new Rect();
   }

   static View a(PlayerActivity var0) {
      return var0.I;
   }

   static View b(PlayerActivity var0) {
      return var0.I;
   }

   static View c(PlayerActivity var0) {
      return var0.I;
   }

   static View d(PlayerActivity var0) {
      return var0.I;
   }

   static ViewGroup e(PlayerActivity var0) {
      return var0.L;
   }

   static Handler f(PlayerActivity var0) {
      return var0.A;
   }

   static Handler g(PlayerActivity var0) {
      return var0.A;
   }

   static Handler h(PlayerActivity var0) {
      return var0.A;
   }

   static Handler i(PlayerActivity var0) {
      return var0.A;
   }

   static Handler j(PlayerActivity var0) {
      return var0.A;
   }

   public void a(Rect var1) {
      if (!this.ag.equals(var1)) {
         this.ag.set(var1);
         this.f();
      }
   }

   @Override
   public void a(boolean var1) {
      super.a(var1);
      byte var2;
      if (var1) {
         var2 = 0;
      } else {
         var2 = 4;
      }

      if (this.a.getVisibility() != var2 && !var1) {
         if (!G) {
            this.a.setVisibility(8);
         }

         this.a.setVisibility(var2);
         this.a.startAnimation(this.c(var1));
      }

      if (this.g.getVisibility() != var2) {
         this.g.setVisibility(var2);
         this.g.startAnimation(this.c(var1));
      }

      if (var1) {
         this.A.sendEmptyMessage(100);
      }
   }

   @Override
   public boolean a() {
      boolean var1;
      if (!super.a() && this.a.getVisibility() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected int b(boolean var1) {
      int var4 = this.k.getDuration();
      if (this.ac != var4) {
         this.ac = var4;
         this.e.setText(com.alensw.b.l.b.a((long)var4));
      }

      long var5 = this.d.getMax();
      int var3 = this.k.getCurrentPosition();
      if (var4 > 0 && !this.r) {
         this.d.setProgress((int)(var3 * var5 / var4));
      }

      int var2 = this.k.getBufferPercentage();
      this.d.setSecondaryProgress((int)(var5 * var2 / 100L));
      var2 = var3;
      if (var1) {
         var2 = var3;
         if (!this.r) {
            String var8 = "";
            String var7 = var8;
            var2 = var3;
            if (this.s) {
               var7 = var8;
               var2 = var3;
               if (var4 > 0) {
                  var2 = var4 - var3;
                  var7 = "" + "-";
               }
            }

            var7 = var7 + com.alensw.b.l.b.a((long)var2);
            this.f.setText(var7);
         }
      }

      return var2;
   }

   protected void b() {
      ImageView var1 = (ImageView)this.g.findViewById(com.alensw.PicFolder.R.id.rewind);
      var1.setOnClickListener(this.Z);
      a(var1, new com.alensw.ui.d.g(3), -855638017);
      com.alensw.ui.d.d.a(var1);
      var1 = (ImageView)this.g.findViewById(com.alensw.PicFolder.R.id.forward);
      var1.setOnClickListener(this.Z);
      a(var1, new com.alensw.ui.d.g(2), -855638017);
      com.alensw.ui.d.d.a(var1);
   }

   protected void c() {
      if (this.i.getVisibility() == 0) {
         this.i.setVisibility(8);
         this.i.startAnimation(this.c(false));
      }
   }

   protected void d() {
      ImageView var2 = this.h;
      byte var1;
      if (!this.k.isPlaying()) {
         var1 = 0;
      } else {
         var1 = 1;
      }

      a(var2, new com.alensw.ui.d.g(var1), -855638017);
   }

   protected void e() {
      boolean var1;
      if (this.c.getProgress() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      Drawable var2;
      if (var1) {
         var2 = this.ad;
      } else {
         var2 = this.ae;
      }

      Object var3 = var2;
      if (var2 == null) {
         var3 = this.getResources();
         String var4;
         if (var1) {
            var4 = "ic_volume_small";
         } else {
            var4 = "ic_volume_off_small";
         }

         var3 = com.alensw.b.l.b.a((Resources)var3, var4);
         var2 = (Drawable)var3;
         if (var3 == null) {
            var2 = new ColorDrawable(0);
         }

         if (var1) {
            this.ad = var2;
            var3 = var2;
         } else {
            this.ae = var2;
            var3 = var2;
         }
      }

      if (var3 != this.b.getDrawable()) {
         this.b.setImageDrawable((Drawable)var3);
      }
   }

   protected void f() {
      this.a(this.ag, this.af);
      this.L.setPadding(this.af.left, 0, this.af.right, this.af.bottom);
   }

   @Override
   public boolean handleMessage(Message var1) {
      switch (var1.what) {
         case 100:
            boolean var3;
            if (this.L.getVisibility() == 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            int var2 = this.b(var3);
            this.A.sendEmptyMessageDelayed(100, 1000 - var2 % 1000);
         default:
            return true;
      }
   }

   @TargetApi(19)
   @Override
   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      super.setContentView(com.alensw.PicFolder.R.layout.player);
      Intent var6 = this.getIntent();
      Uri var8 = var6.getData();
      if (var8 == null) {
         this.finish();
      } else {
         Resources var7 = this.getResources();
         SharedPreferences var9 = PreferenceManager.getDefaultSharedPreferences(this);
         boolean var5;
         if (!QuickApp.d && !var9.getBoolean("auto_hide_navigate_bar", false)) {
            var5 = false;
         } else {
            var5 = true;
         }

         if (!this.e(var5)) {
            this.getWindow().addFlags(1024);
         }

         this.setTitle(com.alensw.PicFolder.R.string.loading);
         this.i(true);
         this.I.getViewTreeObserver().addOnGlobalLayoutListener(this.y);
         this.n = var6.getIntExtra("android.intent.extra.screenOrientation", com.alensw.b.l.b.a(var9.getString("screen_orientation", "-1"), -1));
         if (this.n != -2) {
            this.a(this.n);
         }

         this.q = var6.getBooleanExtra("android.intent.extra.finishOnCompletion", true);
         this.t = (AudioManager)this.getSystemService("audio");
         this.a = (ViewGroup)this.I.findViewById(com.alensw.PicFolder.R.id.volume_bar);
         this.b = (ImageView)this.a.findViewById(com.alensw.PicFolder.R.id.vol_icon);
         this.c = (VolumeBar)this.a.findViewById(com.alensw.PicFolder.R.id.volume);
         this.c.setMax(this.t.getStreamMaxVolume(3));
         this.c.setProgress(this.t.getStreamVolume(3));
         this.g = (ViewGroup)this.I.findViewById(com.alensw.PicFolder.R.id.play_bar);
         this.h = (ImageView)this.g.findViewById(com.alensw.PicFolder.R.id.play);
         this.h.setOnClickListener(this.Z);
         com.alensw.ui.d.d.a(this.h);
         this.b();
         this.i = (ImageView)this.g.findViewById(com.alensw.PicFolder.R.id.replay);
         this.d = (SeekBar)this.L.findViewById(com.alensw.PicFolder.R.id.seek);
         this.d.setMax(QuickApp.f);
         this.d.setProgress(0);
         this.d.setOnSeekBarChangeListener(this.z);
         float var2 = this.getResources().getDisplayMetrics().density;
         this.e = (TextView)this.L.findViewById(com.alensw.PicFolder.R.id.duration);
         this.e.setTextColor(-855638017);
         this.e.setShadowLayer(var2 / 2.0F, var2 / 4.0F, var2 / 4.0F, -16777216);
         this.f = (TextView)this.L.findViewById(com.alensw.PicFolder.R.id.time);
         this.f.setTextColor(-855638017);
         this.f.setShadowLayer(var2 / 2.0F, var2 / 4.0F, var2 / 4.0F, -16777216);
         this.f.setOnClickListener(new com.alensw.PicFolder.an(this));
         this.j = (PlayerView)this.I.findViewById(com.alensw.PicFolder.R.id.player);
         this.j.a(this.d, this.c, this.z);
         this.j.a(new com.alensw.PicFolder.ao(this, var8));
         this.k = this.j.a();
         this.k.setOnCompletionListener(new com.alensw.PicFolder.ar(this));
         this.k.setOnErrorListener(new com.alensw.PicFolder.as(this, var8));
         if (!H) {
            this.v = new com.alensw.PicFolder.at(this);
            this.registerReceiver(this.v, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
         }

         this.d();
         this.x.a(com.alensw.b.l.b.b(this, "video_bookmark"));
         int var4;
         if (var1 != null && var1.containsKey("cur_pos")) {
            var4 = var1.getInt("cur_pos", 0);
         } else {
            Integer var10 = (Integer)this.x.b(var8.toString());
            int var3;
            if (var10 != null) {
               var3 = var10;
            } else {
               var3 = 0;
            }

            var4 = var3;
            if (var3 > 0) {
               com.b.a.l var11 = com.b.a.b.a(var7, com.alensw.PicFolder.R.raw.ic_menu_retry, -855638017, var7.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_material_icon_size));
               this.i.setImageDrawable(var11);
               this.i.setVisibility(0);
               this.i.setOnClickListener(new com.alensw.PicFolder.au(this));
               var4 = var3;
            }
         }

         this.j.a(var6, var4);
      }
   }

   @TargetApi(11)
   @Override
   public boolean onCreateOptionsMenu(Menu var1) {
      com.alensw.b.g.b.a(this, com.alensw.PicFolder.R.menu.player, var1);
      boolean var3;
      if (this.u != null && this.o > 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      MenuItem var5 = var1.findItem(com.alensw.PicFolder.R.id.tracks);
      this.a(var5, var3);
      if (var3) {
         android.support.a.a.m var4 = new android.support.a.a.m(this);
         var4.a(this.X);
         var5.setIcon(var4);
      }

      this.a(var1, com.alensw.PicFolder.R.id.share, 2);
      this.a(var1.findItem(com.alensw.PicFolder.R.id.rotation), 2);
      this.m = var1.findItem(com.alensw.PicFolder.R.id.battery);
      if (!H) {
         int var2 = this.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_material_icon_size);
         this.m.setIcon(com.alensw.ui.d.d.a(this.w, var2, var2, this.X));
         this.a(this.m, 2);
      } else {
         this.a(this.m, false);
      }

      return true;
   }

   @Override
   protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      Uri uri=intent.getData();
      if (uri==null || uri.equals(getIntent().getData())) return;
      // singleTask receives a new URI when content playback falls back to our HTTPS listener.
      this.j.b();
      setIntent(intent);
      this.q=intent.getBooleanExtra("android.intent.extra.finishOnCompletion",true);
      this.p=false; this.r=false; this.l=null; this.u=null; this.o=0;
      this.d.setProgress(0); this.d.setSecondaryProgress(0); this.i.setVisibility(View.GONE);
      setTitle(com.alensw.PicFolder.R.string.loading); this.i(true);
      this.j.a(new com.alensw.PicFolder.ao(this,uri));
      this.k.setOnErrorListener(new com.alensw.PicFolder.as(this,uri));
      Integer bookmark=(Integer)this.x.b(uri.toString());
      this.j.a(intent,bookmark==null?0:bookmark); invalidateOptionsMenu();
   }

   @Override
   public void onDestroy() {
       super.onDestroy();
       if (this.v != null) {
          this.unregisterReceiver(this.v);
          this.v = null;
       }

      if (this.I != null) {
         this.I.getViewTreeObserver().removeGlobalOnLayoutListener(this.y);
      }

      if (this.j != null) {
         this.j.b();
      }
   }

   @Override
   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3 = true;
      switch (var1) {
         case 24:
         case 25:
            AudioManager var5 = this.t;
            byte var4;
            if (var1 == 25) {
               var4 = -1;
            } else {
               var4 = 1;
            }

            var5.adjustVolume(var4, 0);
            this.u();
            if (this.a.getVisibility() != 0) {
               if (!G) {
                  this.a.setVisibility(8);
               }

               this.a.setVisibility(0);
               this.a.startAnimation(this.c(true));
            }

            this.c.setProgress(this.t.getStreamVolume(3));
            this.e();
            this.f(1500);
            break;
         default:
            var3 = super.onKeyDown(var1, var2);
      }

      return var3;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem var1) {
      short var2 = 0;
      boolean var6 = true;
      int var4 = var1.getItemId();
      boolean var5;
      switch (var4) {
         case com.alensw.PicFolder.R.id.share:
            Intent var7 = this.getIntent();
            new com.alensw.ui.c.df(this, var7.getData(), 'V', var7.getType(), null).a(this.findViewById(var4));
            var5 = var6;
            break;
         case com.alensw.PicFolder.R.id.rewind:
         case com.alensw.PicFolder.R.id.forward:
            int var3 = this.k.getCurrentPosition();
            if (var4 == com.alensw.PicFolder.R.id.forward) {
               var2 = 10000;
            } else {
               var2 = -10000;
            }

            this.k.seekTo(var2 + var3);
            this.b(true);
            this.f(3000);
            var5 = var6;
            break;
         case com.alensw.PicFolder.R.id.play:
            if (this.k.isPlaying()) {
               this.k.pause();
               this.A.removeMessages(100);
               this.u();
            } else {
               this.k.start();
               this.A.sendEmptyMessage(100);
               this.a(false);
            }

            this.d();
            var5 = var6;
            break;
         case com.alensw.PicFolder.R.id.tracks:
            var5 = var6;
            if (this.u != null) {
               var5 = var6;
               if (this.o > 1) {
                  com.alensw.b.g.b.a(this, this.findViewById(var4), new com.alensw.PicFolder.av(this));
                  var5 = var6;
               }
            }
            break;
         case com.alensw.PicFolder.R.id.rotation:
            if (this.getRequestedOrientation() == 0) {
               var2 = 1;
            }

            this.a(var2);
            var5 = var6;
            break;
         default:
            var5 = super.onOptionsItemSelected(var1);
      }

      return var5;
   }

   @Override
   public void onPause() {
      Uri var2 = this.getIntent().getData();
       if (var2 != null) {
          String var3 = var2.toString();
         int var1 = this.k.getCurrentPosition();
         if (!this.p && var1 > 5000 && var1 + 5000 < this.k.getDuration() && this.k.getDuration() > 60000) {
            this.x.a(var3, var1);
         } else {
            this.x.c(var3);
         }

         this.x.h();
      }

      this.j.c();
      super.onPause();
   }

   @Override
   public void onResume() {
      if (this.ab) {
         this.j.d();
      }

      super.onResume();
   }

   @Override
   protected void onSaveInstanceState(Bundle var1) {
       if (this.p) {
          var1.remove("cur_pos");
      } else {
         var1.putInt("cur_pos", this.k.getCurrentPosition());
      }
   }

   @Override
   public void onWindowFocusChanged(boolean var1) {
      if (!this.ab && var1) {
         this.j.d();
      }

      this.ab = var1;
      super.onWindowFocusChanged(var1);
   }
}
