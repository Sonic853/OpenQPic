package com.alensw.PicFolder;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;

class be extends BaseAdapter {
   final ArrayList a;
   final WallpaperActivity b;

   be(WallpaperActivity var1, ArrayList var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public int getCount() {
      return this.a.size();
   }

   @Override
   public Object getItem(int var1) {
      return this.a.get(var1);
   }

   @Override
   public long getItemId(int var1) {
      return 0L;
   }

   @Override
   public View getView(int var1, View var2, ViewGroup var3) {
      ImageView var4;
      if (var2 != null) {
         var4 = (ImageView)var2;
      } else {
         var4 = new ImageView(this.b);
      }

      var4.setScaleType(ScaleType.CENTER_INSIDE);
      var4.setImageDrawable(this.b.a((Point)this.a.get(var1)));
      return var4;
   }
}
