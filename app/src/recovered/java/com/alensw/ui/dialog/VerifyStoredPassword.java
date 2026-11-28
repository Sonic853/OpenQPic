package com.alensw.ui.dialog;

import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.view.PasswordView;

final class VerifyStoredPassword implements PasswordCheckCallback {
   final String a;
   final boolean b;
   final Runnable c;
   final Runnable d;

   VerifyStoredPassword(String var1, boolean var2, Runnable var3, Runnable var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   @Override
   public boolean a(PasswordView var1, String var2) {
      if (this.a.equals(var2)) {
         if (this.b) {
            QuickApp.y = var2;
         }

         if (this.c != null) {
            this.c.run();
         }
      } else {
         GalleryPasswordDialog.a(var1);
         if (this.d != null) {
            this.d.run();
         }
      }

      return true;
   }
}
