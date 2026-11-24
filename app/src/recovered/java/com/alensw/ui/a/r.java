package com.alensw.ui.a;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Spinner;
import com.alensw.ui.view.TitleView;

final class r implements OnClickListener {
   final View a;
   final com.alensw.ui.a.s b;

   r(View var1, com.alensw.ui.a.s var2) {
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
