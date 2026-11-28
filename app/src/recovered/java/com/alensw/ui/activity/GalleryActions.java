package com.alensw.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.MenuItem;
import android.view.View;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.media.MediaFileTypes;
import com.alensw.svg.SvgDrawable;
import com.alensw.svg.SvgPicture;
import com.alensw.ui.dialog.GalleryDialogs;
import com.alensw.ui.drawable.AnimatedTrashIcon;
import com.alensw.ui.menu.GalleryMenu;
import com.alensw.ui.menu.GalleryMenuItem;
import com.alensw.utils.GalleryUtils;
import com.alensw.video.VideoProxyService;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class GalleryActions {
   @TargetApi(13)
   public static int a(Context context, Intent intent, int i, View view, IntentTargetCallback srcType0634) {
      return GalleryShareMenu.show(context, intent, view, srcType0634);
   }

   public static Intent a(Intent var0, ResolveInfo var1) {
      var0 = new Intent(var0);
      var0.setComponent(new ComponentName(var1.activityInfo.applicationInfo.packageName, var1.activityInfo.name));
      var0.addFlags(50331648);
      return var0;
   }

   public static List<ResolveInfo> a(Context context, Intent intent, int i) {
        try {
            return context.getPackageManager().queryIntentActivities(intent, i);
        } catch (RuntimeException e) {
            return new ArrayList();
        }
    }

   public static void a(Context var0, double var1, double var3, View var5) {
      String var6 = GalleryUtils.a("%.7f,%.7f", var1, var3);
      String var7 = "geo:" + var6 + "?z=16";
      String var8 = "http://maps.google.com/maps?q=loc:" + var6;
      if (a(var0, new Intent("android.intent.action.VIEW", Uri.parse(var7)), com.alensw.PicFolder.R.string.show_in_map, var5, new LaunchMapTarget(var8, var7, var6, var0)) == 0) {
         a(var0, "android.intent.action.VIEW", var8);
      }
   }

   public static void a(Context var0, int var1, View var2, Runnable var3) {
      AuthorizeDelete var5 = new AuthorizeDelete(var0, var3);
      String var4 = var0.getString(com.alensw.PicFolder.R.string.delete);
      String var7 = var4;
      if (var1 > 1) {
         var7 = var4 + " (" + var1 + ")";
      }

      if (var2 != null) {
         ConfirmDeleteMenuItem var8 = new ConfirmDeleteMenuItem(var5);
         Resources var9 = var0.getResources();
         AnimatedTrashIcon var10 = new AnimatedTrashIcon(SvgPicture.a(var9, com.alensw.PicFolder.R.raw.ic_menu_delete, var9.getColor(com.alensw.PicFolder.R.color.material_warning), var9.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_material_icon_size)), false);
         var2.postDelayed(new AnimateDeleteIcon(var10), 150L);
         GalleryMenu var6 = new GalleryMenu(var0);
         ((GalleryMenuItem)var6.add(0, com.alensw.PicFolder.R.id.delete, 0, var7)).a(32).setIcon(var10).setOnMenuItemClickListener(var8);
         var6.a(true);
         var6.a(var2);
      } else {
         GalleryDialogs.a(var0, 0, var7 + "?", var5, null);
      }
   }

   public static void a(Context var0, Intent var1, ResolveInfo var2) {
      a(var0, a(var1, var2));
   }

   public static void a(Context var0, Uri var1) {
      Intent var2 = new Intent();
      var2.setComponent(new ComponentName("com.google.android.gms", "com.google.android.gms.panorama.PanoramaViewActivity"));
      var2.setData(var1);
      a(var0, var2);
   }

   public static void a(Context var0, Uri var1, String var2) {
      VideoProxyService.view(var0, var1, var2);
   }

   public static void a(Context var0, View var1) {
      if (QuickApp.j == 3 && VERSION.SDK_INT >= 17) {
         Intent var2 = new Intent("android.intent.action.MAIN");
         var2.setComponent(new ComponentName("com.android.gallery3d", "com.android.camera.CameraLauncher"));
         if (a(var0, var2)) {
            return;
         }
      }

      if (!a(var0, new Intent("android.media.action.STILL_IMAGE_CAMERA"))) {
         a(var0, new Intent("android.media.action.IMAGE_CAPTURE"), com.alensw.PicFolder.R.string.camera, var1, new LaunchCameraTarget(var0));
      }
   }

   public static void a(Context var0, View var1, int[] var2, int var3, boolean var4) {
      SortMenu var7 = new SortMenu(var0, var3, var4);
      GalleryMenu.a(var0, com.alensw.PicFolder.R.menu.sort, var7);
      Arrays.sort(var2);

      for (int var5 = var7.size() - 1; var5 >= 0; var5--) {
         MenuItem var8 = var7.getItem(var5);
         int var6 = var8.getItemId();
         if (Arrays.binarySearch(var2, var6) < 0) {
            var7.b(var5);
         } else if (var6 == var3) {
            var8.setCheckable(true).setChecked(true);
         }
      }

      var7.a(var1);
   }

   public static void a(Context var0, String var1) {
      String var2 = null;
      Integer var3 = QuickApp.s.a(var1, 'V');
      if (var3 != null) {
         var2 = "_id=" + var3;
         var2 = QuickApp.s.b(GalleryMediaStore.b, new String[]{"mime_type"}, var2);
      }

      String var5 = var2;
      if (var2 == null) {
         var5 = MediaFileTypes.a(var1, 'V');
      }

      a(var0, Uri.fromFile(new File(var1)), var5);
   }

   public static boolean a(Activity var0, Intent var1, int var2) {
      try {
         var0.startActivityForResult(var1, var2);
      } catch (Throwable var4) {
         return false;
      }

      return true;
   }

   public static boolean a(Context var0, Intent var1) {
      try {
         var0.startActivity(var1);
      } catch (Throwable var3) {
         return false;
      }

      return true;
   }

   public static boolean a(Context var0, String var1, String var2) {
      return a(var0, new Intent(var1, Uri.parse(var2)));
   }

   public static void b(Context var0, View var1) {
      GalleryMenu var2 = new GalleryMenu(var0);
      GalleryMenu.a(var0, com.alensw.PicFolder.R.menu.flip, var2);
      SvgDrawable var3 = SvgPicture.a(var0.getResources(), com.alensw.PicFolder.R.raw.ic_menu_flip, var2.b);
      var2.findItem(com.alensw.PicFolder.R.id.flip_horz).setIcon(var3);
      var2.findItem(com.alensw.PicFolder.R.id.flip_vert).setIcon(new VerticalFlipIcon(var3.a(), -1, -1));
      var2.a(true);
      var2.a(var1);
   }

   public static boolean b(Context var0, Uri var1, String var2) {
      Intent var4 = new Intent("android.intent.action.VIEW");
      var4.setDataAndType(var1, var2);
      Iterator var5 = a(var0, var4, 0).iterator();

      boolean var3;
      while (true) {
         if (var5.hasNext()) {
            ResolveInfo var7 = (ResolveInfo)var5.next();
            String var6 = var7.activityInfo.applicationInfo.packageName;
            if (!var6.contains(".android.gallery") && !var6.startsWith("com.cooliris.media") && !var6.startsWith("com.htc.album")) {
               continue;
            }

            a(var0, var4, var7);
            var3 = true;
            break;
         }

         var3 = false;
         break;
      }

      return var3;
   }

   public static void c(Context var0, View var1) {
      GalleryMenu var2 = new GalleryMenu(var0);
      GalleryMenu.a(var0, com.alensw.PicFolder.R.menu.rotate, var2);
      SvgDrawable var3 = SvgPicture.a(var0.getResources(), com.alensw.PicFolder.R.raw.ic_menu_rotate, var2.b);
      var2.findItem(com.alensw.PicFolder.R.id.rotate_right).setIcon(var3);
      var2.findItem(com.alensw.PicFolder.R.id.rotate_left).setIcon(new RotateLeftIcon(var3.a(), -1, -1));
      var2.findItem(com.alensw.PicFolder.R.id.rotate_180).setIcon(new RotateHalfTurnIcon(var3.a(), -1, -1));
      var2.a(true);
      var2.a(var1);
   }
}
