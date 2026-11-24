package com.alensw.ui.view;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.bean.UriFile;
import java.util.HashMap;
import java.util.Iterator;

public abstract class z extends BaseAdapter {
   private final int a;
   private final int b;
   private final HashMap c;
   private final HashMap d;

   public z(int var1, int var2) {
      this.a = var1;
      this.b = var2;
      this.c = new HashMap(256);
      this.d = new HashMap(64);
   }

   public static void a(ImageView var0, com.alensw.b.h.h var1) {
      if (var1 != null) {
         var1.l();
      }

      Object var2 = var0.getTag();
      if (var2 != null && var2 instanceof com.alensw.b.h.h) {
         ((com.alensw.b.h.h)var2).m();
      }

      var0.setTag(var1);
      Bitmap var3;
      if (var1 != null && var1.j()) {
         var3 = var1.d();
      } else {
         var3 = null;
      }

      var0.setImageBitmap(var3);
   }

   public abstract Uri a(int var1, View var2);

   public abstract View a(int var1, ViewGroup var2);

   public ImageView a(Uri var1) {
      return (ImageView)this.d.get(var1);
   }

   public void a() {
      Iterator var1 = this.d.values().iterator();

      while (var1.hasNext()) {
         a((ImageView)var1.next(), null);
      }

      this.d.clear();
      this.c.clear();
   }

   @Override
   public final View getView(int var1, View var2, ViewGroup var3) {
      boolean var4;
      if (var2 == null) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (var4) {
         var2 = this.a(var1, var3);
      }

      ImageView var6 = (ImageView)var2.findViewById(this.a);
      TextView var8 = (TextView)var2.findViewById(this.b);
      Uri var7 = this.a(var1, var2);
      if (var7 == null) {
         if (var6 != null) {
            a(var6, null);
         }

         if (var8 != null) {
            var8.setText(null);
         }
      } else {
         CommonFile var5 = (CommonFile)this.c.get(var7);
         CommonFile var9 = var5;
         if (var5 == null) {
            var9 = UriFile.a(var7);
            this.c.put(var7, var9);
         }

         if (var6 != null) {
            this.d.put(var7, var6);
            com.alensw.b.h.h var10 = (com.alensw.b.h.h)QuickApp.w.b(var9);
            a(var6, var10);
            if (var10 == null) {
               QuickApp.t.a(var9, false, new com.alensw.ui.view.aa(this, var6, var7));
            }
         }

         if (var8 != null) {
            var8.setText(var9.a());
         }
      }

      return var2;
   }
}
