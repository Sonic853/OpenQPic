package com.alensw.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
public class ConfirmPreference extends DialogPreference {
   public Runnable a;

   public ConfirmPreference(Context var1) {
      this(var1, null);
   }

   public ConfirmPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public ConfirmPreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   @Override
   protected void showDialog(Bundle bundle) {
        Context context = getContext();
        AlertDialog alertDialogA = com.alensw.ui.a.f.a(context, 0, (Object) getDialogMessage());
        if (alertDialogA == null) {
            return;
        }
        Drawable dialogIcon = getDialogIcon();
        if (dialogIcon != null) {
            alertDialogA.setIcon(dialogIcon);
        }
        alertDialogA.setTitle(getDialogTitle());
        alertDialogA.setButton(-1, context.getString(17039379), new com.alensw.ui.view.s(this));
        alertDialogA.setButton(-2, context.getString(17039369), new com.alensw.ui.view.t(this));
        com.alensw.ui.a.f.a(alertDialogA);
    }
}
