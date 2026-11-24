package com.alensw.ui.view;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

class aw implements OnClickListener {
   final PasswordView a;

   aw(PasswordView var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      char var2 = (Character)var1.getTag();
      CharSequence var4 = PasswordView.b(this.a).getText();
      if (var2 == 8592) {
         int var3 = var4.length();
         if (var3 > 0) {
            PasswordView.b(this.a).setText(TextUtils.substring(var4, 0, var3 - 1));
         }
      } else if (var2 == 10003) {
         if (PasswordView.a(this.a) != null) {
            PasswordView.a(this.a).a(this.a, var4.toString());
         }
      } else {
         PasswordView.b(this.a).setText(TextUtils.concat(var4, Character.toString(var2)));
      }
   }
}
