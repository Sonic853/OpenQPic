package com.alensw.ui.activity;

import android.content.Context;
import com.alensw.ui.dialog.GalleryPasswordDialog;
final class AuthorizeDelete implements Runnable {
   final Context a;
   final Runnable b;

   AuthorizeDelete(Context var1, Runnable var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void run() {
      GalleryPasswordDialog.a(this.a, "protect_delete", this.b, null);
   }
}
