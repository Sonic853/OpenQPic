package com.alensw.ui.activity;

import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
class bk extends BaseAdapter {
   final SelectThemeActivity a;

   bk(SelectThemeActivity var1) {
      this.a = var1;
   }

   @Override
   public int getCount() {
      return SelectThemeActivity.c(this.a).size();
   }

   @Override
   public Object getItem(int var1) {
      return SelectThemeActivity.c(this.a).get(var1);
   }

   @Override
   public long getItemId(int var1) {
      return 0L;
   }

   @Override
   public View getView(int var1, View var2, ViewGroup var3) {
      Object var4 = null;
      View var10 = var2;
      if (var2 == null) {
         var10 = View.inflate(this.a, com.alensw.PicFolder.R.layout.theme_item, null);
         ((ImageView)var10.findViewById(com.alensw.PicFolder.R.id.home)).setImageDrawable(new android.support.a.a.m(this.a));
         ((ImageView)var10.findViewById(com.alensw.PicFolder.R.id.menu))
            .setImageDrawable(com.b.a.b.a(this.a.getResources(), com.alensw.PicFolder.R.raw.ic_menu_overflow, this.a.X, SelectThemeActivity.d(this.a)));
      }

      com.alensw.ui.activity.bl var6 = (com.alensw.ui.activity.bl)SelectThemeActivity.c(this.a).get(var1);
      var10.findViewById(com.alensw.PicFolder.R.id.title_bar).setBackgroundColor(var6.c);
      ((android.support.a.a.m)((ImageView)var10.findViewById(com.alensw.PicFolder.R.id.home)).getDrawable()).a(var6.e);
      Drawable var7 = ((ImageView)var10.findViewById(com.alensw.PicFolder.R.id.menu)).getDrawable();
      ColorFilter var5 = (ColorFilter)SelectThemeActivity.e(this.a).get(var6.e);
      Object var8 = var5;
      if (var5 == null) {
         var8 = new PorterDuffColorFilter(var6.e, Mode.SRC_IN);
         SelectThemeActivity.e(this.a).put(var6.e, var8);
      }

      var7.setColorFilter((ColorFilter)var8);
      ImageView var11 = (ImageView)var10.findViewById(com.alensw.PicFolder.R.id.check);
      var8 = var4;
      if (var6.a == this.a.T) {
         var8 = var4;
         if (var6.c == this.a.V) {
            var8 = SelectThemeActivity.f(this.a);
         }
      }

      var11.setImageDrawable((Drawable)var8);
      var10.setBackgroundColor(var6.d);
      return var10;
   }
}
