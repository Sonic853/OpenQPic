package com.alensw.ui.view;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;

class ScrollDrawableCallback implements Callback {
   final ScrollableGalleryView a;

   ScrollDrawableCallback(ScrollableGalleryView var1) {
      this.a = var1;
   }

   @Override
   public void invalidateDrawable(Drawable var1) {
      this.a.invalidate();
   }

   @Override
   public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
   }

   @Override
   public void unscheduleDrawable(Drawable var1, Runnable var2) {
   }
}
