package com.alensw.PicFolder;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.alensw.bean.CommonFile;
import com.alensw.bean.CommonFolder;
import com.alensw.bean.CommonRoot;
import com.alensw.bean.DocumentFile;
import com.alensw.bean.DocumentFolder;
import com.alensw.bean.UriFile;
import com.alensw.transfer.TransferActivity;
import com.alensw.ui.activity.MainPreference;
import com.alensw.updater.GitHubUpdates;
import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends com.alensw.ui.c.cx {
   private DrawerLayout e;
   private ViewGroup f;
   private ListView g;
   private android.support.a.a.a h;
   private com.alensw.ui.view.bc i;
   private int j;
   private boolean waitingForStorage;

   public static Intent a(String var0, Uri var1, Intent var2) {
      Intent var3 = new Intent(var0, var1);
      if (var2 != null) {
         var3.putExtras(var2);
      }

      return var3;
   }

   static com.alensw.ui.view.bc a(GalleryActivity var0) {
      return var0.i;
   }

   static DrawerLayout b(GalleryActivity var0) {
      return var0.e;
   }

   static android.support.a.a.a c(GalleryActivity var0) {
      return var0.h;
   }

   private void c(ViewGroup var1) {
      this.e = (DrawerLayout)var1.findViewById(com.alensw.PicFolder.R.id.drawer_layout);
      this.f = (ViewGroup)this.e.findViewById(com.alensw.PicFolder.R.id.drawer_panel);
      this.g = (ListView)this.f.findViewById(com.alensw.PicFolder.R.id.drawer_list);
      this.h = new com.alensw.PicFolder.ag(this, this, this.e, this.X, com.alensw.PicFolder.R.string.app_name, com.alensw.PicFolder.R.string.app_name);
      this.e.setDrawerListener(this.h);
      if (this.T == 0) {
         this.e.setScrimColor(this.g());
      }

      this.f.setOnClickListener(new com.alensw.PicFolder.ah(this));
      this.i = new com.alensw.ui.view.bc(this, this.g);
      this.g.setAdapter((ListAdapter)this.i);
      ListView var3 = this.g;
      boolean var2;
      if (this.T != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.setClipToPadding(var2);
      if (this.T != 0) {
         this.g.setBackgroundColor(this.U);
      }

      this.g.setOnItemClickListener(new com.alensw.PicFolder.ai(this));
   }

   static ListView d(GalleryActivity var0) {
      return var0.g;
   }

   static void e(GalleryActivity var0) {
      var0.e();
   }

   @TargetApi(8)
   private void onCreate__$appendPatch(Bundle var1) {
      StrictMode.setVmPolicy(new Builder().build());
      Log.i("create data folder", "creating");
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getExternalFilesDir(null));
      var2.append("");
      File var3 = new File(var2.toString());
      if (!var3.exists()) {
         var3.mkdirs();
      }

      Log.i("create data folder", "success");
   }

   @TargetApi(8)
   private void onCreate__$appendPatch2(Bundle var1) {
      Log.i("sqlitedatabase", "start");
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getExternalFilesDir(null));
      var2.append("");
      File var3 = new File(var2.toString());
      if (!var3.exists()) {
         var3.mkdirs();
      }

      Log.i("sqlitedatabase", "finished");
   }

   private void onCreate__$appendPatch3(Bundle var1) {
      GitHubUpdates.onStartup(this);
   }

   private void onCreate__$appendPatch4(Bundle var1) {
      if (!this.isStoragePermissionGranted()) {
         this.waitingForStorage = true;
         GalleryStorage.requestAccess(this);
      }
   }

   private void onCreate__$appendPatch5(Bundle var1) {
   }

   private void onCreate__$appendPatch6(Bundle var1) {
      Log.d("DXP", "It works!");
   }

   @TargetApi(19)
   private void onCreate__$appendSource(Bundle var1) {
      super.onCreate(var1);
      Intent var4 = this.getIntent();
      this.j = b(var4);
      if (var1 != null) {
         this.i.c = var1.getInt("current_root");
      }

      if (this.d() <= 0) {
         Uri var6 = var4.getData();
         boolean var2;
         if (var6 == null) {
            var2 = true;
         } else {
            var2 = false;
         }

         SharedPreferences var3 = PreferenceManager.getDefaultSharedPreferences(this);
         if (var2 && var3.getBoolean("open_last_folder", false)) {
            String var5 = var3.getString("last_folder", null);
            if (var5 != null) {
               File var15 = new File(var5);
               if (var15.exists()) {
                  var6 = Uri.fromFile(var15);
               }
            }
         }

         com.alensw.PicFolder.ab var16 = new com.alensw.PicFolder.ab(this);
         if (this.j != 0) {
            this.a(com.alensw.ui.c.bz.class, var4, null);
            Uri var12 = var6;
            if (var6 == null) {
               if (this.j == 1) {
                  var12 = com.alensw.ui.activity.p.D;
               } else {
                  var12 = var6;
                  if (this.j == 2) {
                     var12 = com.alensw.ui.activity.p.E;
                  }
               }
            }

            if (var12 != null && "file".equals(var12.getScheme())) {
               this.a(com.alensw.ui.c.bf.class, a(var4.getAction(), var12, var4), null);
            }
         } else if (var6 != null) {
            String var13 = QuickApp.s.a(var6);
            if (var13 != null) {
               File var7 = new File(var13);
               com.alensw.PicFolder.ac var8 = new com.alensw.PicFolder.ac(this, var2, var4, var7.isDirectory(), var7);
               if (com.alensw.b.l.b.h(var13)) {
                  com.alensw.ui.a.x.a(this, "protect_hidden", var8, var16);
               } else {
                  var8.run();
               }
            } else if (com.alensw.b.j.a.d(var6)) {
               CommonFile var14 = UriFile.a(var6);
               if (var14 instanceof DocumentFile && "vnd.android.document/directory".equals(var14.b())) {
                  new com.alensw.PicFolder.ad(this, com.alensw.ui.c.z.a(new DocumentFolder(var14, var6.getAuthority(), ((DocumentFile)var14).k()), false, null)).run();
               }
            } else {
               this.a(com.alensw.ui.c.dp.class, var4, null);
            }
         } else if (var4.hasExtra("QuickPic.folder")) {
            CommonFolder var9 = (CommonFolder)var4.getParcelableExtra("QuickPic.folder");
            if (var9 != null) {
               this.a(com.alensw.ui.c.z.class, com.alensw.ui.c.z.a(var9, true, null), null);
            }
         } else {
            String var10 = var3.getString("last_drive", "");
            CommonRoot var11 = this.i.a(var10);
            if ("folders".equals(var11.n)) {
               this.a(com.alensw.ui.c.bz.class, new Intent(), null);
            } else {
               this.a(com.alensw.ui.c.z.class, com.alensw.ui.c.z.a(var11.k(), true, var11.n), null);
            }
         }
      }
   }

   @TargetApi(8)
   private void onCreate__$appendSource2(Bundle var1) {
      this.onCreate__$appendSource(var1);
      this.onCreate__$appendPatch(var1);
   }

   @TargetApi(8)
   private void onCreate__$appendSource3(Bundle var1) {
      this.onCreate__$appendSource2(var1);
      this.onCreate__$appendPatch2(var1);
   }

   private void onCreate__$appendSource4(Bundle var1) {
      this.onCreate__$appendSource3(var1);
      this.onCreate__$appendPatch3(var1);
   }

   private void onCreate__$appendSource5(Bundle var1) {
      this.onCreate__$appendSource4(var1);
      this.onCreate__$appendPatch4(var1);
   }

   private void onCreate__$appendSource6(Bundle var1) {
      this.onCreate__$appendSource5(var1);
      this.onCreate__$appendPatch5(var1);
   }

   @Override
   public void a(Rect var1) {
      super.a(var1);
      if (this.f != null) {
         MarginLayoutParams var2 = (MarginLayoutParams)this.f.getLayoutParams();
         var2.topMargin = var1.top;
         var2.bottomMargin = var1.bottom;
      }
   }

   @Override
   public void a(MenuItem var1, int var2) {
      if (QuickApp.e) {
         var2 = 0;
      }

      super.a(var1, var2);
   }

   @Override
   protected void a(com.alensw.ui.c.cu var1) {
      boolean var4 = false;
      super.a(var1);
      this.g(true);
      this.h(true);
      if (this.j == 0 && this.h != null) {
         boolean var3 = this.h.b();
         boolean var2;
         if (this.d() <= 1 && !(var1 instanceof com.alensw.ui.c.dp)) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.h.a(var2);
         if (var2 != var3 && this.a == 3) {
            View var5 = this.I;
            if (!var2) {
               var3 = true;
            } else {
               var3 = false;
            }

            com.alensw.PicFolder.af var6 = new com.alensw.PicFolder.af(this, var5, this.j(var3), var2);
            var3 = var4;
            if (!var2) {
               var3 = true;
            }

            var6.a(Math.max(this.b(var3), 300L), true);
         }
      }
   }

   @Override
   public void a(boolean var1) {
      if (this.j == 0) {
         DrawerLayout var3 = this.e;
         byte var2;
         if (var1) {
            var2 = 0;
         } else {
            var2 = 1;
         }

         var3.a(var2, 3);
      }

      super.a(var1);
   }

   @Override
   protected ViewGroup a_() {
      ViewGroup var1 = (ViewGroup)View.inflate(this, com.alensw.PicFolder.R.layout.main, null);
      this.setContentView(var1);
      this.c(var1);
      return var1;
   }

   public Handler b() {
      return this.A;
   }

   public boolean isStoragePermissionGranted() {
      return GalleryStorage.hasAccess(this);
   }

   @Override
   public void onBackPressed() {
      if (this.e.e(3)) {
         this.e.d(3);
      } else {
         super.onBackPressed();
      }
   }

   @Override
   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      if (this.h != null) {
         this.h.a(var1);
      }
   }

   @Override
   public void onCreate(Bundle var1) {
      this.onCreate__$appendSource6(var1);
      this.onCreate__$appendPatch6(var1);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var4 = true;
      if (!this.h.a(var1)) {
         if (this.e.e(3)) {
            this.e.d(3);
         }

         switch (var1.getItemId()) {
            case com.alensw.PicFolder.R.id.transfer:
               Intent var6 = new Intent();
               Uri[] var5 = this.f();
               if (var5 != null && var5.length > 0) {
                  ArrayList var7 = new ArrayList();
                  int var3 = var5.length;

                  for (int var2 = 0; var2 < var3; var2++) {
                     var7.add(var5[var2]);
                  }

                  var6.setAction("android.intent.action.SEND_MULTIPLE");
                  var6.putParcelableArrayListExtra("android.intent.extra.STREAM", var7);
               }

               var6.setFlags(67108864);
               var6.setComponent(new ComponentName(this, TransferActivity.class));
               com.alensw.ui.activity.a.a(this, var6);
               break;
            case com.alensw.PicFolder.R.id.settings:
               com.alensw.ui.activity.a.a(this, new Intent(this, MainPreference.class));
               break;
            default:
               var4 = super.onOptionsItemSelected(var1);
         }
      }

      return var4;
   }

   @Override
   public void onPostCreate(Bundle var1) {
      boolean var2 = true;
      super.onPostCreate(var1);
      if (this.j == 0) {
         this.h.a();
         if (this.d() > 1 || this.c() instanceof com.alensw.ui.c.dp) {
            var2 = false;
         }

         this.h.a(var2);
         this.e.a(0, 3);
         SharedPreferences var3 = PreferenceManager.getDefaultSharedPreferences(this);
         if (var3.getInt("drawer_shown", 0) != QuickApp.k) {
            com.alensw.b.l.b.a(var3.edit().putInt("drawer_shown", QuickApp.k));
            this.e.postDelayed(new com.alensw.PicFolder.ae(this), 300L);
         }
      } else {
         this.h.a(false);
         this.e.a(1, 3);
      }
   }

   @Override
   public void onResume() {
      if (this.waitingForStorage && this.isStoragePermissionGranted()) {
         this.waitingForStorage = false;
         GalleryStorage.accessGranted(this);
      }

      super.onResume();
   }

   @Override
   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      if (this.i != null) {
         var1.putInt("current_root", this.i.c);
      }
   }

   @Override
   public void onStop() {
      super.onStop();
      QuickApp.y = "";
   }
}
