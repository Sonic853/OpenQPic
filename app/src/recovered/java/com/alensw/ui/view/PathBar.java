package com.alensw.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;

public class PathBar extends HorizontalScrollView {
   public static final File a = new File(File.separator);
   private File b = a;
   private OnClickListener c;
   private final int d;
   private final int e;
   private final int f;
   private final LinearLayout g;
   private final LinearLayout.LayoutParams h = new LinearLayout.LayoutParams(-2, -1);
   private final OnClickListener i = new com.alensw.ui.view.az(this);

   public PathBar(Context var1) {
      this(var1, null);
   }

   public PathBar(Context var1, AttributeSet var2) {
      super(var1, var2);
      DisplayMetrics var3 = this.getResources().getDisplayMetrics();
      this.d = (int)(var3.density * 8.0F);
      this.e = Math.min(var3.widthPixels, var3.heightPixels) * 3 / 4;
      this.f = com.alensw.ui.activity.ao.b(var1, 16842806);
      this.g = new LinearLayout(var1);
      this.g.setGravity(16);
      this.addView(this.g);
      this.setHorizontalScrollBarEnabled(false);
      this.setPath(a);
   }

   static OnClickListener a(PathBar var0) {
      return var0.c;
   }

   private TextView a(int var1) {
      TextView var2 = this.a(">", var1);
      var2.setClickable(false);
      var2.setFocusable(false);
      return var2;
   }

   private TextView a(File var1, int var2) {
      String var3;
      if (var1.equals(a)) {
         var3 = "root";
      } else {
         var3 = var1.getName();
      }

      TextView var4 = this.a(var3, var2);
      var4.setTag(var1);
      return var4;
   }

   private TextView a(String var1, int var2) {
      TextView var3 = (TextView)View.inflate(this.getContext(), com.alensw.PicFolder.R.layout.action_text_hide, null);
      var3.setMinWidth(this.d);
      var3.setMaxWidth(this.e);
      var3.setText(var1);
      var3.setTextColor(this.f);
      this.g.addView(var3, var2, this.h);
      return var3;
   }

   static int b(PathBar var0) {
      return var0.computeHorizontalScrollRange();
   }

   public File getPath() {
      return this.b;
   }

   @Override
   public void setOnClickListener(OnClickListener var1) {
      this.c = var1;
   }

   public void setPath(File var1) {
      if (var1 != null) {
         int var2 = this.g.getChildCount();
         if (!this.b.equals(var1) || var2 <= 0) {
            String var5 = var1.getPath();
            if (this.b.getPath().startsWith(com.alensw.b.l.b.e(var5)) && var2 > 0) {
               int var3 = var5.length();
               var2--;

               while (var2 >= 0) {
                  Object var9 = this.g.getChildAt(var2).getTag();
                  if (var9 != null && ((File)var9).getPath().length() <= var3) {
                     break;
                  }

                  this.g.removeViewAt(var2);
                  var2--;
               }

               TextView var10 = (TextView)this.g.getChildAt(var2);
               if (var10 != null) {
                  var10.setPaintFlags(var10.getPaintFlags() & -9);
                  var10.setOnClickListener(null);
               }
            } else {
               File var8 = var1.getParentFile();
               this.g.removeAllViews();

               for (; var8 != null; var8 = var8.getParentFile()) {
                  boolean var4 = var8.equals(a);
                  TextView var6 = this.a(var8, 0);
                  var6.setPaintFlags(var6.getPaintFlags() | 8);
                  var6.setOnClickListener(this.i);
                  if (!var4) {
                     this.a(0);
                  }
               }

               if (!a.equals(var1)) {
                  this.a(-1);
               }

               this.a(var1, -1);
            }

            this.b = var1;
            this.post(new com.alensw.ui.view.ba(this));
         }
      }
   }
}
