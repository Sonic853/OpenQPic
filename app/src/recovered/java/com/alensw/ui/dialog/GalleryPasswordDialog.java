package com.alensw.ui.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import com.alensw.PicFolder.QuickApp;
import com.alensw.ui.view.PasswordView;

public class GalleryPasswordDialog extends GalleryAlertDialog {
   public GalleryPasswordDialog(Context var1, String var2, boolean var3, PasswordCheckCallback var4, Runnable var5) {
      super(var1);
      super.setTitle(var2);
      super.a(com.alensw.PicFolder.R.layout.password);
      PasswordView var6 = (PasswordView)this.b;
      var6.setPatternMode(var3);
      var6.setInputListener(new SubmitDialogPassword(this, var4));
      if (var5 != null) {
         super.setOnCancelListener(new CancelPasswordDialog(this, var5));
      }
   }

   public static void a(Context var0, String var1, Runnable var2, Runnable var3) {
      a(var0, "password", var1, var2, var3);
   }

   public static void a(Context var0, String var1, String var2, Runnable var3, Runnable var4) {
      boolean var5 = false;
      SharedPreferences var8 = PreferenceManager.getDefaultSharedPreferences(var0);
      boolean var6 = "password".equals(var1);
      boolean var7 = var8.getBoolean(var2, false);
      var1 = var8.getString(var1, "");
      if (var7 && var1.length() != 0 && (!var6 || !var1.equals(QuickApp.y))) {
         if (!Character.isDigit(var1.charAt(0))) {
            var5 = true;
         }

         new GalleryPasswordDialog(var0, var0.getString(com.alensw.PicFolder.R.string.password_input), var5, new VerifyStoredPassword(var1, var6, var3, var4), var4).show();
      } else {
         var3.run();
      }
   }

   public static void a(Context var0, boolean var1, PasswordCheckCallback var2) {
      new GalleryPasswordDialog(var0, var0.getString(com.alensw.PicFolder.R.string.password_setup), var1, new PromptPasswordConfirmation(var0, var1, var2), null).show();
   }

   public static void a(View var0) {
      Toast.makeText(var0.getContext(), com.alensw.PicFolder.R.string.password_invalid, 0).show();
      var0.performHapticFeedback(1, 3);
   }
}
