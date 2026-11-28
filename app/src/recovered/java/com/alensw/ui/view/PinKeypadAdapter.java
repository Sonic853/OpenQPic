package com.alensw.ui.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

class PinKeypadAdapter extends BaseAdapter {
   final OnClickListener a;
   final PasswordView b;

   PinKeypadAdapter(PasswordView var1, OnClickListener var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public int getCount() {
      return PasswordView.a.length;
   }

   @Override
   public Object getItem(int var1) {
      return PasswordView.a[var1];
   }

   @Override
   public long getItemId(int var1) {
      return PasswordView.a[var1];
   }

   @Override
   public View getView(int var1, View var2, ViewGroup var3) {
      TextView var5;
      if (var2 == null) {
         var5 = (TextView)View.inflate(this.b.getContext(), com.alensw.PicFolder.R.layout.pin_code, null);
         var5.setOnClickListener(this.a);
      } else {
         var5 = (TextView)var2;
      }

      char var4 = (char)this.getItemId(var1);
      var5.setText(Character.toString(var4));
      var5.setTag(var4);
      return var5;
   }
}
