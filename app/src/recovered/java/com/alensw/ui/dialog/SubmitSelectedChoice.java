package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Spinner;
import com.alensw.ui.view.TitleView;

final class SubmitSelectedChoice implements OnClickListener {
   final View a;
   final TextInputCallback b;

   SubmitSelectedChoice(View var1, TextInputCallback var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      Object var3;
      if (this.a instanceof Spinner) {
         var3 = ((Spinner)this.a).getSelectedItem();
      } else {
         var3 = ((TitleView)this.a).getText();
      }

      this.b.a(var3.toString());
   }
}
