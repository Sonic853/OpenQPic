package com.alensw.gallery;

import android.net.Uri;
import com.alensw.PicFolder.QuickApp;
import com.alensw.concurrent.CancellableCallable;
import com.alensw.image.Picture;
import com.alensw.image.PictureBitmap;
import java.io.File;

class SavePreviewTask implements CancellableCallable {
   final Uri a;
   final PreviewWriteState b;
   final String c;
   final long d;
   final PictureLoader e;

   SavePreviewTask(PictureLoader var1, Uri var2, PreviewWriteState var3, String var4, long var5) {
      this.e = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
   }

   @Override
   public void a() {
      PictureLoader.b(this.e).remove(this.a);
   }

   @Override
   public void b() {
      PictureLoader.b(this.e).remove(this.a);
   }

   public Void c() {
      Picture var3 = this.e.a(this.a);
      if (var3 != null) {
         boolean var1 = var3.j();
         if (var1 && !PictureLoader.a(this.e, this.a)) {
            this.b.a = true;
            File var4 = PictureLoader.a(this.e).f();
            if (PictureLoader.a(var4.getPath(), var3)) {
               String var2;
               if (this.c != null) {
                  var2 = "file://" + this.c;
               } else {
                  var2 = this.a.toString();
               }

               this.e.savePreview(var2, this.d, var4);
            } else {
               var4.delete();
             }

            this.b.a = false;
         }

         if (var1 && this.c == null) {
            ThumbnailDatabase var6 = QuickApp.r;
            PictureBitmap var5 = var3.a(var6.d, var6.e, var6.b, ThumbnailDatabase.a);
            if (var5 != null) {
               var6.a(this.a.toString(), this.d, var5);
               var5.m();
            }
         }

         var3.m();
      }

      return null;
   }

   @Override
   public Object call() {
      return this.c();
   }
}
