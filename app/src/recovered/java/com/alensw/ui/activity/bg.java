package com.alensw.ui.activity;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class bg extends BaseAdapter {
   final LayoutInflater a;
   final Drawable b;
   final PathListActivity c;

   bg(PathListActivity var1, LayoutInflater var2, Drawable var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public int getCount() {
      return this.c.a.size();
   }

   @Override
   public Object getItem(int var1) {
      return null;
   }

   @Override
   public long getItemId(int var1) {
      return 0L;
   }

   @Override
   public View getView(int var1, View var2, ViewGroup var3) {
      View var8 = var2;
      if (var2 == null) {
         var8 = this.a.inflate(com.alensw.PicFolder.R.layout.text_button, null);
         ImageView var6 = (ImageView)var8.findViewById(com.alensw.PicFolder.R.id.icon);
         var6.setImageDrawable(this.b);
         var6.setOnClickListener(this.c.f);
      }

      String var7 = (String)this.c.a.get(var1);
      TextView var5 = (TextView)var8.findViewById(com.alensw.PicFolder.R.id.title);
      ImageView var4 = (ImageView)var8.findViewById(com.alensw.PicFolder.R.id.icon);
      var5.setText(var7);
      var4.setTag(var7);
      return var8;
   }
}
