package com.alensw.ui.dialog;

import com.alensw.ui.view.PasswordInputListener;
import com.alensw.ui.view.PasswordView;
class SubmitDialogPassword implements PasswordInputListener {
   final PasswordCheckCallback a;
   final GalleryPasswordDialog b;

   SubmitDialogPassword(GalleryPasswordDialog var1, PasswordCheckCallback var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(PasswordView var1, String var2) {
      if (this.a.a(var1, var2)) {
         this.b.dismiss();
      }
   }
}
