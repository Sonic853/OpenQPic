package com.alensw.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import androidx.documentfile.provider.DocumentFile;
import com.alensw.PicFolder.CropActivity;
import com.alensw.PicFolder.FileProvider;
import com.alensw.PicFolder.GalleryActivity;
import com.alensw.PicFolder.PlayerActivity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.jni.JniUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class p extends com.alensw.ui.activity.ao implements Callback {
   public static Uri D;
   public static Uri E;
   protected Handler A;
   protected int B;
   protected com.alensw.ui.view.cd C;

   public static int b(Intent var0) {
      byte var1 = 1;
      String var2 = var0.getAction();
      if ("android.intent.action.PICK".equals(var2) || "android.intent.action.GET_CONTENT".equals(var2)) {
         String var3 = var0.getType();
         if ("video/*".equals(var3) || "vnd.android.cursor.dir/video".equals(var3)) {
            var1 = 2;
         } else if ("vnd.android.document/directory".equals(var3)) {
            var1 = 3;
         } else if ("*/*".equals(var3)) {
            var1 = 4;
         }
      } else if (!"android.intent.action.SET_WALLPAPER".equals(var2)) {
         if ("com.alensw.PicFolder.action.PICK_MULTIPLE".equals(var2)) {
            var1 = 5;
         } else {
            var1 = 0;
         }
      }

      return var1;
   }

   private void onActivityResult__$prependPatch(int var1, int var2, Intent var3) {
      if (var2 == -1 && var1 == 205452) {
         DocumentFile.fromTreeUri(this.getApplicationContext(), var3.getData()).createFile("image/jpeg", "!qpicfake.jpg");
      }
   }

   private void onActivityResult__$prependSource(int var1, int var2, Intent var3) {
      super.onActivityResult(var1, var2, var3);
      if (var1 == 4 && var2 == -1) {
         Uri var4;
         String var5;
         try {
            var4 = var3.getData();
            var5 = DocumentsContract.getTreeDocumentId(var4);
            var2 = var5.lastIndexOf(58);
         } catch (Throwable var8) {
            com.alensw.b.l.b.a(this, var8);
            return;
         }

         var1 = var2;
         if (var2 == -1) {
            try {
               var1 = var5.length();
            } catch (Throwable var7) {
               com.alensw.b.l.b.a(this, var7);
               return;
            }
         }

         try {
            com.alensw.a.c.a = var5.substring(0, var1);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("external_sd_uuid", com.alensw.a.c.a).commit();
            var1 = var3.getFlags();
            this.getContentResolver().takePersistableUriPermission(var4, var1 & 3);
         } catch (Throwable var6) {
            com.alensw.b.l.b.a(this, var6);
         }
      }
   }

   public Drawable a(int i, long j, long j2, String str) {
       try (InputStream input = getResources().openRawResource(i)) {
           ByteArrayOutputStream buffer = new ByteArrayOutputStream(4096);
           byte[] bytes = new byte[4096];
           int count;
           while ((count = input.read(bytes)) > 0) buffer.write(bytes, 0, count);
           String svg = buffer.toString();
           try {
               Resources docs = createPackageContext("com.android.documentsui", 0).getResources();
               String title = docs.getString(docs.getIdentifier("title_open", "string", "com.android.documentsui"));
               if (title != null) svg = svg.replace("Open from", title);
               String recent = docs.getString(docs.getIdentifier("root_recent", "string", "com.android.documentsui"));
               if (recent != null) svg = svg.replace("Recent", recent);
               String select = docs.getString(docs.getIdentifier("menu_select", "string", "com.android.documentsui"));
               if (select != null) svg = svg.replace("SELECT 'SD card'", TextUtils.expandTemplate(select, str == null ? "SD card" : str));
               if (str != null) svg = svg.replace("SD card", str);
               if (j > 0) {
                   String size = docs.getString(docs.getIdentifier("root_available_bytes", "string", "com.android.documentsui"), Formatter.formatFileSize(this, j));
                   if (size != null) svg = svg.replace("00GB", size);
               }
               if (j2 > 0) {
                   String size = docs.getString(docs.getIdentifier("root_available_bytes", "string", "com.android.documentsui"), Formatter.formatFileSize(this, j2));
                   if (size != null) svg = svg.replace("??GB", size);
               }
           } catch (Throwable missingLabel) { missingLabel.printStackTrace(); }
           return com.b.a.d.a(svg).a();
       } catch (Throwable missingResource) { return com.b.a.b.a(getResources(), i); }
   }

   public void a(int var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if (var1 == 4) {
         var2 = var3;
         if (VERSION.SDK_INT >= 11) {
            var2 = var3;
            if (System.getInt(this.getContentResolver(), "accelerometer_rotation", 0) == 0) {
               var2 = true;
            }
         }
      }

      if (var2 && this.C == null) {
         this.C = new com.alensw.ui.activity.u(this, this);
         this.C.a();
      } else if (!var2 && this.C != null) {
         this.C.b();
         this.C = null;
      }

      this.setRequestedOrientation(var1);
   }

   public boolean a(Intent var1) {
      String var5 = var1.getAction();
      com.alensw.a.ba var6 = QuickApp.s;
      if ("android.intent.action.SEND".equals(var5)) {
         Uri var11 = (Uri)var1.getParcelableExtra("android.intent.extra.STREAM");
         if (var11 != null && "file".equals(var11.getScheme())) {
            String var7 = var11.getPath();
            char var2;
            if (var1.getType().startsWith("video/")) {
               var2 = 'V';
            } else {
               var2 = 'I';
            }

            Uri var14 = var6.b(var7, var2);
            Uri var12 = var14;
            if (var14 == null) {
               var12 = FileProvider.a(var7);
            }

            var1.putExtra("android.intent.extra.STREAM", var12);
         }
      } else if ("android.intent.action.SEND_MULTIPLE".equals(var5) && var1.getClipData() == null && var1.hasExtra("multiple_files")) {
         ArrayList var15 = var1.getParcelableArrayListExtra("android.intent.extra.STREAM");
         ArrayList var9 = var1.getStringArrayListExtra("multiple_files");
         ArrayList var8 = var1.getIntegerArrayListExtra("multiple_types");
         int var4 = var15.size();

         for (int var3 = 0; var3 < var4; var3++) {
            String var10 = (String)var9.get(var3);
            Uri var13 = var6.b(var10, (char)((Integer)var8.get(var3)).intValue());
            if (var13 == null) {
               var13 = FileProvider.a(var10);
            }

            var15.set(var3, var13);
         }

         var1.putParcelableArrayListExtra("android.intent.extra.STREAM", var15);
      }

      return false;
   }

   public boolean a(String str) {
        if (str == null || str.startsWith(com.alensw.a.e.b + File.separatorChar)) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Uri uriA = com.alensw.a.c.a(str);
            if (uriA != null && checkCallingOrSelfUriPermission(uriA, 2) == 0) {
                return true;
            }
            b(str);
            return true;
        }
        if (android.os.Build.VERSION.SDK_INT < 19) {
            return false;
        }
        AlertDialog alertDialogA = com.alensw.ui.a.f.a((android.content.Context) this, 17039380, (Object) getString(com.alensw.PicFolder.R.string.no_write_sd_permission));
        alertDialogA.setButton(-1, getString(17039370), new com.alensw.ui.activity.r(this));
        alertDialogA.setButton(-2, getString(com.alensw.PicFolder.R.string.details), new com.alensw.ui.activity.s(this));
        com.alensw.ui.a.f.a(alertDialogA);
        return true;
    }

   public void b(String var1) {
      if (VERSION.SDK_INT >= 21) {
         com.alensw.ui.a.u var2 = com.alensw.ui.a.u.a(this, 17039380, com.alensw.PicFolder.R.layout.sd_permission);
         if (var2 != null) {
            ((ImageView)var2.findViewById(com.alensw.PicFolder.R.id.image))
               .setImageDrawable(this.a(com.alensw.PicFolder.R.raw.sd_steps, JniUtils.fuGetAvailBytes(com.alensw.a.e.b), JniUtils.fuGetAvailBytes(var1), QuickApp.p.f(var1)));
            var2.setButton(-1, this.getString(17039370), new com.alensw.ui.activity.t(this));
            com.alensw.ui.a.f.a(var2);
         }
      }
   }

   public int g() {
      return this.U & 16777215 | -805306368;
   }

   @Override
   public boolean handleMessage(Message var1) {
      return false;
   }

   @Override
   public void onActivityResult(int var1, int var2, Intent var3) {
      this.onActivityResult__$prependPatch(var1, var2, var3);
      this.onActivityResult__$prependSource(var1, var2, var3);
   }

   @TargetApi(14)
   @Override
   public void onCreate(Bundle var1) {
      byte var2 = 0;
      this.A = new Handler(this);
      this.M = true;
      SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(this);
      boolean var3;
      if (!var4.getBoolean("split_bar", this.N) && !(this instanceof CropActivity)) {
         var3 = false;
      } else {
         var3 = true;
      }

      this.N = var3;
      if (var4.getBoolean("hide_status_bar", false)) {
         this.getWindow().addFlags(1024);
      }

      if (VERSION.SDK_INT >= 14) {
         Window var5 = this.getWindow();
         if (this.N) {
            var2 = 1;
         }

         var5.setUiOptions(var2, 1);
      }

      super.onCreate(var1);
      this.B = this.getResources().getConfiguration().orientation;
   }

   @Override
   public boolean onMenuOpened(int var1, Menu var2) {
      boolean var3;
      if (VERSION.SDK_INT < 11 && var1 == 0 && this.K != null) {
         this.K.a(this.I);
         var3 = false;
      } else {
         var3 = super.onMenuOpened(var1, var2);
      }

      return var3;
   }

   @Override
   public void onPause() {
      super.onPause();
      if (this.C != null) {
         this.C.b();
      }
   }

   @Override
   public void onResume() {
      if (this.C != null) {
         this.C.a();
      }

      super.onResume();
   }

   @Override
   public void onStart() {
        int i;
        boolean z;
        boolean z2 = true;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int i2 = this instanceof PlayerActivity ? -1 : defaultSharedPreferences.getInt("main_theme", 0);
        if (this.T != i2) {
            this.T = i2;
            z = true;
        } else if ((this.T == -3 || this.T == -2) && this.V != (i = defaultSharedPreferences.getInt("actionbar_color", this.V))) {
            this.V = i;
            z = true;
        } else {
            z = false;
        }
        this.R = defaultSharedPreferences.getBoolean("auto_black_bkgnd", false);
        boolean z3 = defaultSharedPreferences.getBoolean("split_bar", this.N) || (this instanceof CropActivity);
        if (this.N != z3) {
            this.N = z3;
        } else {
            z2 = z;
        }
        super.onStart();
        if (z2) {
            this.A.post(new com.alensw.ui.activity.q(this));
        }
    }

   @Override
   public void onStop() {
      super.onStop();
   }

   @Override
   public void setContentView(View var1) {
      super.setContentView(var1);
      if (this.M) {
         if (VERSION.SDK_INT == 19) {
            this.getWindow().addFlags(201326592);
         } else if (VERSION.SDK_INT >= 21) {
            this.getWindow().addFlags(Integer.MIN_VALUE);
         }
      }

      if (!(this instanceof GalleryActivity)) {
         this.g(true);
      }

      this.a((ViewGroup)this.I.findViewById(com.alensw.PicFolder.R.id.bottom_bar));
   }
}
