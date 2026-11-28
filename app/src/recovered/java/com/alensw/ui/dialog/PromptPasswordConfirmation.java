package com.alensw.ui.dialog;

import android.content.Context;
import com.alensw.ui.view.PasswordView;

final class PromptPasswordConfirmation implements PasswordCheckCallback {
   final Context a;
   final boolean b;
   final PasswordCheckCallback c;

   PromptPasswordConfirmation(Context var1, boolean var2, PasswordCheckCallback var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public boolean a(PasswordView var1, String var2) {
      if (var2.length() > 0) {
         new GalleryPasswordDialog(this.a, this.a.getString(com.alensw.PicFolder.R.string.password_confirm), this.b, new ConfirmNewPassword(this, var2), null).show();
      } else {
         this.c.a(var1, "");
      }

      return true;
   }
}
