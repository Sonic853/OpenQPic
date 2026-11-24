package com.alensw.ui.a;

import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.io.File;

class am extends BaseAdapter {
   final com.alensw.ui.a.ae a;

   am(com.alensw.ui.a.ae var1) {
      this.a = var1;
   }

   @Override
   public int getCount() {
      return this.a.k.h.size();
   }

   @Override
   public Object getItem(int var1) {
      return this.a.k.h.get(var1);
   }

   @Override
   public long getItemId(int var1) {
      return 0L;
   }

   @Override
   public View getView(int var1, View var2, ViewGroup var3) {
      TextView var6;
      if (var2 == null) {
         var6 = new TextView(this.a.a, null, 16842817);
         var6.setCompoundDrawablePadding(this.a.j);
         var6.setEllipsize(TruncateAt.END);
         var6.setGravity(19);
         var6.setMinimumHeight(this.a.i);
         var6.setPadding(this.a.j, this.a.j, this.a.j, this.a.j);
         var6.setSingleLine(true);
         var6.setTextSize(18.0F);
      } else {
         var6 = (TextView)var2;
      }

      File var7 = (File)this.a.k.h.get(var1);
      int var4 = var6.getTextColors().getDefaultColor();
      if (var7.canWrite()) {
         var1 = -16777216;
      } else {
         var1 = Integer.MIN_VALUE;
      }

      var6.setTextColor(var1 | 16777215 & var4);
      String var8;
      if (var7 == this.a.l) {
         var8 = "..";
      } else {
         var8 = var7.getName();
      }

      var6.setText(var8);
      var6.setCompoundDrawablesWithIntrinsicBounds(this.a.e, null, null, null);
      return var6;
   }
}
