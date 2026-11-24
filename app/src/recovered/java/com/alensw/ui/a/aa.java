package com.alensw.ui.a;

import android.content.Context;
import com.alensw.ui.view.PasswordView;

final class aa implements com.alensw.ui.a.ad {
   final Context a;
   final boolean b;
   final com.alensw.ui.a.ad c;

   aa(Context var1, boolean var2, com.alensw.ui.a.ad var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public boolean a(PasswordView var1, String var2) {
      if (var2.length() > 0) {
         new com.alensw.ui.a.x(this.a, this.a.getString(com.alensw.PicFolder.R.string.password_confirm), this.b, new com.alensw.ui.a.ab(this, var2), null).show();
      } else {
         this.c.a(var1, "");
      }

      return true;
   }
}
