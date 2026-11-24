package com.alensw.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Build.VERSION;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.view.MenuItem;
import android.widget.Toast;
import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.view.ConfirmPreference;
import com.alensw.updater.UpdateView;
import com.alensw.utils.MIUIUtils;
import java.util.Locale;

public class MainPreference extends com.alensw.ui.activity.ba {
   private Runnable g = new com.alensw.ui.activity.z(this);

   public static Intent a(Context var0) {
      Intent var1 = new Intent("android.intent.action.SEND");
      var1.setType("text/plain");
      var1.putExtra("android.intent.extra.TEXT", var0.getString(com.alensw.PicFolder.R.string.share_message, var0.getString(com.alensw.PicFolder.R.string.github_repository_url)));
      return var1;
   }

   static Runnable a(MainPreference var0) {
      return var0.g;
   }

   private void a__$appendPatch(PreferenceScreen var1) {
      if (var1.getTitle().equals(this.getResources().getString(com.alensw.PicFolder.R.string.update_auto_desc))) {
         Intent var2 = new Intent();
         var2.setClass(this.getApplicationContext(), UpdateView.class);
         this.startActivity(var2);
      }
   }

   private void a__$appendPatch2(PreferenceScreen var1) {
      if (var1.getTitle().equals(this.getString(com.alensw.PicFolder.R.string.view))) {
         try {
            Preference var2 = this.findPreference("disable_battery_opt");
            if (this.isBatteryOptimizationEnabled(false)) {
               this.getPreferenceScreen().removePreference(var2);
            }

            com.alensw.ui.activity.MainPreference$1 var4 = new com.alensw.ui.activity.MainPreference$1(this);
            var2.setOnPreferenceClickListener(var4);
         } catch (Exception var3) {
         }
      }
   }

   private void a__$appendSource(PreferenceScreen var1) {
      super.a(var1);
      SharedPreferences var5 = this.getPreferenceManager().getSharedPreferences();
      CharSequence var6 = var1.getTitle();
      if (var6.equals(this.getString(com.alensw.PicFolder.R.string.stable_keyword_interface))) {
         var1.findPreference("main_theme").setOnPreferenceClickListener(new com.alensw.ui.activity.v(this));
      } else if (var6.equals(this.getString(com.alensw.PicFolder.R.string.local_albums))) {
         var1.findPreference("included_folders").setIntent(PathListActivity.a(this, false));
         var1.findPreference("excluded_folders").setIntent(PathListActivity.a(this, true));
         SwitchPreference var24 = (SwitchPreference)var1.findPreference("crop_thumbnail");
         var24.setOnPreferenceClickListener(new com.alensw.ui.activity.aa(this, var24));
         var1.findPreference("default_sort").setOnPreferenceClickListener(new com.alensw.ui.activity.ab(this, var5));
      } else if (var6.equals(this.getString(com.alensw.PicFolder.R.string.view))) {
         this.a((ListPreference)var1.findPreference("screen_orientation"));
         if (VERSION.SDK_INT < 16) {
            this.a(var1, "auto_hide_navigate_bar");
         } else if (VERSION.SDK_INT < 19) {
            SwitchPreference var12 = (SwitchPreference)var1.findPreference("auto_hide_navigate_bar");
            var12.setOnPreferenceClickListener(new com.alensw.ui.activity.ac(this, var12));
         }
      } else if (var6.equals(this.getString(com.alensw.PicFolder.R.string.security))) {
         SwitchPreference var7 = (SwitchPreference)var1.findPreference("password_protect");
         String var25 = var5.getString("password", "");
         if (var25.length() > 0) {
            var7.setChecked(true);
            int var2;
            if (Character.isDigit(var25.charAt(0))) {
               var2 = com.alensw.PicFolder.R.string.password_mode_pin;
            } else {
               var2 = com.alensw.PicFolder.R.string.password_mode_pattern;
            }

            var7.setSummary(var2);
         } else {
            var7.setChecked(false);
         }

         var7.setOnPreferenceClickListener(new com.alensw.ui.activity.ae(this, var7, var5));
         com.alensw.ui.activity.ah var17 = new com.alensw.ui.activity.ah(this, var5);

         for (int var14 = 1; var14 < var1.getPreferenceCount(); var14++) {
            Preference var26 = var1.getPreference(var14);
            if (var26 instanceof SwitchPreference && "password_protect".equals(var26.getDependency())) {
               var26.setTitle("" + var26.getTitle());
               var26.setOnPreferenceClickListener(var17);
            }
         }
      } else if (var6.equals(this.getString(com.alensw.PicFolder.R.string.slideshow))) {
         ListPreference var30 = (ListPreference)var1.findPreference("slideshow_animation");
         int var3 = var30.getEntries().length;
         String[] var27 = new String[var3];

         for (int var15 = 0; var15 < var3; var15++) {
            var27[var15] = Integer.toString(var15);
         }

         var30.setEntryValues(var27);
         this.a(var30);
         var30 = (ListPreference)var1.findPreference("slideshow_interval");
         Integer var28 = com.alensw.b.l.b.a(var5.getString("slideshow_interval", "3"), 3);
         String var9 = this.getString(com.alensw.PicFolder.R.string.custom);
         String var10 = com.alensw.b.l.b.a(this.getResources(), "seconds", "s");
         int[] var13 = new int[]{3, 5, 10, 30, 60, 0};
         String[] var8 = new String[var13.length];

         for (int var16 = 0; var16 < var13.length - 1; var16++) {
            var8[var16] = Integer.toString(var13[var16]);
         }

         var8[var13.length - 1] = var9;
         var30.setEntries(var8);
         var30.setEntryValues(var8);
         var30.setSummary(var28.toString() + ' ' + var10);
         var30.setOnPreferenceChangeListener(new com.alensw.ui.activity.aj(this, var9, var5, var28, var30, var10));
      } else if (var6.equals(this.getString(com.alensw.PicFolder.R.string.cache))) {
         ((ConfirmPreference)var1.findPreference("clear_preview_cache")).a = new com.alensw.ui.activity.al(this);
         ((ConfirmPreference)var1.findPreference("clear_thumb_cache")).a = this.g;
      } else if (var6.equals(this.getString(com.alensw.PicFolder.R.string.android_11_warning))) {
         Locale var29 = this.getResources().getConfiguration().locale;
         boolean var4 = " ".equals(var29.getLanguage());
         String var32 = this.getPackageName();

         try {
            StringBuilder var18 = new StringBuilder();
            this.setTitle(
               var18.append(this.getString(com.alensw.PicFolder.R.string.app_name)).append(": ").append(this.getPackageManager().getPackageInfo(var32, 0).versionName).toString()
            );
         } catch (Throwable var11) {
         }

         Preference var33 = var1.findPreference("author");
         StringBuilder var36 = new StringBuilder();
         String var19;
         if (var4) {
            var19 = "快宜团队";
         } else {
            var19 = "wstxda@gmail.com";
         }

         String var37 = var36.append(var19).append("").toString();
         Intent var20 = new Intent("android.intent.action.VIEW", Uri.parse("mailto:" + var37));
         var20.putExtra("subject", this.getString(com.alensw.PicFolder.R.string.app_name));
         var33.setSummary(var37);
         this.a(var33, var20);
         Preference var21 = var1.findPreference("blog");
         if (var4) {
         }

         var21.setSummary("t.me/WSTprojects");
         var21.setIntent(new Intent("android.intent.action.VIEW", Uri.parse("http://" + "t.me/WSTprojects")));
         Preference var22 = var1.findPreference("translator");
         if (var22 != null) {
            if (var4) {
               this.a(var1, "translator");
            } else {
               CharSequence var34 = var22.getSummary();
               if (var34 != null && var34.length() > 0) {
                  String var35 = var34.toString();
                  if (var35.indexOf(64) >= 0) {
                     this.a(var22, new Intent("android.intent.action.VIEW", Uri.parse("mailto:" + var35)));
                  }
               } else {
                  this.a(var1, "translator");
               }
            }
         }

         var1.findPreference("details").setOnPreferenceClickListener(new com.alensw.ui.activity.an(this, var32));
         Preference var23 = var1.findPreference("donate");
         if (var23 != null) {
            if (Locale.SIMPLIFIED_CHINESE.equals(var29)) {
               var1.removePreference(var23);
            } else {
               var23.setIntent(new Intent("android.intent.action.VIEW", Uri.parse(this.getString(com.alensw.PicFolder.R.string.github_repository_url))));
            }
         }

         var1.findPreference("share").setIntent(Intent.createChooser(a((Context)this), this.getString(com.alensw.PicFolder.R.string.share)));
      }
   }

   private void a__$appendSource2(PreferenceScreen var1) {
      this.a__$appendSource(var1);
      this.a__$appendPatch(var1);
   }

   protected void a(int var1) {
      Toast.makeText(this, var1, 1).show();
   }

   protected void a(ListPreference var1) {
      var1.setSummary(var1.getEntry());
      var1.setOnPreferenceChangeListener(new com.alensw.ui.activity.y(this, var1));
   }

   protected void a(Preference var1, Intent var2) {
      var1.setOnPreferenceClickListener(new com.alensw.ui.activity.x(this, var2));
   }

   @Override
   protected void a(PreferenceScreen var1) {
      this.a__$appendSource2(var1);
      this.a__$appendPatch2(var1);
   }

   protected void a(boolean var1, SwitchPreference var2) {
      com.alensw.ui.a.x.a(this, var1, new com.alensw.ui.activity.w(this, var2));
   }

   public Boolean isBatteryOptimizationEnabled(Boolean var1) {
      String var3 = this.getPackageName();
      PowerManager var4 = (PowerManager)this.getSystemService("power");
      if (VERSION.SDK_INT >= 23) {
         boolean var2 = var4.isIgnoringBatteryOptimizations(var3);
         if (!var2 && var1) {
            Intent var5 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            StringBuilder var7 = new StringBuilder();
            var7.append("package:");
            var7.append(this.getPackageName());
            var5.setData(Uri.parse(var7.toString()));
            this.startActivity(var5);
            Intent var6 = new Intent();
            var6.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
            var6.setFlags(268435456);
            StringBuilder var8 = new StringBuilder();
            var8.append("package:");
            var8.append(var3);
            var6.setData(Uri.parse(var8.toString()));
            if (MIUIUtils.isMIUI()) {
               Toast.makeText(this, this.getString(com.alensw.PicFolder.R.string.disable_battery_optimization_miui_toast), 1).show();
            }

            this.startActivity(var6);
         }

         return var2;
      } else {
         return true;
      }
   }

   @Override
   protected void onActivityResult(int var1, int var2, Intent var3) {
      if (var1 == 1001 && var2 == -1) {
         this.recreate();
      } else {
         super.onActivityResult(var1, var2, var3);
      }
   }

   @Override
   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.addPreferencesFromResource(com.alensw.PicFolder.R.xml.main_pref);
      String var5 = this.getIntent().getStringExtra("screen");
      String var4 = var5;
      if (var5 == null) {
         var4 = var5;
         if (var1 != null) {
            var4 = var1.getString("screen");
         }
      }

      PreferenceScreen var6 = this.getPreferenceScreen();
      if (var6 != null && var4 != null) {
         int var3 = var6.getPreferenceCount();

         for (int var2 = 0; var2 < var3; var2++) {
            Preference var7 = var6.getPreference(var2);
            if (var7 instanceof PreferenceScreen && var4.equals(var7.getTitle())) {
               var7.getOnPreferenceClickListener().onPreferenceClick(var7);
               break;
            }
         }
      }
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem var1) {
      int var2 = 2;
      boolean var5 = true;
      SharedPreferences var6 = this.getPreferenceManager().getSharedPreferences();
      int var3 = var6.getInt("default_sort", 0);
      switch (var1.getItemId()) {
         case com.alensw.PicFolder.R.id.help:
            boolean var8 = " ".equals(this.getResources().getConfiguration().locale.getLanguage());
            StringBuilder var7 = new StringBuilder().append("");
            if (var8) {
            }

            com.alensw.ui.activity.a.a(this, "android.intent.action.VIEW", var7.append("").append("").toString());
            var2 = var3;
            break;
         case com.alensw.PicFolder.R.id.sort_default:
            var2 = 0;
            break;
         case com.alensw.PicFolder.R.id.sort_by_name:
            if (var3 == 2) {
               var2 = 4;
            }
            break;
         case com.alensw.PicFolder.R.id.sort_by_date:
            if (var3 == 1) {
               var2 = 3;
            } else {
               var2 = 1;
            }
            break;
         case com.alensw.PicFolder.R.id.sort_by_exif:
            if (var3 == 6) {
               var2 = 5;
            } else {
               var2 = 6;
            }
            break;
         case com.alensw.PicFolder.R.id.sort_by_size:
            if (var3 == 7) {
               var2 = 8;
            } else {
               var2 = 7;
            }
            break;
         default:
            return super.onOptionsItemSelected(var1);
      }

      boolean var4 = var5;
      if (var2 != var3) {
         com.alensw.a.q.a(this).a = var2;
         QuickApp.a(true);
         var6.edit().putInt("default_sort", var2).commit();
         var4 = var5;
      }

      return var4;
   }

   @Override
   protected void onResume() {
      super.onResume();
      if (this.getPreferenceScreen().getTitle().equals(this.getString(com.alensw.PicFolder.R.string.view))) {
         new com.alensw.ui.activity.MainPreference$2(this).start();
      }
   }

   @Override
   protected void onSaveInstanceState(Bundle var1) {
      PreferenceScreen var2 = this.getPreferenceScreen();
      if (var2 != null) {
         var1.putString("screen", var2.getTitle().toString());
      } else {
         var1.remove("screen");
      }
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
         PreferenceScreen var2 = this.getPreferenceScreen();
         if (var2 != null) {
            var1.putExtra("screen", var2.getTitle().toString());
         }

         this.startActivity(var1);
      }
   }
}
