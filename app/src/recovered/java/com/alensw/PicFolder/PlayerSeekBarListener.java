package com.alensw.PicFolder;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.alensw.utils.GalleryUtils;
class PlayerSeekBarListener implements OnSeekBarChangeListener {
   final PlayerActivity a;

   PlayerSeekBarListener(PlayerActivity var1) {
      this.a = var1;
   }

   @Override
   public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
      if (var1 == this.a.d && var3) {
         var2 = (int)((long)this.a.k.getDuration() * var2 / this.a.d.getMax());
         this.a.k.seekTo(var2);
         this.a.f.setText(GalleryUtils.a((long)var2));
      } else if (var1 == this.a.c && var3) {
         this.a.t.setStreamVolume(3, var2, 0);
         this.a.e();
      }
   }

   @Override
   public void onStartTrackingTouch(SeekBar var1) {
      if (var1 == this.a.d) {
         this.a.r = true;
         this.a.u();
         PlayerActivity.e(this.a).setVisibility(0);
         PlayerActivity.f(this.a).removeMessages(100);
      } else if (var1 == this.a.c) {
         this.a.u();
         this.a.a.setVisibility(0);
         this.a.e();
      }
   }

   @Override
   public void onStopTrackingTouch(SeekBar var1) {
      if (var1 == this.a.d) {
         this.a.r = false;
         this.a.b(true);
         this.a.d();
         this.a.f(1500);
         PlayerActivity.g(this.a).sendEmptyMessage(100);
      } else if (var1 == this.a.c) {
         this.a.f(1500);
      }
   }
}
