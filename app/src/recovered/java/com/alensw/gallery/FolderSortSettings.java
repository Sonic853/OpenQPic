package com.alensw.gallery;

import android.content.Context;
import android.preference.PreferenceManager;
import com.alensw.bean.CommonFolder;
import com.alensw.io.IntegerPropertyFile;
import com.alensw.io.StoragePaths;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.Map.Entry;

public class FolderSortSettings {
   private static FolderSortSettings c;
   public int a;
   private final IntegerPropertyFile b = new LegacyFolderSortStore(this, 16);

   public FolderSortSettings() {
      this.a = 0;
   }

   public static synchronized FolderSortSettings a(Context context) {
      if (c == null) {
         c = new FolderSortSettings();
         c.b(context);
      }
      return c;
   }

   private void b(Context var1) {
      this.a = PreferenceManager.getDefaultSharedPreferences(var1).getInt("default_sort", 0);
      File var2 = GalleryUtils.b(var1, "folder_modes");
      this.b.a(var2);
      File var4 = StoragePaths.a(var1, "cfolder_modes");
      IntegerPropertyFile var5 = new IntegerPropertyFile(256);
      if (var4.exists() && var5.a(var4)) {
         for (Object value : var5.f()) {
            Entry var6 = (Entry)value;
            this.b.a((String)var6.getKey(), var6.getValue());
         }

         if (this.b.e()) {
            this.b.h();
         }

         GalleryUtils.c(var4);
      }
   }

   public int a(ScannedFolder var1) {
      Integer var3 = (Integer)this.b.b(var1.getPath());
      int var2;
      if (var3 != null) {
         var2 = var3;
      } else {
         var2 = var1.a(this.a);
      }

      return var2;
   }

   public int a(CommonFolder var1) {
      int var2 = 2;
      Integer var5 = (Integer)this.b.b(var1.i().toString());
      if (var5 != null) {
         var2 = var5;
      } else {
         int var4 = var1.c();
         byte var3;
         if ((var4 & 16) != 0) {
            var3 = 0;
         } else {
            var3 = 2;
         }

         if (this.a == 0) {
            if ((var4 & 32) != 0) {
               var2 = 1;
            }
         } else if (this.a == 5) {
            var2 = 3;
         } else if (this.a == 6) {
            var2 = 1;
         } else {
            var2 = this.a;
         }

         var2 = CommonFolder.a(var3, var2);
      }

      return var2;
   }

   public void b(ScannedFolder var1) {
      String var2 = var1.getPath();
      this.b.a(var2, var1.d);
      this.b.h();
   }

   public void b(CommonFolder var1) {
      String var2 = var1.i().toString();
      this.b.a(var2, var1.l);
      this.b.h();
   }
}
