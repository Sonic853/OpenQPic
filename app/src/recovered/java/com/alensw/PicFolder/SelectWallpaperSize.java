package com.alensw.PicFolder;

import android.content.Intent;
import android.graphics.Point;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;

class SelectWallpaperSize implements OnItemClickListener {
   final ArrayList a;
   final Intent b;
   final WallpaperActivity c;

   SelectWallpaperSize(WallpaperActivity var1, ArrayList var2, Intent var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      Point var6 = (Point)this.a.get(var3);
      this.c.a(this.b, var6.x, var6.y);
   }
}
