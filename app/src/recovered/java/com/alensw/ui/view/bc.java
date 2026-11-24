package com.alensw.ui.view;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.alensw.PicFolder.GalleryActivity;
import com.alensw.bean.CommonRoot;
import com.alensw.bean.MediaStoreRoot;
import java.util.ArrayList;

public class bc extends BaseAdapter {
   public static final int[][] b;
   public int c = -1;
   private final GalleryActivity d;
   private final ListView e;
   private final int f;
   private final int g;
   private final Drawable h;
   private final CommonRoot i;
   private final CommonRoot j;
   private final ArrayList k = new ArrayList(8);

   static {
      int[] var0 = new int[]{com.alensw.PicFolder.R.id.settings, com.alensw.PicFolder.R.raw.logo_settings, com.alensw.PicFolder.R.string.settings};
      b = new int[][]{{com.alensw.PicFolder.R.id.camera, com.alensw.PicFolder.R.raw.ic_menu_camera, com.alensw.PicFolder.R.string.camera}, {com.alensw.PicFolder.R.id.transfer, com.alensw.PicFolder.R.raw.logo_transfer, com.alensw.PicFolder.R.string.transfer}, var0};
   }

   public bc(GalleryActivity var1, ListView var2) {
      this.d = var1;
      this.e = var2;
      Resources var3 = this.d.getResources();
      this.f = com.alensw.ui.activity.ao.b(var1, 16842808);
      this.g = var3.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.folder_item_size);
      this.h = var3.getDrawable(com.alensw.PicFolder.R.drawable.circle_indictor);
      this.i = new CommonRoot(var3.getString(com.alensw.PicFolder.R.string.local_albums), "*/*", 0);
      this.i.l = com.alensw.PicFolder.R.raw.logo_gallery;
      this.i.n = "folders";
      this.j = new MediaStoreRoot(var3.getString(com.alensw.PicFolder.R.string.local_moments), "*/*");
      this.j.l = com.alensw.PicFolder.R.raw.logo_moments;
      this.j.n = "moments";
   }

   public CommonRoot a(String var1) {
      CommonRoot var2;
      if ("moments".equals(var1)) {
         this.c = 1;
         var2 = this.j;
      } else {
         this.c = 0;
         var2 = this.i;
      }

      return var2;
   }

   public void a() {
      if (this.k.isEmpty()) {
         this.a(this.k);
         this.notifyDataSetChanged();
      }
   }

   public void a(int var1) {
      if (this.c != var1) {
         this.c = var1;
         this.notifyDataSetChanged();
      }
   }

   public void a(ArrayList var1) {
      var1.add(0, this.i);
      var1.add(1, this.j);

      for (int[] var6 : b) {
         CommonRoot var5 = new CommonRoot(this.d.getString(var6[2]), null, 0);
         var5.l = var6[1];
         var1.add(var5);
      }
   }

   public boolean a(AdapterView var1, View var2, int var3, long var4) {
      CommonRoot var8 = this.c(var3);
      if (var8 != null) {
         for (int[] var7 : b) {
            if (var8.l == var7[1]) {
               this.d.onOptionsItemSelected(new com.alensw.b.g.l(null, this.d, 0, var7[0], null));
               break;
            }
         }
      }

      return true;
   }

   public CommonRoot c(int var1) {
      CommonRoot var2;
      if (var1 >= 0 && var1 < this.k.size()) {
         var2 = (CommonRoot)this.k.get(var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   @Override
   public int getCount() {
      return this.k.size();
   }

   @Override
   public Object getItem(int var1) {
      return this.k.get(var1);
   }

   @Override
   public long getItemId(int var1) {
      return var1;
   }

   @Override
   public View getView(int var1, View var2, ViewGroup var3) {
      byte var5 = 0;
      CommonRoot var6 = (CommonRoot)this.getItem(var1);
      com.alensw.ui.view.bm var10;
      if (var2 == null) {
         var10 = new com.alensw.ui.view.bm(this);
         var2 = View.inflate(this.d, com.alensw.PicFolder.R.layout.folder_item, null);
         var10.a = (TextView)var2.findViewById(com.alensw.PicFolder.R.id.title);
         var10.b = (TextView)var2.findViewById(com.alensw.PicFolder.R.id.summary);
         var10.c = (ImageView)var2.findViewById(com.alensw.PicFolder.R.id.icon);
         var2.setTag(var10);
      } else {
         var10 = (com.alensw.ui.view.bm)var2.getTag();
      }

      if (var10.d != var6) {
         var10.d = var6;
         var10.a.setText(var6.a());
         String var7 = var6.o;
         var10.b.setText(var7);
         TextView var8 = var10.b;
         byte var4;
         if (var7 != null && var7.length() > 0) {
            var4 = 0;
         } else {
            var4 = 8;
         }

         var8.setVisibility(var4);
         if (var6.p == null) {
            var6.p = com.alensw.b.l.b.a(this.d.getResources(), var6.l, this.f);
         }

         var10.c.setImageDrawable(var6.p);
      }

      ImageView var13 = var10.c;
      Drawable var11;
      if (var1 == this.c) {
         var11 = this.h;
      } else {
         var11 = null;
      }

      var13.setBackgroundDrawable(var11);
      boolean var9;
      if (var6.n != null) {
         var9 = true;
      } else {
         var9 = false;
      }

      int var12 = var5;
      if (var9) {
         var12 = this.g;
      }

      var2.setMinimumHeight(var12);
      return var2;
   }
}
