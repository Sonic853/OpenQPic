package com.alensw.gallery;

import android.net.Uri;
import com.alensw.concurrent.CancellableFutureTask;
import com.alensw.image.Picture;
public class PictureLoadFuture extends CancellableFutureTask {
   final PictureLoader a;
   private DecodePictureTask b;

   public PictureLoadFuture(PictureLoader var1, DecodePictureTask var2) {
      super(var2);
      this.a = var1;
      this.b = var2;
   }

   public Picture a() {
      Picture var1;
      if (this.isDone()) {
         var1 = this.a.a(this.b.a.i());
      } else {
         var1 = null;
      }

      if (var1 == null) {
         var1 = (Picture)DecodePictureTask.a(this.b).get();
         if (var1 != null) {
            var1 = var1.c();
         } else {
            var1 = null;
         }
      }

      return var1;
   }

   public final boolean a(Uri var1) {
      Uri var3 = this.b.a.i();
      boolean var2;
      if (var1 != var3 && !var1.equals(var3)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public final boolean b(Uri var1) {
      boolean var2;
      if (!this.isCancelled() && this.a(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   @Override
   public String toString() {
      return this.b.toString();
   }
}
