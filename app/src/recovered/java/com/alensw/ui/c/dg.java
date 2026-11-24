package com.alensw.ui.c;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
class dg implements OnClickListener {
   final float[] a;
   final TextView b;
   final com.alensw.ui.c.df c;

   dg(com.alensw.ui.c.df var1, float[] var2, TextView var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void onClick(View var1) {
      com.alensw.ui.activity.a.a(com.alensw.ui.c.df.a(this.c), this.a[0], this.a[1], this.b);
   }
}
