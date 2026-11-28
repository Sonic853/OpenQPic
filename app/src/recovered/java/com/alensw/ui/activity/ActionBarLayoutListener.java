package com.alensw.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

class ActionBarLayoutListener implements OnGlobalLayoutListener {
   final View a;
   final GalleryChromeActivity b;

   ActionBarLayoutListener(GalleryChromeActivity var1, View var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onGlobalLayout() {
      View var1 = GalleryChromeActivity.b((ViewGroup)this.a);
      if (var1 instanceof ImageView) {
         this.a.getViewTreeObserver().removeGlobalOnLayoutListener(this);
         GalleryChromeActivity.a(var1, this.b.X);
      }
   }
}
