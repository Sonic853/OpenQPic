package com.alensw.ui.dialog;

import com.alensw.ui.view.PasswordView;

class ConfirmNewPassword implements PasswordCheckCallback {
   final String a;
   final PromptPasswordConfirmation b;

   ConfirmNewPassword(PromptPasswordConfirmation var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean a(PasswordView var1, String var2) {
      if (this.a.equals(var2)) {
         this.b.c.a(var1, this.a);
      } else {
         GalleryPasswordDialog.a(var1);
      }

      return true;
   }
}
