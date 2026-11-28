package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.gallery.ScannedFolder;
import com.alensw.ui.dialog.BackgroundProgressDialog;
import com.alensw.utils.GalleryUtils;
import java.io.File;

class HideFolderDialog extends BackgroundProgressDialog {
   final ScannedFolder a;
   final GalleryMediaStore b;
   final String c;
   final Runnable e;
   final MediaActionPage f;

   HideFolderDialog(MediaActionPage var1, Activity var2, String var3, int var4, ScannedFolder var5, GalleryMediaStore var6, String var7, Runnable var8) {
      super(var2, var3, var4);
      this.f = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
      this.e = var8;
   }

   private void a(Object var1) {
      this.f.b.post(new ShowHideFolderError(this, var1));
   }

   @Override
   protected void a() {
      byte var1 = 1;

      try {
         File var2 = new File(this.a, "test");
         var2 = GalleryUtils.a(var2, "tmp");
         if (!GalleryUtils.b(var2)) {
            RuntimeException var6 = new RuntimeException("test write failed!");
            throw var6;
         }

         GalleryUtils.c(var2);
      } catch (Throwable var3) {
         var3.printStackTrace();
         return;
      }

      if (!this.b.d(this.c)) {
         this.a("Hide from the media store failed!\nPlease try again later.");
      } else if (this.b.c()) {
         this.a(com.alensw.PicFolder.R.string.can_not_operate_now);
      } else {
         ScannedFolder var5 = this.a;
         if (!GalleryUtils.a(this.a, true)) {
            var1 = 0;
         }

         var5.f = var1;
      }
   }

   @Override
   protected void b() {
      if (this.a.a(true)) {
         if (this.e != null) {
            this.e.run();
         }
      } else {
         this.f.a(this.a.getPath());
      }
   }
}
