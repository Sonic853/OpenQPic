package com.alensw.ui.view;

import android.net.Uri;
import android.widget.ImageView;
import com.alensw.PicFolder.QuickApp;
import com.alensw.cache.LruEntry;
import com.alensw.gallery.ThumbnailCallback;
import com.alensw.image.PictureBitmap;
class BindListThumbnail implements ThumbnailCallback, Runnable {
   final ThumbnailListAdapter a;
   private final ImageView b;
   private final Uri c;
   private PictureBitmap d;

   public BindListThumbnail(ThumbnailListAdapter var1, ImageView var2, Uri var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public void a(Object var1, PictureBitmap var2) {
      this.d = (PictureBitmap)var2.l();
      QuickApp.w.a(var1, (LruEntry)var2.l());
      this.b.post(this);
   }

   @Override
   public void run() {
      if (this.a.a(this.c) == this.b) {
         ThumbnailListAdapter.a(this.b, this.d);
      }

      if (this.d != null) {
         this.d.m();
      }

      this.d = null;
   }
}
