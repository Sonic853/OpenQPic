package com.alensw.PicFolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

@SuppressLint("Instantiatable")
class VolumeBar extends SeekBar {
   public VolumeBar(Context var1) {
      this(var1, null);
   }

   public VolumeBar(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setClickable(false);
      this.setFocusable(false);
      ColorDrawable var4 = new ColorDrawable(0);
      int var3 = Math.round(var1.getResources().getDisplayMetrics().density * 48.0F);
      var4.setBounds(0, 0, var3, var3);
      this.setThumb(var4);
   }

   @Override
   public void onDraw(Canvas var1) {
      com.alensw.compat.LegacyPlatform.save(var1, 1);
      var1.rotate(270.0F);
      var1.translate(-this.getHeight(), 0.0F);
      super.onDraw(var1);
      var1.restore();
   }

   @Override
   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var2, var1, var4, var3);
   }

   @Override
   public boolean onTouchEvent(MotionEvent var1) {
      return false;
   }

   @Override
   public void setProgress(int var1) {
      super.setProgress(var1);
      this.invalidate();
   }
}
