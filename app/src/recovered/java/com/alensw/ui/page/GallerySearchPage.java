package com.alensw.ui.page;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import com.alensw.bean.CommonFile;
import com.alensw.bean.CommonFolder;
import com.alensw.bean.LocalFile;
import com.alensw.bean.LocalFolder;
import com.alensw.bean.MediaStoreFolder;
import com.alensw.concurrent.CancellableFutureTask;
import com.alensw.gallery.ScannedFolder;
import com.alensw.gallery.ScannedMediaFile;
import com.alensw.media.MediaFileTypes;
import com.alensw.ui.dialog.BackgroundProgressDialog;
import java.io.File;

public class GallerySearchPage extends MediaFolderPage {
   private String B;
   private CancellableFutureTask L;
   private ContentResolver M;

   public static Intent a(Context var0, String var1, String var2) {
      Object var4;
      if ("moments".equals(var2)) {
         var4 = new MediaStoreFolder(0, var1, -1879048142, 0L, 0L, null, true, true);
      } else {
         var4 = new LocalFolder("/search", var1, 268435506, 0L, 0L);
      }

      Intent var3 = new Intent();
      var3.setData(((CommonFolder)var4).i());
      var3.putExtra("QuickPic.folder", (Parcelable)var4);
      var3.putExtra("QuickPic.keyword", var1);
      if (var2 != null) {
         var3.putExtra("QuickPic.root_id", var2);
      }

      return var3;
   }

   static String a(GallerySearchPage var0) {
      return var0.B;
   }

   private void a(ScannedMediaFile var1) {
      if (this.H != 0) {
         String var2 = MediaFileTypes.a(var1.b, var1.c);
         LocalFile var3 = new LocalFile(var1.c, var1.a, var1.b, var2, 5, 0L, var1.d, var1.d);
         this.b.sendMessage(this.b.obtainMessage(3010, var3));
      }
   }

   static void a(GallerySearchPage var0, ScannedMediaFile var1) {
      var0.a(var1);
   }

   static void a(GallerySearchPage var0, ScannedFolder var1) {
      var0.b(var1);
   }

   static void a(GallerySearchPage var0, CommonFile var1) {
      var0.b(var1);
   }

   private void b(ScannedFolder var1) {
      ScannedMediaFile var2 = var1.e(0);
      if (this.H != 0 && var2 != null) {
         LocalFolder var3 = new LocalFolder(var2.a(), var1.getName(), 268435461, 0L, 0L);
         this.b.sendMessage(this.b.obtainMessage(3010, var3));
      }
   }

   private void b(CommonFile var1) {
      if (this.H != 0) {
         this.b.sendMessage(this.b.obtainMessage(3010, var1));
      }
   }

   @Override
   public void a(Bundle var1) {
      super.a(var1);
      this.B = this.E.getStringExtra("QuickPic.root_id");
      if (this.B == null || this.B.length() == 0) {
         this.B = "folders";
      }

      this.M = this.C.getApplication().getContentResolver();
   }

   @Override
   public void a(CommonFolder var1) {
      if (var1 instanceof LocalFolder) {
         String var3 = var1.j();
         Intent var2 = new Intent();
         var2.setData(Uri.fromFile(new File(var3)));
         this.C.a(LocalFolderPage.class, var2, null);
      } else {
         super.a(var1);
      }
   }

   @Override
   public void g() {
      if (this.L != null) {
         this.L.cancel(false);
         this.L = null;
      }

      super.g();
   }

   @Override
   public boolean handleMessage(Message var1) {
      boolean var3;
      switch (var1.what) {
         case 3010:
            if (this.H != 0) {
               CommonFile var4 = (CommonFile)var1.obj;
               int var2 = this.y.b(var4);
               if (var2 < 0) {
                  this.y.a(-var2 - 1, var4);
               }

               this.f.requestLayout();
            }

            var3 = true;
            break;
         default:
            var3 = super.handleMessage(var1);
      }

      return var3;
   }

   @Override
   public void n() {
      if (this.L == null) {
         this.c(true);
         this.f.setEmptyText(this.g(com.alensw.PicFolder.R.string.loading));
         this.L = BackgroundProgressDialog.d.a(new SearchFoldersTask(this, this.E.getStringExtra("QuickPic.keyword")));
      }
   }
}
