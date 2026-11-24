package com.alensw.PicFolder;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

class ar implements OnCompletionListener {
   final PlayerActivity a;

   ar(PlayerActivity var1) {
      this.a = var1;
   }

   @Override
   public void onCompletion(MediaPlayer var1) {
      this.a.p = true;
      if (this.a.q) {
         this.a.finish();
      }
   }
}
