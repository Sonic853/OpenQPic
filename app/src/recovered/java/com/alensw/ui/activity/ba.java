package com.alensw.ui.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings.System;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.view.ContainerLayout;
import com.alensw.ui.view.TitleBar;
import java.util.Stack;

public class ba extends PreferenceActivity {
   public int a;
   public int b;
   protected int c;
   protected int d;
   protected TitleBar e;
   protected final OnClickListener f;
   private final Stack g = new Stack();
   private int h;
   private int i;
   private Animation j;

   public ba() {
      this.f = new com.alensw.ui.activity.bb(this);
   }

   static Stack a(com.alensw.ui.activity.ba var0) {
      return var0.g;
   }

   protected void a(PreferenceScreen var1) {
      this.setPreferenceScreen(var1);
      if (this.h != 0) {
         try {
            if (this.j == null) {
               this.j = AnimationUtils.loadAnimation(this, this.h);
            }

            this.getListView().startAnimation(this.j);
         } catch (Throwable var2) {
         }
      }
   }

   @TargetApi(11)
   public void a(MenuItem var1, int var2) {
      if (var1 != null) {
         if (com.alensw.ui.activity.ao.F) {
            var1.setShowAsAction(var2);
         }

         if (this.e != null && var1 instanceof com.alensw.b.g.l) {
            this.e.a(var1, var2);
         }
      }
   }

   public void a(ViewGroup var1) {
      for (int var2 = var1.getChildCount() - 1; var2 >= 0; var2--) {
         View var4 = var1.getChildAt(var2);
         if (var4 instanceof ViewGroup) {
            this.a((ViewGroup)var4);
         } else if (var4.getId() == 16908310 && VERSION.SDK_INT < 11 && var4 instanceof TextView) {
            ((TextView)var4).setTextSize(2, 18.0F);
         } else if (var4 instanceof CheckBox) {
            Drawable var3 = com.alensw.b.g.a.a(var4.getResources(), this.d, this.c, false);
            ((CheckBox)var4).setButtonDrawable(var3);
         } else if (var4.getClass() == ImageView.class) {
            var4.setBackgroundDrawable(null);
            ((ImageView)var4).setImageDrawable(null);
         }
      }
   }

   protected boolean a(PreferenceGroup var1, String var2) {
      int var4 = var1.getPreferenceCount();
      int var3 = 0;

      boolean var5;
      while (true) {
         if (var3 >= var4) {
            var5 = false;
            break;
         }

         Preference var7 = var1.getPreference(var3);
         String var6 = var7.getKey();
         if (var6 != null && var6.equals(var2)) {
            var1.removePreference(var7);
            var5 = true;
            break;
         }

         if (var7 instanceof PreferenceGroup && this.a((PreferenceGroup)var7, var2)) {
            var5 = true;
            break;
         }

         var3++;
      }

      return var5;
   }

   @Override
   public void addPreferencesFromResource(int var1) {
      super.addPreferencesFromResource(var1);
      PreferenceScreen var2 = this.getPreferenceScreen();
      if (var2 != null) {
         var2.setTitle(this.getTitle());
         this.b(var2);
      }
   }

   protected void b(PreferenceScreen var1) {
      PreferenceManager var8 = this.getPreferenceManager();
      int var4 = var1.getPreferenceCount();

      for (int var2 = 0; var2 < var4; var2++) {
         Preference var7 = var1.getPreference(var2);
         if (var7 instanceof PreferenceScreen) {
            PreferenceScreen var9 = (PreferenceScreen)var7;
            PreferenceScreen var6 = var8.createPreferenceScreen(this);
            int var5 = var9.getPreferenceCount();

            for (int var3 = 0; var3 < var5; var3++) {
               var6.addPreference(var9.getPreference(var3));
            }

            var6.setKey(var9.getKey());
            var6.setTitle(var9.getTitle());
            var7.setOnPreferenceClickListener(new com.alensw.ui.activity.be(this, var6));
            this.b(var9);
            var9.removeAll();
         }
      }
   }

   @Override
   public void onBackPressed() {
      if (this.g.isEmpty()) {
         super.onBackPressed();
      } else {
         this.a((PreferenceScreen)this.g.pop());
      }
   }

   @TargetApi(11)
   @Override
   public void onCreate(Bundle var1) {
      if (System.getFloat(this.getContentResolver(), "transition_animation_scale", 1.0F) > 0.0F) {
         this.h = this.getResources().getIdentifier("dialog_enter", "anim", "android");
         this.i = this.getResources().getIdentifier("dialog_exit", "anim", "android");
         super.overridePendingTransition(this.h, this.i);
      }

      SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(this);
      this.a = var2.getInt("main_theme", 0);
      this.setTheme(com.alensw.ui.activity.ao.h(this.a));
      if (this.a != -3 && this.a != -2) {
         this.b = com.alensw.ui.activity.ao.b(this, com.alensw.PicFolder.R.attr.colorActionBar);
      } else {
         this.b = var2.getInt("actionbar_color", this.b);
      }

      this.d = this.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.check_box_size);
      this.c = com.alensw.ui.activity.ao.b(this, com.alensw.PicFolder.R.attr.colorMenuDrawable);
      if (VERSION.SDK_INT < 11) {
         this.requestWindowFeature(7);
      }

      super.onCreate(var1);
      ((QuickApp)this.getApplication()).a((Activity)this);
      Window var3 = this.getWindow();
      ListView var4 = this.getListView();
      if (com.alensw.ui.activity.ao.F) {
         ActionBar var5 = this.getActionBar();
         var5.setDisplayHomeAsUpEnabled(true);
         if (this.a == -3 || this.a == -2) {
            var5.setBackgroundDrawable(new ColorDrawable(this.b));
            if (VERSION.SDK_INT >= 14) {
               var5.setSplitBackgroundDrawable(new ColorDrawable(this.b));
            }

            if (VERSION.SDK_INT >= 21) {
               var3.setStatusBarColor(this.b);
            }
         }

         com.alensw.ui.activity.ao.a(this, com.alensw.ui.activity.ao.b(this, com.alensw.PicFolder.R.attr.colorActionIcon));
      } else {
         var3.setFeatureInt(7, com.alensw.PicFolder.R.layout.title_bar);
         TitleBar var6 = (TitleBar)this.findViewById(com.alensw.PicFolder.R.id.title_bar);
         var6.setBackgroundColor(this.b);
         var6.setTitle(super.getTitle());
         var6.setOnHomeClickListener(this.f);
         var6.a(true);
         this.e = var6;
      }

      if (VERSION.SDK_INT < 21) {
         var4.setOnHierarchyChangeListener(new com.alensw.ui.activity.bc(this));
      }
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      ((QuickApp)this.getApplication()).b(this);
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
            var2 = false;
      }

      return var2;
   }

   @Override
   public void onPostCreate(Bundle var1) {
      super.onPostCreate(var1);
      if (this.e != null) {
         this.e.d();
      }
   }

   @Override
   public boolean onPrepareOptionsMenu(Menu var1) {
      if (this.e != null) {
         this.e.a(var1);
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
      super.onStop();
      ((QuickApp)this.getApplication()).d(this);
   }

   @Override
   protected void onTitleChanged(CharSequence var1, int var2) {
      if (this.e != null) {
         this.e.setTitle(var1);
      }

      super.onTitleChanged(var1, var2);
   }

   @Override
   public void setContentView(int var1) {
      this.setContentView(this.getLayoutInflater().inflate(var1, null));
   }

   @Override
   public void setContentView(View var1) {
      boolean var2;
      if (VERSION.SDK_INT == 19) {
         var2 = true;
      } else {
         var2 = false;
      }

      Object var3 = var1;
      if (var2) {
         var3 = new com.alensw.ui.activity.bd(this, this);
         ((ContainerLayout)var3).addView(var1);
      }

      super.setContentView((View)var3);
      if (var2) {
         Window var4 = this.getWindow();
         var4.addFlags(67108864);
         var4.getDecorView().setSystemUiVisibility(1280);
      }
   }
}
