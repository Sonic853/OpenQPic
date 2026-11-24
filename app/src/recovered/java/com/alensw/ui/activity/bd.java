package com.alensw.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import com.alensw.ui.view.ContainerLayout;

class bd extends ContainerLayout {
   final com.alensw.ui.activity.ba a;
   private final Rect c;
   private final Rect d;

   bd(com.alensw.ui.activity.ba var1, Context var2) {
      super(var2);
      this.a = var1;
      this.c = new Rect();
      this.d = new Rect();
   }

   @Override
   protected boolean fitSystemWindows(Rect var1) {
      if (!this.c.equals(var1)) {
         this.c.set(var1);
         this.d.set(var1);
         this.setPadding(this.d.left, this.d.top, this.d.right, this.d.bottom);
      }

      return super.fitSystemWindows(var1);
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      this.d.set(this.c);
      this.setPadding(this.d.left, this.d.top, this.d.right, this.d.bottom);
   }
}
