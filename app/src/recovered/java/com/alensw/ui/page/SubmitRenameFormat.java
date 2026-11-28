package com.alensw.ui.page;

import android.content.SharedPreferences;
import com.alensw.ui.dialog.TextInputCallback;
class SubmitRenameFormat implements TextInputCallback {
   final SharedPreferences a;
   final LocalFolderPage b;

   SubmitRenameFormat(LocalFolderPage var1, SharedPreferences var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.a.edit().putString("name_format", var1).commit();
         this.b.g(var1);
      }
   }
}
