package com.alensw.ui.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class s implements OnClickListener {
   final com.alensw.ui.activity.p a;

   s(com.alensw.ui.activity.p var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface dialogInterface, int i) {
        com.alensw.ui.activity.a.a(this.a, (String) null, "http://www.google.com/search?q=Android+4.4+External+SD+Card");
    }
}
