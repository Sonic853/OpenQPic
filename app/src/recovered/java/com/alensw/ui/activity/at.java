package com.alensw.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

class at implements OnGlobalLayoutListener {
   final View a;
   final com.alensw.ui.activity.ao b;

   at(com.alensw.ui.activity.ao var1, View var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onGlobalLayout() {
      View var1 = com.alensw.ui.activity.ao.b((ViewGroup)this.a);
      if (var1 instanceof ImageView) {
         this.a.getViewTreeObserver().removeGlobalOnLayoutListener(this);
         com.alensw.ui.activity.ao.a(var1, this.b.X);
      }
   }
}
