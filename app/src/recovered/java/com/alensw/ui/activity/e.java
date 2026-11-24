package com.alensw.ui.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
class e extends com.alensw.b.g.a {
   final com.alensw.ui.activity.d k;

   e(com.alensw.ui.activity.d var1, Context var2, int var3) {
      super(var2, var3);
      this.k = var1;
   }

   public Drawable a(Resources var1, int var2, int var3) {
      StateListDrawable var4 = new StateListDrawable();
      var4.addState(i, com.b.a.b.a(var1, com.alensw.PicFolder.R.raw.ic_menu_done, var2, var3));
      var4.addState(j, new ColorDrawable(0));
      return var4;
   }

   public Drawable b(Resources var1, int var2, int var3, boolean var4) {
      StateListDrawable var5 = new StateListDrawable();
      com.alensw.ui.activity.f var6 = new com.alensw.ui.activity.f(this, com.b.a.b.a(var1, com.alensw.PicFolder.R.raw.ic_button_forward, var2, var3).a(), var3, var3, var4);
      var5.addState(i, var6);
      var5.addState(j, var6);
      return var5;
   }

   @Override
   public View getView(int var1, View var2, ViewGroup var3) {
      var2 = super.getView(var1, var2, var3);
      if (((com.alensw.b.g.l)this.h.get(var1)).getItemId() == this.k.f) {
         CompoundButton var6 = (CompoundButton)var2.findViewById(com.alensw.PicFolder.R.id.check);
         Resources var4 = var6.getResources();
         if (this.k.f == com.alensw.PicFolder.R.id.sort_default) {
            var6.setButtonDrawable(this.a(var4, this.b, this.a));
         } else {
            var6.setButtonDrawable(this.b(var4, this.b, this.a, this.k.g));
         }
      }

      return var2;
   }
}
