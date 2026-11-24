package com.alensw.PicFolder;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

class ay implements OnPreparedListener {
   final OnPreparedListener a;
   final PlayerView b;

   ay(PlayerView var1, OnPreparedListener var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onPrepared(MediaPlayer var1) {
      PlayerView.b(this.b, var1.getVideoWidth());
      PlayerView.c(this.b, var1.getVideoHeight());
      PlayerView.a(this.b, this.b.e()[PlayerView.h(this.b)]);
      this.b.requestLayout();
      this.a.onPrepared(var1);
   }
}
